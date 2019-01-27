package io.github.cbskarmory

object Utility {
    // $COVERAGE-OFF$
    val __VERSION__ = "v0.2-beta"

    def readLn(): String = {
        print("> "); scala.io.StdIn.readLine
    }

    def printIntro(): Unit = {
        println(intro)
    }

    def printSeparator(): Unit = {
        println("----------------")
    }

    def intro: String = {
        s"[TrigSimple ${__VERSION__}]" +
                s"\nType 'exit' to exit or an expression to simplify. Ctrl-C to cancel"
    }

    // $COVERAGE_ON$

    val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    val (negOne, zero, one, two, three, four) =
        (IntExpr(-1), IntExpr(0), IntExpr(1), IntExpr(2), IntExpr(3), IntExpr(4))

    @inline
    def divides(a: Int, b: Int): Boolean = {
        ((a % b) + b) % b == 0
    }
}
