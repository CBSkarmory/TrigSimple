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

    it should "parse basic arithmetic symbols as the respective tokens" in {
        Lexer.tokenize("+".toList) should be(List(TokPlus(), EOF()))
        Lexer.tokenize("-".toList) should be(List(TokMinus(), EOF()))
        Lexer.tokenize("*".toList) should be(List(TokTimes(), EOF()))
        Lexer.tokenize("/".toList) should be(List(TokDiv(), EOF()))
        Lexer.tokenize("^".toList) should be(List(TokPow(), EOF()))
    }

    it should "parse left and right parens/brackets as LParen and RParen tokens" in {
        Lexer.tokenize("(".toList) should be(List(TokLParen(), EOF()))
        Lexer.tokenize("[".toList) should be(List(TokLParen(), EOF()))
        Lexer.tokenize(")".toList) should be(List(TokRParen(), EOF()))
        Lexer.tokenize("]".toList) should be(List(TokRParen(), EOF()))
    }

    it should "handle positive integers parsing digits into tokens" in {
        Lexer.tokenize("1".toList) should be(List(TokInt(1), EOF()))
        Lexer.tokenize("0".toList) should be(List(TokInt(0), EOF()))
        Lexer.tokenize("256".toList) should be(List(TokInt(2), TokInt(5), TokInt(6), EOF()))
    }

    it should "parse consecutive phrases into the respective consecutive tokens" in {
        Lexer.tokenize("sin/cos".toList) should be(List(TokSin(), TokDiv(), TokCos(), EOF()))
        Lexer.tokenize("sin^2+cos^2".toList) should be(
            List(TokSin(), TokPow(), TokInt(2), TokPlus(), TokCos(), TokPow(), TokInt(2), EOF())
        )
        Lexer.tokenize("sec^2-tan^2".toList) should be(
            List(TokSec(), TokPow(), TokInt(2), TokMinus(), TokTan(), TokPow(), TokInt(2), EOF())
        )
        Lexer.tokenize("111*2*3*4*5*6".toList) should be(
            List(TokInt(1), TokInt(1), TokInt(1), TokTimes(), TokInt(2), TokTimes(), TokInt(3), TokTimes(),
                TokInt(4), TokTimes(), TokInt(5), TokTimes(), TokInt(6), EOF())
        )
    }

    it should "ignore spaces separating phrases and spaces at the start/end" in {
        Lexer.tokenize("sin / cos".toList) should be(List(TokSin(), TokDiv(), TokCos(), EOF()))
        Lexer.tokenize("sin^    2 + cos      ^2".toList) should be(
            List(TokSin(), TokPow(), TokInt(2), TokPlus(), TokCos(), TokPow(), TokInt(2), EOF())
        )
        Lexer.tokenize("sec^2 - tan^2".toList) should be(
            List(TokSec(), TokPow(), TokInt(2), TokMinus(), TokTan(), TokPow(), TokInt(2), EOF())
        )
        Lexer.tokenize("  111  * 2 *  3*4 *   5* 6   ".toList) should be(
            List(TokInt(1), TokInt(1), TokInt(1), TokTimes(), TokInt(2), TokTimes(), TokInt(3), TokTimes(),
                TokInt(4), TokTimes(), TokInt(5), TokTimes(), TokInt(6), EOF())
        )
    }

    it should "throw IllegalArgumentException if a phrase is broken by a space" in {
        a[IllegalArgumentException] should be thrownBy {
            Lexer.tokenize("co s  ^ 2".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            Lexer.tokenize("tanse c ".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            Lexer.tokenize("s i n c o s".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            Lexer.tokenize("c ot ^ 44".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            Lexer.tokenize("asdf".toList)
        }
    }

    it should "throw IllegalArgumentException if an unknown sequence is encountered" in {
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("asdf".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("\\sin".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("_cos".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("`tan".toList)}
        a [IllegalArgumentException] should be thrownBy {Lexer.tokenize("<><>".toList)}
    }
}