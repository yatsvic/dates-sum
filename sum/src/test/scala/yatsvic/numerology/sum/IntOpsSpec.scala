package yatsvic.numerology.sum

import org.specs2.mutable.SpecificationWithJUnit
import IntOps._
import org.specs2.matcher.JUnitMustMatchers

class IntOpsSpec extends SpecificationWithJUnit {
    "digit" >> {
      IntOps.split(2022) must beSome((202, 2))
    }
    "digits" >> {
      2022.digits must beEqualTo(List(2, 0, 2, 2))
    }
}
