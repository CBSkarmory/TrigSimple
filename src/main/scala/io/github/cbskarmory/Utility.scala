package io.github.cbskarmory

object Utility {
    val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    val (negOne, zero, one, two, three) = (IntExpr(-1), IntExpr(0), IntExpr(1), IntExpr(2), IntExpr(3))

    @inline
    def divides(a: Int, b: Int): Boolean = {
        ((a % b) + b) % b == 0
    }
}
