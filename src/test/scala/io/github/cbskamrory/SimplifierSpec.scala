package io.github.cbskamrory

import io.github.cbskarmory.{Simplifier, _}
import org.scalatest._


class SimplifierSpec extends FlatSpec with Matchers {

    private val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    private val (one, two) = (IntExpr(1), IntExpr(2))
    
    "A Simplifier" should "find an equivalent version for verbatim identities" in {
        new Simplifier(Add(Pow(sin,two), Pow(cos,two))).explore() should be (Some(one))
    }

    it should "have explore() return None for things have no simpler form found" in {
        new Simplifier(Add(Pow(sin,IntExpr(999)), Pow(cos,two))).explore() should be (None)
    }

    it should "be able evaluate +, *, and - of ints (single level, positive int result)" in {
        new Simplifier(Add(one, one)).explore() should be (Some(two))
        new Simplifier(Mult(one, one)).explore() should be (Some(one))
        new Simplifier(Sub(two, one)).explore() should be (Some(one))

    }

    it should "be able evaluate addition and multiplication of ints (nested)" in {
        new Simplifier(Mult(two, Add(one, one))).explore() should be (Some(IntExpr(4)))
        new Simplifier(Add(one, Add(one,Add(one,Add(one, one))))).explore() should be (Some(IntExpr(5)))
        new Simplifier(Mult(two, Mult(two,Mult(two,Mult(two, two))))).explore() should be (Some(IntExpr(32)))
    }

    it should "use commutativity of addition" in {
        new Simplifier(Add(Pow(cos,two), Pow(sin,two))).explore() should be (Some(one))
    }

    it should "use associativity of addition" in {
        new Simplifier(Add(Pow(sin,two), Add(Pow(cos,two), one))).explore() should be (Some(two))
    }
}