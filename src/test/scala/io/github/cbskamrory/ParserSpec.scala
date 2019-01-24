package io.github.cbskamrory

import io.github.cbskarmory.Parser.parseExpr
import io.github.cbskarmory._
import org.scalatest._

class ParserSpec extends FlatSpec with Matchers {

    private val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    private val (one_t, one) = (TokInt("1"), IntExpr(1))
    private val (two_t, two) = (TokInt("2"), IntExpr(2))

    "The Parser" should "parse atomic tokens into the respective expressions" in {
        parseExpr(List(TokInt("1"), EOF())) should be (IntExpr(1))
        parseExpr(List(TokInt("1024"), EOF())) should be (IntExpr(1024))
        parseExpr(List(TokSin(), EOF())) should be (sin)
        parseExpr(List(TokCsc(), EOF())) should be (csc)
        parseExpr(List(TokCos(), EOF())) should be (cos)
        parseExpr(List(TokSec(), EOF())) should be (sec)
        parseExpr(List(TokTan(), EOF())) should be (tan)
        parseExpr(List(TokCot(), EOF())) should be (cot)
    }

    it should "parse basic arithmetic hybrid structures" in {
        parseExpr(List(one_t, TokPlus(), one_t, EOF())) should be (Add(one, one))
        parseExpr(List(TokSin(), TokTimes(), TokSec(), EOF())) should be (Mult(sin,sec))
    }

    it should "execute order of ops with mult/div before add/sub" in {
        parseExpr(List(one_t, TokPlus(), two_t, TokTimes(), two_t, EOF())) should be (Add(one, Mult(two, two)))
        parseExpr(List(TokSin(), TokPlus(), TokCsc(), TokTimes(), TokCot(), EOF())) should be (Add(sin, Mult(csc, cot)))
    }

    it should "throw ParseError if malformed input is encountered" in {
        a[ParseError] should be thrownBy {parseExpr(List())}
        a[ParseError] should be thrownBy {parseExpr(List(EOF()))}
        try {parseExpr(List(EOF()))} catch {
            case e: Throwable =>
                e.isInstanceOf[ParseError] should be (true)
                e.toString should be ("Encountered EOF token early; is the token list empty?\n" +
                        s"${e.getClass.getCanonicalName}")
        }
        a[ParseError] should be thrownBy {parseExpr(List(TokPlus(), TokPlus(), EOF()))}
        a[ParseError] should be thrownBy {parseExpr(List(TokSin(), TokPlus(), EOF()))}
    }
}