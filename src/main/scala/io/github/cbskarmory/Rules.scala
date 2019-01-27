package io.github.cbskarmory

import io.github.cbskarmory.Utility._

import scala.collection.immutable.HashSet

object Rules {

    private val basicFns = Vector(sin, csc, cos, sec, tan, cot)

    var targets: Set[Expr] = new HashSet[Expr]
    targets ++= basicFns
    targets ++= basicFns.map(Pow(_, two)) // f^2
    targets ++= basicFns.map(Pow(_, three)) // f^3
    targets ++= basicFns.map(Mult(two, _)) // 2f
    targets ++= basicFns.map(Mult(three, _)) // 3f
    targets ++= basicFns.map(Mult(four, _)) // 4f

    var transforms: Vector[Expr => Option[Expr]] = Vector()

    // Commutativity of addition: x+y = y+x
    transforms :+= { x: Expr =>
        x match {
            case Add(a, b) => Some(Add(b, a))
            case _ => None
        }
    }
    // Commutativity of multiplication: x*y = y*x
    transforms :+= { x: Expr =>
        x match {
            case Mult(a, b) => Some(Mult(b, a))
            case _ => None
        }
    }
    // Associativity of addition: (a + b) + c = a + (b + c); (a + b) - c = a + (b - c)
    transforms :+= { x: Expr =>
        x match {
            case Add(Add(a, b), c) => Some(Add(a, Add(b, c)))
            case Sub(Add(a, b), c) => Some(Add(a, Sub(b, c)))
            case Add(a, Add(b, c)) => Some(Add(Add(a, b), c))
            case Add(a, Sub(b, c)) => Some(Sub(Add(a, b), c))
            case _ => None
        }
    }
    // Associativity of multiplication: (a * b) * c = a * (b * c)`
    transforms :+= { x: Expr =>
        x match {
            case Mult(Mult(a, b), c) => Some(Mult(a, Mult(b, c)))
            case Mult(a, Mult(b, c)) => Some(Mult(Mult(a, b), c))
            case _ => None
        }
    }
    // Additive Identity
    transforms :+= { x: Expr =>
        x match {
            case Add(`zero`, ex) => Some(ex)
            case _ => None
        }
    }
    // Sub Identity
    transforms :+= { x: Expr =>
        x match {
            case Sub(ex, `zero`) => Some(ex)
            case _ => None
        }
    }
    // Sub Inverse
    transforms :+= { x: Expr =>
        x match {
            case Sub(ex1, ex2) if ex1 == ex2 => Some(zero)
            case _ => None
        }
    }
    // Additive Inverse
    transforms :+= { x: Expr =>
        x match {
            case Add(ex1, Mult(`negOne`, ex2)) if ex1 == ex2 => Some(zero)
            case _ => None
        }
    }
    // Multiplicative Identity
    transforms :+= { x: Expr =>
        x match {
            case Mult(`one`, ex) => Some(ex)
            case Mult(ex, `one`) => Some(ex)
            case _ => None
        }
    }
    // Multiplicative Inverse
    transforms :+= { x: Expr =>
        x match {
            case Mult(ex1, Div(`one`, ex2)) if ex1 == ex2 => Some(one)
            case _ => None
        }
    }
    // Div Identity
    transforms :+= { x: Expr =>
        x match {
            case Div(ex, `one`) => Some(ex)
            case _ => None
        }
    }
    // Div Inverse
    transforms :+= { x: Expr =>
        x match {
            case Div(ex1, ex2) if ex1 == ex2 => Some(one)
            case _ => None
        }
    }
    // multiplication distributes over addition: a(x + y) = (a*x) + (a*y)
    transforms :+= { x: Expr =>
        x match {
            case Mult(a, Add(e1, e2)) => Some(Add(Mult(a, e1), Mult(a, e2)))
            case Add(Mult(a, e1), Mult(b, e2)) if a == b => Some(Mult(a, Add(e1, e2)))
            case _ => None
        }
    }
    // basic factoring
    transforms :+= { x: Expr =>
        x match {
            case Add(ex1, Mult(ex2, otherEx)) if ex1 == ex2 => Some(Mult(ex1, Add(one, otherEx)))
            case Sub(ex1, Mult(ex2, otherEx)) if ex1 == ex2 => Some(Mult(ex1, Sub(one, otherEx)))
            case _ => None
        }
    }

    // a(a^k) = a^(k+1)
    transforms :+= { x: Expr =>
        x match {
            case Mult(ex1, Pow(ex2, IntExpr(k))) if ex1 == ex2 && k < 8 => Some(Pow(ex2, IntExpr(k + 1)))
            case Pow(ex, IntExpr(k)) if 1 < k && k < 9 => Some(Mult(ex, Pow(ex, IntExpr(k - 1))))
            case _ => None
        }
    }

    // x + x = 2x
    transforms :+= { x: Expr =>
        x match {
            case Mult(IntExpr(_), IntExpr(_)) => None
            case Mult(`two`, e) => Some(Add(e, e))
            case Add(IntExpr(_), IntExpr(_)) => None
            case Add(a, b) if a == b => Some(Mult(`two`, a))
            case _ => None
        }
    }

    // x * x = x^2
    transforms :+= { x: Expr =>
        x match {
            case Pow(IntExpr(_), IntExpr(_)) => None
            case Pow(e, `two`) => Some(Mult(e, e))
            case Mult(IntExpr(_), IntExpr(_)) => None
            case Mult(a, b) if a == b => Some(Pow(a, `two`))
            case _ => None
        }
    }

