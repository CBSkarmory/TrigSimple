package io.github.cbskamrory

import io.github.cbskarmory._
import org.scalatest._

class TokenSpec extends FlatSpec with Matchers{
    "Tokens" should "render as the class name" in  {
        TokSin().toString should be ("TokSin")
        TokCos().toString should be ("TokCos")
        EOF().toString should be ("EOF")
        TokInt("1").toString should be ("TokInt: 1")
        TokInt("1024").toString should be ("TokInt: 1024")
    }
}
