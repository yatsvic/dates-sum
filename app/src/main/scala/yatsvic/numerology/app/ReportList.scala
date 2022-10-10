package yatsvic.numerology.app

import yatsvic.numerology.sum.LocalDateOps
import yatsvic.numerology.sum.LocalDateOps._

import java.time.LocalDate

object ReportList {
  case class ReportFunctions(
    filter: LocalDate => Boolean,
    text: LocalDate => String
  )
  def reportFunctions(target: ListConfig.Target, short: Boolean): ReportFunctions = target match {
    case ListConfig.One(s1) => ReportFunctions(
      _.sum1 == s1,
      if (short) LocalDateOps.dateString else d => s"${LocalDateOps.dateString(d)}: ${d.stringSum1}"
    )
    case ListConfig.Two(s2) => ReportFunctions(
      _.sum2 == s2,
      if (short) LocalDateOps.dateString else d => s"${LocalDateOps.dateString(d)}: ${d.stringSum2}"
    )
    case ListConfig.Both(s1, s2) => ReportFunctions(
      d => d.sum1 == s1 && d.sum2 == s2,
      if (short) LocalDateOps.dateString else d =>
        s"${LocalDateOps.dateString(d)}: ${d.stringSum2}, ${d.stringSum1}"
    )
  }

  def run(config: ListConfig): List[String] = {
    val rf = reportFunctions(config.target, config.short)
    config.datesRange.datesList.filter(rf.filter).map(rf.text)
  }
}
