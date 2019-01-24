package io.github.cbskamrory

import io.github.cbskarmory.Simplifier
import io.github.cbskarmory._
import org.scalatest._


class SimplifierSpec extends FlatSpec with Matchers {

    private val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    private val (one, two) = (IntExpr(1), IntExpr(2))
    
    "A Simplifier" should "find an equivalent version for basic identities (commutative)" in {
        new Simplifier(Add(Pow(sin,two), Pow(cos,two))).explore() should be (Some(one))
        new Simplifier(Add(Pow(cos,two), Pow(sin,two))).explore() should be (Some(one))
    }

    it should "have explore() return None for things have no simpler form found" in {
        new Simplifier(Add(Pow(sin,IntExpr(999)), Pow(cos,two))).explore() should be (None)
    }
}