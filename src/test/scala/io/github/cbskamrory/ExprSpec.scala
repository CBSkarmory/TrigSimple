package io.github.cbskamrory

import io.github.cbskarmory._
import org.scalatest._

class ExprSpec extends FlatSpec with Matchers{
    "IntExpr" should "render as the number" in  {
        IntExpr(0).toString should be ("0")
        IntExpr(1).toString should be ("1")
        IntExpr(1024).toString should be ("1024")

    }
}