    // (a/b)^k = (a^k) / (b^k)
    transforms :+= { x: Expr =>
        x match {
            case Pow(Div(a, b), IntExpr(k)) => Some(Div(Pow(a, IntExpr(k)), Pow(b, IntExpr(k))))
            case Div(Pow(a, IntExpr(k)), Pow(b, IntExpr(j))) if j == k => Some(Pow(Div(a, b), IntExpr(k)))
            case _ => None
        }
    }

    // a * (x/b) = (a/b) * x
    // (a * x) / b = (a/b) * x
    transforms :+= { x: Expr =>
        x match {
            case Mult(expr1, Div(ex, expr2)) if expr1 == expr2 => Some(ex)
            case Mult(IntExpr(a), Div(ex, IntExpr(b))) if divides(a, b) => Some(Mult(IntExpr(a / b), ex))
            case Div(Mult(expr1, ex), expr2) if expr1 == expr2 => Some(ex)
            case Div(Mult(IntExpr(a), ex), IntExpr(b)) if divides(a, b) => Some(Mult(IntExpr(a / b), ex))
            case _ => None
        }
    }

    // (x/a) / b = (x / (ab))
    transforms :+= { x: Expr =>
        x match {
            case Div(Div(ex, a), b) => Some(Div(ex, Mult(a, b)))
            case _ => None
        }
    }

    // (a / b) / a = 1/b
    transforms :+= { x: Expr =>
        x match {
            case Div(Div(a, b), c) if a == c => Some(Div(`one`, b))
            case _ => None
        }
    }

    // 1 / (fn^k) = (1 / fn) ^ k
    transforms :+= { x: Expr =>
        x match {
            case Div(`one`, Pow(ex, IntExpr(k))) => Some(Pow(Div(`one`, ex), IntExpr(k)))
            case _ => None
        }
    }

    // (x - y) = -(y - x)
    // a(x - y) = -a(y - x)
    transforms :+= { x: Expr =>
        x match {
            case Mult(`negOne`, Sub(e1, e2)) => Some(Sub(e2, e1))
            case Sub(e1, e2) => Some(Mult(IntExpr(-1), Sub(e2, e1)))
            case Mult(IntExpr(a), Sub(e1, e2)) => Some(Mult(IntExpr(-a), Sub(e2, e1)))
            case _ => None
        }
    }

    // a / (x / y) = (ay / x)
    transforms :+= { x: Expr =>
        x match {
            case Div(numerator, Div(a, b)) => Some(Div(Mult(numerator, b), a))
            case Div(Mult(num1, num2), den) => Some(Div(num1, Div(den, num2)))
            case Div(num, den) => Some(Div(`one`, Div(den, num)))
            case _ => None
        }
    }

    // multiplying fractions
    transforms :+= { x: Expr =>
        x match {
            case Mult(Div(num1, den1), Div(num2, den2)) => Some(Div(Mult(num1, num2), Mult(den1, den2)))
            case Mult(ex1, Div(num, den)) => Some(Div(Mult(ex1, num), den))
            case _ => None
        }
    }
    // adding fractions
    transforms :+= { x: Expr =>
        x match {
            case Add(Div(a, bot1), Div(b, bot2)) if bot1 == bot2 => Some(Div(Add(a, b), bot1)) // same denominator
            case _ => None
        }
    }
    // adding frac to non-frac: (a/y) + b = (a + (yb)) / y
    transforms :+= { x: Expr =>
        x match {
            case Add(Div(a, bot), b) => Some(Div(Add(a, Mult(bot, b)), bot)) // make denominator same
            case _ => None
        }
    }
    // adding frac to 1: (a/b) + 1 = (a/b) + (b/b)
    transforms :+= { x: Expr =>
        x match {
            case Add(Div(a, b), `one`) => Some(Add(Div(a, b), Div(b, b)))
            case _ => None
        }
    }

    // add two ints
    transforms :+= { x: Expr =>
        x match {
            case Add(IntExpr(a), IntExpr(b)) => Some(IntExpr(a + b))
            case _ => None
        }
    }
    // multiply two ints
    transforms :+= { x: Expr =>
        x match {
            case Mult(IntExpr(a), IntExpr(b)) => Some(IntExpr(a * b))
            case _ => None
        }
    }
    // subtract two ints
    transforms :+= { x: Expr =>
        x match {
            case Sub(IntExpr(a), IntExpr(b)) => Some(IntExpr(a - b))
            case _ => None
        }
    }

    // sin^2 + cos^2 = 1
    transforms :+= { x: Expr =>
        x match {
            case Add(Pow(`sin`, `two`), Pow(`cos`, `two`)) => Some(one)
            case Sub(`one`, Pow(`sin`, `two`)) => Some(Pow(cos, two))
            case Sub(`one`, Pow(`cos`, `two`)) => Some(Pow(sin, two))
            case _ => None
        }
    }

    // sin / cos = tan
    transforms :+= { x: Expr =>
        x match {
            case Div(`sin`, `cos`) => Some(`tan`)
            case `tan` => Some(Div(`sin`, `cos`))
            case _ => None
        }
    }
    // 1 / sin = csc
    transforms :+= { x: Expr =>
        x match {
            case Div(`one`, `sin`) => Some(`csc`)
            case `csc` => Some(Div(`one`, `sin`))
            case _ => None
        }
    }
    // 1 / cos = sec
    transforms :+= { x: Expr =>
        x match {
            case Div(`one`, `cos`) => Some(`sec`)
            case `sec` => Some(Div(`one`, `cos`))
            case _ => None
        }
    }
    // 1 / tan = cot
    transforms :+= { x: Expr =>
        x match {
            case Div(`one`, `tan`) => Some(`cot`)
            case `cot` => Some(Div(`one`, `tan`))
            case _ => None
        }
    }

}
