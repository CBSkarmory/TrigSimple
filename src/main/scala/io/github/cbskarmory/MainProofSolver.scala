package io.github.cbskarmory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory.Parser.parseExpr
import io.github.cbskarmory.Utility._

import scala.annotation.tailrec

// $COVERAGE-OFF$
object MainProofSolver {
    def main(args: Array[String]): Unit = {

        printIntro()
        println("[Identity proof solver]")

        @tailrec
        def readInput(): Unit = {
            println("Enter starting expression:")
            val ln = readLn().toLowerCase()
            if (ln == "exit") {
                return
            }
            try {
                val exp = parseExpr(tokenize(ln.toList))
                println("--------")
                println(s"Parsed: $exp, enter target expr or 'retry' to re-enter starting expr:")
                val ln2 = readLn().toLowerCase()
                if ("retry" == ln2) {
                    throw new InterruptedException()
                }
                val targetExpr = parseExpr(tokenize(ln2.toList))
                println(s"Parsed: $targetExpr")

                val simplifier = new Simplifier(core = exp, targets = Set(targetExpr))
                val path = simplifier.getWork match {
                    case None => "not reachable"
                    case Some(p) => p.map(_.toString).reduce((a, b) => a + "\n" + b)
                }
                println(s"${simplifier.checks} nodes explored")
                println(s"${simplifier.getWork.getOrElse(Vector()).size} steps\n--------")
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

// $COVERAGE-ON$