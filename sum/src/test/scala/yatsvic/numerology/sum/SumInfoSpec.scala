package yatsvic.numerology.sum

import org.specs2.mutable.SpecificationWithJUnit
import LocalDateOps.SumInfo
import java.time.LocalDate
import LocalDateOps._

class SumInfoSpec extends SpecificationWithJUnit {
  "apply" >> {
    SumInfo(1954) must beEqualTo(SumInfo(List(1, 9, 5, 4)))
    SumInfo(LocalDate.parse("2022-12-16")) must beEqualTo(SumInfo(List(2, 0, 2, 2, 1, 2, 1, 6)))
  }
  "next" >> {
    SumInfo(2022).next must beNone
    SumInfo(1954).next must beSome(SumInfo(19))
  }
  "sum" >> {
    SumInfo(2022).sum must beEqualTo(6)
    SumInfo(LocalDate.parse("2022-02-24")).sum must beEqualTo(14)
  }
  "text" >> {
    SumInfo(2022).text must beEqualTo("2+0+2+2=6")
    SumInfo(LocalDate.parse("2022-02-24")).text must beEqualTo("2+0+2+2+2+2+4=14")
  }

  "list" >> {
    SumInfo.list(SumInfo(LocalDate.parse("2022-02-24"))) must beEqualTo(List(SumInfo(20220224), SumInfo(14)))
  }
}
