package io.github.cbskarmory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory.Parser.parseExpr

import scala.annotation.tailrec
import scala.io.StdIn.readLine

// $COVERAGE-OFF$
object InputReader {
    def main(args: Array[String]): Unit = {

        @tailrec
        def readInput(): Unit = {
            val ln = readLine().toLowerCase()
            //handle input
            try {
                val exp = parseExpr(tokenize(ln.toList))
                val simplifier = new Simplifier(exp)
                val ans = simplifier.explore() match {
                    case None => "unknown"
                    case Some(ex) => ex.toString
                }
                println(ans)

            } catch {
                case e: InterpreterError => println(e.toString)
            }
            if (ln == null || ln.isEmpty) () else readInput()
        }
        readInput()
    }
}
// $COVERAGE_ON$