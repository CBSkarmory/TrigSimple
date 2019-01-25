package io.github.cbskarmory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory.Parser.parseExpr

import scala.annotation.tailrec
import scala.io.StdIn.readLine

// $COVERAGE-OFF$
object InputReader {
    def main(args: Array[String]): Unit = {

        println("Type 'exit' to exit or an expression to simplify")

        @tailrec
        def readInput(): Unit = {
            val ln = readLine().toLowerCase()
            if (ln == "exit") {
                return
            }
            try {
                val exp = parseExpr(tokenize(ln.toList))
                val simplifier = new Simplifier(exp)
                val ans = simplifier.getSimplified match {
                    case None => "unknown"
                    case Some(ex) => ex.toString + "\n"
                }
                val path = simplifier.getWork match {
                    case None => ""
                    case Some(p) => p.toString()
                }
                println(ans + path)

            } catch {
                case e: InterpreterError => println(e.toString)
            }
            readInput()
        }
        readInput()
    }
}
// $COVERAGE_ON$