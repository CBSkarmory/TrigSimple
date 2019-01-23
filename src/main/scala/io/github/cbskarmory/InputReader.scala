package io.github.cbskarmory

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object InputReader {
    def main(args: Array[String]): Unit = {
        @tailrec
        def readInput(): Unit = {
            val ln = readLine()
            //handle input
            println(Lexer.tokenize(ln.toList))

            if (ln == null || ln.isEmpty) () else readInput()
        }
        readInput()
    }
}
