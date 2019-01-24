package io.github.cbskamrory

import io.github.cbskarmory.Lexer.tokenize
import io.github.cbskarmory._
import org.scalatest._


class LexerSpec extends FlatSpec with Matchers {

    "The Lexer" should "parse basic trig functions as the respective tokens" in {
        tokenize("sin".toList) should be (List(TokSin(), EOF()))
        tokenize("csc".toList) should be (List(TokCsc(), EOF()))
        tokenize("cos".toList) should be (List(TokCos(), EOF()))
        tokenize("sec".toList) should be (List(TokSec(), EOF()))
        tokenize("tan".toList) should be (List(TokTan(), EOF()))
        tokenize("cot".toList) should be (List(TokCot(), EOF()))
    }

    it should "parse basic arithmetic symbols as the respective tokens" in {
        tokenize("+".toList) should be(List(TokPlus(), EOF()))
        tokenize("-".toList) should be(List(TokMinus(), EOF()))
        tokenize("*".toList) should be(List(TokTimes(), EOF()))
        tokenize("/".toList) should be(List(TokDiv(), EOF()))
        tokenize("^".toList) should be(List(TokPow(), EOF()))
    }

    it should "parse left and right parens/brackets as LParen and RParen tokens" in {
        tokenize("(".toList) should be(List(TokLParen(), EOF()))
        tokenize("[".toList) should be(List(TokLParen(), EOF()))
        tokenize(")".toList) should be(List(TokRParen(), EOF()))
        tokenize("]".toList) should be(List(TokRParen(), EOF()))
    }

    it should "handle positive integers turning them into tokens" in {
        tokenize("1".toList) should be(List(TokInt("1"), EOF()))
        tokenize("0".toList) should be(List(TokInt("0"), EOF()))
        tokenize("256".toList) should be(List(TokInt("256"), EOF()))
    }

    it should "parse consecutive phrases into the respective consecutive tokens" in {
        tokenize("sin/cos".toList) should be(List(TokSin(), TokDiv(), TokCos(), EOF()))
        tokenize("sin^2+cos^2".toList) should be(
            List(TokSin(), TokPow(), TokInt("2"), TokPlus(), TokCos(), TokPow(), TokInt("2"), EOF())
        )
        tokenize("sec^2-tan^2".toList) should be(
            List(TokSec(), TokPow(), TokInt("2"), TokMinus(), TokTan(), TokPow(), TokInt("2"), EOF())
        )
        tokenize("123*4*5*6*7*8".toList) should be(
            List(TokInt("123"), TokTimes(), TokInt("4"), TokTimes(), TokInt("5"), TokTimes(),
                TokInt("6"), TokTimes(), TokInt("7"), TokTimes(), TokInt("8"), EOF())
        )
    }

    it should "ignore spaces separating phrases, numbers, and spaces at the start/end" in {
        tokenize("sin / cos".toList) should be(List(TokSin(), TokDiv(), TokCos(), EOF()))
        tokenize("sin^    10   2 4 + cos      ^2".toList) should be(
            List(TokSin(), TokPow(), TokInt("1024"), TokPlus(), TokCos(), TokPow(), TokInt("2"), EOF())
        )
        tokenize("sec^2 - tan^2".toList) should be(
            List(TokSec(), TokPow(), TokInt("2"), TokMinus(), TokTan(), TokPow(), TokInt("2"), EOF())
        )
        tokenize("  123  * 4 *  5*6 *   7* 8   ".toList) should be(
            List(TokInt("123"), TokTimes(), TokInt("4"), TokTimes(), TokInt("5"), TokTimes(),
                TokInt("6"), TokTimes(), TokInt("7"), TokTimes(), TokInt("8"), EOF())
        )
    }

    it should "throw IllegalArgumentException if a phrase is broken by a space" in {
        a[IllegalArgumentException] should be thrownBy {
            tokenize("co s  ^ 2".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            tokenize("tanse c ".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            tokenize("s i n c o s".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            tokenize("c ot ^ 44".toList)
        }
        a[IllegalArgumentException] should be thrownBy {
            tokenize("asdf".toList)
        }
    }

    it should "throw IllegalArgumentException if an unknown sequence is encountered" in {
        a [IllegalArgumentException] should be thrownBy {tokenize("asdf".toList)}
        a [IllegalArgumentException] should be thrownBy {tokenize("\\sin".toList)}
        a [IllegalArgumentException] should be thrownBy {tokenize("_cos".toList)}
        a [IllegalArgumentException] should be thrownBy {tokenize("`tan".toList)}
        a [IllegalArgumentException] should be thrownBy {tokenize("<><>".toList)}
    }
}