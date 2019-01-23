package io.github.cbskarmory

sealed abstract class Expr
case class FnExpr(fn: TrigFn) extends Expr
case class IntExpr(x: Int) extends Expr
case class Add(a: Expr, b: Expr) extends Expr
case class Sub(l: Expr, r: Expr) extends Expr
case class Mult(a: Expr, b: Expr) extends Expr
case class Div(top: Expr, bot: Expr) extends Expr
case class Pow(base: Expr, pow: IntExpr) extends Expr


sealed abstract class TrigFn
case class Sin() extends TrigFn
case class Csc() extends TrigFn
case class Cos() extends TrigFn
case class Sec() extends TrigFn
case class Tan() extends TrigFn
case class Cot() extends TrigFn