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
                println(parseExpr(tokenize(ln.toList)))
            } catch {
                case e: InterpreterError => println(e.toString)
            }


            if (ln == null || ln.isEmpty) () else readInput()
        }
        readInput()
    }
}
// $COVERAGE_ON$