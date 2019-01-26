package io.github.cbskarmory

import io.github.cbskarmory.Utility._

import scala.collection.immutable.HashSet

object Rules {

    private val basicFns = Vector(sin, csc, cos, sec, tan, cot)

    var targets : Set[Expr] = new HashSet[Expr]
    targets ++= basicFns
    targets ++= basicFns.map(Pow(_,two)) // f^2
    targets ++= basicFns.map(Mult(two,_)) // 2f
    targets ++= (0 until 65).map(IntExpr)

    var transforms: Vector[Expr => Option[Expr]] = Vector()

    // Commutativity of addition: x+y = y+x
    transforms :+= {x: Expr => x match {
        case Add(a,b) => Some(Add(b,a))
        case _ => None
    }}
    // Commutativity of multiplication: x*y = y*x
    transforms :+= {x: Expr => x match {
        case Mult(a,b) => Some(Mult(b,a))
        case _ => None
    }}
    // Associativity of addition: (a + b) + c = a + (b + c)
    transforms :+= {x: Expr => x match {
        case Add(Add(a,b),c) => Some(Add(a,Add(b,c)))
        case Add(a,Add(b,c)) => Some(Add(Add(a,b),c))
        case _ => None
    }}
    // Associativity of multiplication: (a * b) * c = a * (b * c)
    transforms :+= {x: Expr => x match {
        case Mult(Mult(a,b),c) => Some(Mult(a,Mult(b,c)))
        case Mult(a,Mult(b,c)) => Some(Mult(Mult(a,b),c))
        case _ => None
    }}
    // multiplication distributes over addition: a(x + y) = (a*x) + (a*y)
    transforms :+= {x: Expr => x match {
        case Mult(IntExpr(a), Add(e1, e2)) => Some(Add(Mult(IntExpr(a), e1), Mult(IntExpr(a), e2)))
        case Add(Mult(IntExpr(a), e1), Mult(IntExpr(b), e2)) if a == b => Some(Mult(IntExpr(a), Add(e1, e2)))
        case _ => None
    }}
    // Additive Identity
    transforms :+= {x: Expr => x match {
        case Add(`zero`, ex) => Some(ex)
        case _ => None
    }}
    // Additive Inverse
    transforms :+= {x: Expr => x match {
        case Add(ex1, Mult(`negOne`, ex2)) if ex1 == ex2 => Some(zero)
        case _ => None
    }}
    // Multiplicative Identity
    transforms :+= {x: Expr => x match {
        case Mult(`one`, ex) => Some(ex)
        case _ => None
    }}
    // Multiplicative Inverse
    transforms :+= {x: Expr => x match {
        case Mult(ex1, Div(`one`,ex2)) if ex1 == ex2 => Some(one)
        case _ => None
    }}

    // x + x = 2x
    transforms :+= {x: Expr => x match {
        case Mult(`two`, e) => Some(Add(e,e))
        case Add(a,b) if a == b => Some(Mult(`two`, a))
        case _ => None
    }}

    // x * x = x^2
    transforms :+= {x: Expr => x match {
        case Pow(e, `two`) => Some(Mult(e,e))
        case Mult(a,b) if a == b => Some(Pow(a, `two`))
        case _ => None
    }}

    // a * (x/b) = (a/b) * x
    // (a * x) / b = (a/b) * x
    transforms :+= {x: Expr => x match {
        case Mult(IntExpr(a), Div(ex, IntExpr(b))) if divides(a,b) => Some(Mult(IntExpr(a/b), ex))
        case Div(Mult(IntExpr(a), ex), IntExpr(b)) if divides(a,b) => Some(Mult(IntExpr(a/b), ex))
        case _ => None
    }}

    // (x - y) = -(y - x)
    // a(x - y) = -a(y - x)
    transforms :+= {x: Expr => x match {
        case Sub(e1, e2) => Some(Mult(IntExpr(-1), Sub(e2, e1)))
        case Mult(IntExpr(a), Sub(e1, e2)) => Some(Mult(IntExpr(-a), Sub(e2, e1)))
        case _ => None
    }}

    // add two ints
    transforms :+= {x: Expr => x match {
        case Add(IntExpr(a), IntExpr(b)) => Some(IntExpr(a + b))
        case _ => None
    }}
    // multiply two ints
    transforms :+= {x: Expr => x match {
        case Mult(IntExpr(a), IntExpr(b)) => Some(IntExpr(a * b))
        case _ => None
    }}
    // subtract two ints
    transforms :+= {x: Expr => x match {
        case Sub(IntExpr(a), IntExpr(b)) => Some(IntExpr(a - b))
        case _ => None
    }}

    // sin^2 + cos^2 = 1
    transforms :+= {x: Expr => x match {
        case Add(Pow(`sin`,`two`), Pow(`cos`,`two`)) => Some(one)
        case _ => None
    }}

    // sin / cos = tan
    transforms :+= {x: Expr => x match {
        case Div(`sin`,`cos`) => Some(`tan`)
        case `tan` => Some(Div(`sin`,`cos`))
        case _ => None
    }}
    // 1 / sin = csc
    transforms :+= {x: Expr => x match {
        case Div(`one`,`sin`) => Some(`csc`)
        case `csc` => Some(Div(`one`,`sin`))
        case _ => None
    }}
    // 1 / cos = sec
    transforms :+= {x: Expr => x match {
        case Div(`one`,`cos`) => Some(`sec`)
        case `sec` => Some(Div(`one`,`cos`))
        case _ => None
    }}
}
