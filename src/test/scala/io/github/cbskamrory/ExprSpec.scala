package io.github.cbskamrory

import io.github.cbskarmory.Utility._
import io.github.cbskarmory._
import org.scalatest._

class ExprSpec extends FlatSpec with Matchers{
    "IntExpr" should "render as the number" in  {
        IntExpr(0).toString should be ("0")
        IntExpr(1).toString should be ("1")
        IntExpr(1024).toString should be ("1024")

    }
    it should "equal iff subexpressions are equal, and eq => hashcode eq" in {
        zero should be (zero)
        zero.hashCode() should be (zero.hashCode())
        one should be (one)
        one.hashCode() should be (one.hashCode())
        IntExpr(12345) should be (IntExpr(12345))
        IntExpr(12345).hashCode() should be (IntExpr(12345).hashCode())
    }
    
    "AddExpr" should "equal iff subexpressions are equal, and eq => hashcode eq" in {
        Add(one, two) should be (Add(one, two))
        Add(one, two).hashCode() should be (Add(one, two).hashCode())
        Add(IntExpr(123), IntExpr(456)) should be (Add(IntExpr(123), IntExpr(456)))
        Add(IntExpr(123), IntExpr(456)).hashCode() should be (Add(IntExpr(123), IntExpr(456)).hashCode())
    }

    "SubExpr" should "equal iff subexpressions are equal, and eq => hashcode eq" in {
        Sub(one, two) should be (Sub(one, two))
        Sub(one, two).hashCode() should be (Sub(one, two).hashCode())
        Sub(IntExpr(123), IntExpr(456)) should be (Sub(IntExpr(123), IntExpr(456)))
        Sub(IntExpr(123), IntExpr(456)).hashCode() should be (Sub(IntExpr(123), IntExpr(456)).hashCode())
    }

    "MultExpr" should "equal iff subexpressions are equal, and eq => hashcode eq" in {
        Mult(one, two) should be (Mult(one, two))
        Mult(one, two).hashCode() should be (Mult(one, two).hashCode())
        Mult(IntExpr(123), IntExpr(456)) should be (Mult(IntExpr(123), IntExpr(456)))
        Mult(IntExpr(123), IntExpr(456)).hashCode() should be (Mult(IntExpr(123), IntExpr(456)).hashCode())
    }
}
