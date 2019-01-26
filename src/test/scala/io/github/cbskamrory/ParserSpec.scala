package io.github.cbskamrory

import io.github.cbskarmory.Parser.parseExpr
import io.github.cbskarmory.Utility._
import io.github.cbskarmory._
import org.scalatest._

class ParserSpec extends FlatSpec with Matchers {

    private val (sin_t, csc_t, cos_t, sec_t, tan_t, cot_t) = (
            TokSin(), TokCsc(), TokCos(), TokSec(), TokTan(), TokCot()
    )
    private val (one_t, one) = (TokInt("1"), IntExpr(1))
    private val (two_t, two) = (TokInt("2"), IntExpr(2))

    "The Parser" should "parse atomic tokens into the respective expressions" in {
        parseExpr(List(TokInt("1"), EOF())) should be (IntExpr(1))
        parseExpr(List(TokInt("1024"), EOF())) should be (IntExpr(1024))
        parseExpr(List(sin_t, EOF())) should be (sin)
        parseExpr(List(csc_t, EOF())) should be (csc)
        parseExpr(List(cos_t, EOF())) should be (cos)
        parseExpr(List(sec_t, EOF())) should be (sec)
        parseExpr(List(tan_t, EOF())) should be (tan)
        parseExpr(List(cot_t, EOF())) should be (cot)
    }
    
    it should "parse basic arithmetic hybrid structures" in {
        parseExpr(List(one_t, TokPlus(), one_t, EOF())) should be (Add(one, one))
        parseExpr(List(cot_t, TokMinus(), two_t, EOF())) should be (Sub(cot, two))
        parseExpr(List(sin_t, TokTimes(), sec_t, EOF())) should be (Mult(sin,sec))
        parseExpr(List(sin_t, TokDiv(), cos_t, EOF())) should be (Div(sin, cos))
    }

    it should "treat LParen expr RParen as just expr (evaluating it first)" in {
        a [ParseError] should be thrownBy {parseExpr(List(sin_t, TokPow(), TokLParen(), two_t, TokRParen()))}
    }

    it should "execute PEMDAS with parens before exponentiation" in {
        parseExpr(List(sin_t, TokPow(), two_t, TokPlus(), two_t, EOF())) should be (Add(Pow(sin, two), two))
        parseExpr(List(sin_t, TokPow(),
            TokLParen(), two_t, TokPlus(), two_t, TokRParen(), EOF())) should be (Pow(sin, Add(two, two)))
    }

    it should "execute PEMDAS with mult/div before add/sub" in {
        parseExpr(List(one_t, TokPlus(), two_t, TokTimes(), two_t, EOF())) should be (Add(one, Mult(two, two)))
        parseExpr(List(sin_t, TokPlus(), csc_t, TokTimes(), cot_t, EOF())) should be (Add(sin, Mult(csc, cot)))
    }

    it should "throw ParseError if malformed input is encountered" in {
        a [ParseError] should be thrownBy {parseExpr(List())}
        a [ParseError] should be thrownBy {parseExpr(List(TokLParen()))}
        a [ParseError] should be thrownBy {parseExpr(List(EOF()))}
        try {parseExpr(List(EOF()))} catch {
            case e: Throwable =>
                e.isInstanceOf[ParseError] should be (true)
                e.toString should be ("Encountered EOF token early; is the token list empty?")
        }
        a [ParseError] should be thrownBy {parseExpr(List(TokPlus(), TokPlus(), EOF()))}
        a [ParseError] should be thrownBy {parseExpr(List(sin_t, TokPlus(), EOF()))}
    }
}