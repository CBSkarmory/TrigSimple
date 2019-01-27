package io.github.cbskamrory

import io.github.cbskarmory.Utility._
import io.github.cbskarmory.{Simplifier, _}
import org.scalatest._


class SimplifierSpec extends FlatSpec with Matchers {

    "A Simplifier" should "find an equivalent version for verbatim identities" in {
        new Simplifier(Add(Pow(sin,two), Pow(cos,two))).getSimplified should be (Some(one))
        new Simplifier(Div(sin, cos)).getSimplified should be (Some(tan))
        new Simplifier(Div(one, cos)).getSimplified should be (Some(sec))
        new Simplifier(Div(one, sin)).getSimplified should be (Some(csc))
        new Simplifier(Div(one, tan)).getSimplified should be (Some(cot))
    }

    it should "have getSimplified return None for things have no simpler form found" in {
        new Simplifier(Add(Pow(sin,IntExpr(999)), Pow(cos,two))).getSimplified should be (None)
    }

    it should "be able evaluate +, *, and - of ints (single level, positive int result)" in {
        new Simplifier(Add(one, one)).getSimplified should be (Some(two))
        new Simplifier(Mult(one, one)).getSimplified should be (Some(one))
        new Simplifier(Sub(two, one)).getSimplified should be (Some(one))
    }

    it should "be able evaluate addition and multiplication of ints (nested)" in {
        new Simplifier(Mult(two, Add(one, one))).getSimplified should be (Some(IntExpr(4)))
        new Simplifier(Add(one, Add(one,Add(one,Add(one, one))))).getSimplified should be (Some(IntExpr(5)))
        new Simplifier(Mult(two, Mult(two,Mult(two,Mult(two, two))))).getSimplified should be (Some(IntExpr(32)))
    }

    it should "use commutativity of addition" in {
        new Simplifier(Add(Pow(cos,two), Pow(sin,two))).getSimplified should be (Some(one))
    }

    it should "use associativity of addition" in {
        new Simplifier(Add(Pow(sin,two), Add(Pow(cos,two), one))).getSimplified should be (Some(two))
        new Simplifier(Sub(Add(sin, cos), cos)).getSimplified should be (Some(sin))
    }

    it should "use additive identity" in {
        new Simplifier(Add(zero, two)).getSimplified should be (Some(two))
        new Simplifier(Add(zero, Pow(sec, three))).getSimplified should be (Some(Pow(sec,three)))
    }

    it should "use additive inverse" in {
        new Simplifier(Add(Pow(sin,two),Mult(negOne, Pow(sin, two)))).getSimplified should be (Some(zero))
    }

    it should "use subtractive identity" in {
        new Simplifier(Sub(one, zero)).getSimplified should be (Some(one))
        new Simplifier(Sub(Pow(sin,two),zero)).getSimplified should be (Some(Pow(sin,two)))
    }

    it should "be able to add 2 of the same trig function into 2 * that function" in {
        new Simplifier(Add(sin,sin)).getSimplified should be (Some(Mult(two, sin)))
        new Simplifier(Add(csc,csc)).getSimplified should be (Some(Mult(two, csc)))
        new Simplifier(Add(cos,cos)).getSimplified should be (Some(Mult(two, cos)))
        new Simplifier(Add(sec,sec)).getSimplified should be (Some(Mult(two, sec)))
        new Simplifier(Add(tan,tan)).getSimplified should be (Some(Mult(two, tan)))
        new Simplifier(Add(cot,cot)).getSimplified should be (Some(Mult(two, cot)))
    }

    it should "be able to cancel fn and 1/fn" in {
        new Simplifier(Mult(sin, csc)).getSimplified should be (Some(one))
        new Simplifier(Mult(cos, sec)).getSimplified should be (Some(one))
        new Simplifier(Mult(tan, cot)).getSimplified should be (Some(one))
    }

    it should "be able to move a denominator's denominator to the numerator" in {
        new Simplifier(Div(sin, csc)).getSimplified should be (Some(Pow(sin,two)))
        new Simplifier(Div(cos, sec)).getSimplified should be (Some(Pow(cos,two)))
        new Simplifier(Div(tan, cot)).getSimplified should be (Some(Pow(tan,two)))
    }

    it should "be able to cancel out the same thing in the numerator and denominator" in {
        new Simplifier(Mult(Div(sin, cos), Div(cos, sin))).getSimplified should be (Some(one))
        new Simplifier(Mult(Div(three, two), Div(two, three))).getSimplified should be (Some(one))
        new Simplifier(Mult(Div(sec, tan), Div(tan, sec))).getSimplified should be (Some(one))
    }

    it should "be able to handle basic factoring and exponents (low powers)" in {
        new Simplifier(Sub(sin, Mult(sin, Pow(cos, two)))).getSimplified should be (Some(Pow(sin, three)))
    }

    it should "handle simple identities on its own" in {
        new Simplifier(Add(Mult(tan, sin), cos)).getSimplified should be (Some(sec))
        new Simplifier(Add(Pow(tan, two), one)).getSimplified should be (Some(Pow(sec,two)))
    }

    it should "handle moderately complex identities on its own" in {
        val s1 = new Simplifier(Div(Pow(tan, two), Add(Pow(tan, two), one)))
        s1.getSimplified should be (Some(Pow(sin, two)))
        s1.getWork.get.length should be (12)
    }

    it should "simplify basic expressions with as few steps as possible" in {
        new Simplifier(Add(one, one)).getWork.get.size should be (2)
        new Simplifier(Mult(Add(one, one), two)).getWork.get.size should be (3)
        new Simplifier(Mult(two, Add(one, one))).getWork.get.size should be (3)
    }
}