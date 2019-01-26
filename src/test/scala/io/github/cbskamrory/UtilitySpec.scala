package io.github.cbskamrory

import io.github.cbskarmory.Utility._
import org.scalatest._

class UtilitySpec extends FlatSpec with Matchers{
    "divides(a,b)" should "return true iff a divides b (handles negatives)" in {
        divides(10,4) should be (false)
        divides(-10,4) should be (false)
        divides(10,-4) should be (false)
        divides(-10,-4) should be (false)
        divides(10,5) should be (true)
        divides(-10,5) should be (true)
        divides(10,-5) should be (true)
        divides(-10,-5) should be (true)

    }
}
