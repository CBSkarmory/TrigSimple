package io.github.cbskarmory

sealed abstract class Expr
case class IntExpr(x: Int) extends Expr
case class Add(a: Expr, b: Expr) extends Expr
case class Sub(l: Expr, r: Expr) extends Expr
case class Mult(a: Expr, b: Expr) extends Expr
case class Div(top: Expr, bot: Expr) extends Expr
case class Pow(base: Expr, pow: Expr) extends Expr
case class Sin() extends Expr
case class Csc() extends Expr
case class Cos() extends Expr
case class Sec() extends Expr
case class Tan() extends Expr
case class Cot() extends Expr