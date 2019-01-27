package io.github.cbskarmory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory.Parser.parseExpr
import io.github.cbskarmory.Utility._

import scala.annotation.tailrec

// $COVERAGE-OFF$
object InputReader {

    def main(args: Array[String]): Unit = {

        printIntro()

        @tailrec
        def readInput(): Unit = {
            printSeparator()
            val ln = readLn().toLowerCase()
            if (ln == "exit") {
                return
            }
            try {
                printSeparator()
                val exp = parseExpr(tokenize(ln.toList))
                println(s"Parsed: $exp")
                val simplifier = new Simplifier(exp)
                val path = simplifier.getWork match {
                    case None => "unknown"
                    case Some(p) => p.map(_.toString).reduce((a, b) => a + "\n" + b)
                }
                println(s"${simplifier.checks} nodes explored")
                println(s"${simplifier.getWork.getOrElse(Vector()).size} steps")
                printSeparator()
                println(path)

            } catch {
                case e: InterpreterError => println(e.toString + " | Please try again")
                case e: InterruptedException => println("<canceled>")
            }
            readInput()
        }

        readInput()
    }
}

// $COVERAGE_ON$