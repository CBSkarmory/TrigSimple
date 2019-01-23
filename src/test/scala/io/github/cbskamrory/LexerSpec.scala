package io.github.cbskamrory

import io.github.cbskarmory._
import org.scalatest._


class LexerSpec extends FlatSpec with Matchers {

    "The Lexer" should "parse basic trig functions as the respective tokens" in {
        Lexer.tokenize("sin".toList) should be (List(TokSin(), EOF()))
        Lexer.tokenize("csc".toList) should be (List(TokCsc(), EOF()))
        Lexer.tokenize("cos".toList) should be (List(TokCos(), EOF()))
        Lexer.tokenize("sec".toList) should be (List(TokSec(), EOF()))
        Lexer.tokenize("tan".toList) should be (List(TokTan(), EOF()))
        Lexer.tokenize("cot".toList) should be (List(TokCot(), EOF()))
    }

    it should "throw IllegalArgumentException if an unknown sequence is encountered" in {
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("asdf".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("\\sin".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("_cos".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("`tan".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("<><>".toList)}
    }
}