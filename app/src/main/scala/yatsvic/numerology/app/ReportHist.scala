package yatsvic.numerology.app

import yatsvic.numerology.app.HistConfig.Sum
import yatsvic.numerology.sum.LocalDateOps._

import java.time.LocalDate

object ReportHist {
  def toStringShort(maxSum: Int, maxCount: Int)(sum: Int, count: Int): String =
    s"${fmt(maxSum).format(sum)} : ${fmt(maxCount).format(count)}"
  def fmt(max: Int): String = s"%0${max.toString.length}d"
  def toStringLong(maxSum: Int, maxCount: Int)(sum: Int, count: Int): String =
    s"${fmt(maxSum).format(sum)} : ${fmt(maxCount).format(count)} | ${"=".repeat(count * 20 / maxCount)}${if(count > 0) "-" else ""}"

  def run(config: HistConfig): List[String] = {

    val groupBy: LocalDate => Int = config.histType match {
      case Sum.one => _.sum1
      case Sum.two => _.sum2
    }
    val dates = config.datesRange.datesList
    val sumToCount = dates.groupBy(groupBy)
      .map { case (sum, list) => (sum, list.size) }
    val maxSum = sumToCount.keySet.max
    val maxCount = sumToCount.values.max
    val toString = if (config.short) toStringShort(maxSum, maxCount) else toStringLong(maxSum, maxCount)
    val report = Range(1, maxSum).toList.map { sum =>
      val count = sumToCount.getOrElse(sum, 0)
      toString(sum, count)
    }
    report
  }
}
