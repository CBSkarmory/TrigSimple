package io.github.cbskamrory

import io.github.cbskarmory.{Simplifier, _}
import org.scalatest._


class SimplifierSpec extends FlatSpec with Matchers {

    private val (sin, csc, cos, sec, tan, cot) = (Sin(), Csc(), Cos(), Sec(), Tan(), Cot())
    private val (one, two) = (IntExpr(1), IntExpr(2))
    
    "A Simplifier" should "find an equivalent version for verbatim identities" in {
        new Simplifier(Add(Pow(sin,two), Pow(cos,two))).getSimplified should be (Some(one))
        new Simplifier(Div(sin, cos)).getSimplified should be (Some(tan))
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
        //new Simplifier(Mult(two, Mult(two,Mult(two,Mult(two, two))))).getSimplified should be (Some(IntExpr(32)))
    }

    it should "use commutativity of addition" in {
        new Simplifier(Add(Pow(cos,two), Pow(sin,two))).getSimplified should be (Some(one))
    }

    it should "use associativity of addition" in {
        new Simplifier(Add(Pow(sin,two), Add(Pow(cos,two), one))).getSimplified should be (Some(two))
    }

    it should "be able to add 2 of the same trig function into 2 * that function" in {
        new Simplifier(Add(sin,sin)).getSimplified should be (Some(Mult(two, sin)))
        new Simplifier(Add(csc,csc)).getSimplified should be (Some(Mult(two, csc)))
        new Simplifier(Add(cos,cos)).getSimplified should be (Some(Mult(two, cos)))
        new Simplifier(Add(sec,sec)).getSimplified should be (Some(Mult(two, sec)))
        new Simplifier(Add(tan,tan)).getSimplified should be (Some(Mult(two, tan)))
        new Simplifier(Add(cot,cot)).getSimplified should be (Some(Mult(two, cot)))
    }

    it should "simplify with as few steps as possible" in {
        new Simplifier(Add(one, one)).getWork.get.size should be (2)
        new Simplifier(Mult(Add(one, one), two)).getWork.get.size should be (3)
        new Simplifier(Mult(two, Add(one, one))).getWork.get.size should be (3)
    }
}