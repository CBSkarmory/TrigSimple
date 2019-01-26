package io.github.cbskarmory

import scala.math.max

// $COVERAGE-OFF$
sealed abstract class Expr {
    def depth = 1
    override def toString: String = getClass.getSimpleName.toLowerCase()
    override def hashCode(): Int = this match {
        case Sin() => 1
        case Csc() => 1 << 1
        case Cos() => 1 << 3
        case Sec() => 1 << 5
        case Tan() => 1 << 7
        case Cot() => 1 << 11
        case _ => 0
    }
    override def equals(o: Any): Boolean = o.getClass == this.getClass
}
case class IntExpr(x: Int) extends Expr {
    override def toString: String = x.toString
    override def hashCode(): Int = x.hashCode()
    override def equals(o: Any): Boolean = super.equals(o) && this.x == o.asInstanceOf[IntExpr].x
}
case class Add(a: Expr, b: Expr) extends Expr {
    private var cachedHC = 0
    override def toString: String = s"(${a.toString} + ${b.toString})"

    override def depth: Int = max(a.depth, b.depth) + 1

    override def hashCode(): Int = {
        if (0 == cachedHC) {
            cachedHC = a.hashCode() + b.hashCode()
        }
        cachedHC
    }
    override def equals(o: Any): Boolean = {
        super.equals(o) && {val other = o.asInstanceOf[Add]; other.a == this.a && other.b == this.b}
    }
}
case class Sub(l: Expr, r: Expr) extends Expr{
    override def depth: Int = max(l.depth, r.depth) + 1
    override def toString: String = s"(${l.toString} - ${r.toString})"
    override def hashCode(): Int = l.hashCode() - r.hashCode()
    override def equals(o: Any): Boolean = {
        super.equals(o) && {val other = o.asInstanceOf[Sub]; other.l == this.l && other.r == this.r}
    }
}
case class Mult(a: Expr, b: Expr) extends Expr {
    private var cachedHC = 0
    override def toString: String = s"(${a.toString} * ${b.toString})"

    override def depth: Int = max(a.depth, b.depth) + 1

    override def hashCode(): Int = {
        if (0 == cachedHC) {
            cachedHC = a.hashCode() * b.hashCode()
        }
        cachedHC
    }

    override def equals(o: Any): Boolean = {
        super.equals(o) && {val other = o.asInstanceOf[Mult]; other.a == this.a && other.b == this.b}
    }
}
case class Div(top: Expr, bot: Expr) extends Expr {
    override def depth: Int = max(top.depth, bot.depth) + 1
    override def toString: String = s"(${top.toString} / ${bot.toString})"
    override def hashCode(): Int = top.hashCode() ^ bot.hashCode()
    override def equals(o: Any): Boolean = {
        super.equals(o) && {val other = o.asInstanceOf[Div]; other.top == this.top && other.bot == this.bot}
    }
}
case class Pow(base: Expr, pow: Expr) extends Expr {
    override def depth: Int = max(base.depth, pow.depth) + 1
    override def toString: String = s"(${base.toString}^${pow.toString})"
    override def hashCode(): Int = ~(base.hashCode() ^ pow.hashCode())
    override def equals(o: Any): Boolean = {
        super.equals(o) && {val other = o.asInstanceOf[Pow]; other.base == this.base && other.pow == this.pow}
    }
}
case class Sin() extends Expr
case class Csc() extends Expr
case class Cos() extends Expr
case class Sec() extends Expr
case class Tan() extends Expr
case class Cot() extends Expr
// $COVERAGE-ON$