package yatsvic.numerology.sum

import org.specs2.mutable.SpecificationWithJUnit
import UnfoldOps._

class UnfoldOpsSpec extends SpecificationWithJUnit {
  def inc(i: Int): Option[(Int, Int)] = {
    if (i > 5) None
    else Some((i+1, i))
  }
  "unfoldLeft" >> {
    0.unfoldLeft(inc) must beEqualTo(List(5, 4, 3, 2, 1, 0))
  }
  "unfoldRight" >> {
    0.unfoldRight(inc) must beEqualTo(List(0, 1, 2, 3, 4, 5))
  }
}
