package io.github.cbskarmory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory.Parser.parseExpr

import scala.annotation.tailrec
import scala.collection.mutable
import scala.io.StdIn.readLine

// $COVERAGE-OFF$
object InputReader {
    def main(args: Array[String]): Unit = {
        val seen = mutable.HashSet[Expr]()
        @tailrec
        def readInput(): Unit = {
            val ln = readLine().toLowerCase()
            //handle input
            try {
                val exp = parseExpr(tokenize(ln.toList))
                print(if (seen.contains(exp)) "already seen\n" else {seen.add(exp); exp + "\n"})
            } catch {
                case e: InterpreterError => println(e.toString)
            }


            if (ln == null || ln.isEmpty) () else readInput()
        }
        readInput()
    }
}
// $COVERAGE_ON$