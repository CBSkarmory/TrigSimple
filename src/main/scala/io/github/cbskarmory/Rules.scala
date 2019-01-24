package io.github.cbskarmory

import scala.collection.immutable.HashSet

object Rules {

    private val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    private val (one, two) = (IntExpr(1), IntExpr(2))
    private val basicFns = Vector(sin, csc, cos, sec, tan, cot)

    var transforms: Vector[Expr => Option[Expr]] = Vector()

    // commutativity of addition
    transforms :+= {x: Expr => x match {
        case Add(a,b) => Some(Add(b,a))
        case _ => None
    }}
    // commutativity of multiplication
    transforms :+= {x: Expr => x match {
        case Mult(a,b) => Some(Mult(b,a))
        case _ => None
    }}
    // sin^2 + cos^2 = 1
    transforms :+= {x: Expr => x match {
        case Add(Pow(`sin`,`two`), Pow(`cos`,`two`)) => Some(one)
        case _ => None
    }}

    var targets : Set[Expr] = new HashSet[Expr]
    targets ++= basicFns
    targets ++= basicFns.map(Pow(_,two))
    targets ++= basicFns.map(Mult(_,two))
    targets ++= HashSet[Expr](one, two)
}
