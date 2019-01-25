package io.github.cbskarmory

// $COVERAGE-OFF$
sealed abstract class Expr {
    override def toString: String = getClass.getSimpleName.toLowerCase()
}
case class IntExpr(x: Int) extends Expr {
    override def toString: String = x.toString
}
case class Add(a: Expr, b: Expr) extends Expr {
    override def toString: String = s"(${a.toString} + ${b.toString})"
}
case class Sub(l: Expr, r: Expr) extends Expr{
    override def toString: String = s"(${l.toString} - ${r.toString})"
}
case class Mult(a: Expr, b: Expr) extends Expr {
    override def toString: String = s"(${a.toString} * ${b.toString})"
}
case class Div(top: Expr, bot: Expr) extends Expr {
    override def toString: String = s"(${top.toString} / ${bot.toString})"
}
case class Pow(base: Expr, pow: Expr) extends Expr {
    override def toString: String = s"(${base.toString}^${pow.toString})"
}
case class Sin() extends Expr
case class Csc() extends Expr
case class Cos() extends Expr
case class Sec() extends Expr
case class Tan() extends Expr
case class Cot() extends Expr
// $COVERAGE-ON$