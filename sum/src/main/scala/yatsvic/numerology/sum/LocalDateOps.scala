package yatsvic.numerology.sum

import IntOps._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.language.implicitConversions

object LocalDateOps {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  def dateString(ld: LocalDate): String = ld.toString

  case class SumInfo(digits: List[Int]) {
    val sum: Int = digits.sum
    def text: String = s"${digits.mkString("+")}=$sum"
    def next: Option[SumInfo] = if (sum < 10) None else Some(SumInfo(sum.digits))

    override def equals(to: Any): Boolean = to match {
      case to: SumInfo => digits.filterNot(_==0).equals(to.digits.filterNot(_==0))
      case _ => false
    }
  }
  object SumInfo {
    def stream(si: SumInfo): LazyList[SumInfo] =
      LazyList.cons(si, si.next.fold(LazyList.empty[SumInfo])(stream))
    def list(si: SumInfo): List[SumInfo] = stream(si).toList
  }

  implicit def int2IntList(i: Int): List[Int] = i.digits

  implicit def localDate2IntList(ld: LocalDate): List[Int] =
    ld.getYear.digits ::: ld.getMonthValue.digits ::: ld.getDayOfMonth.digits


  implicit class LocalDate1(ld: LocalDate) {
    val infoList: List[SumInfo] = SumInfo.list(SumInfo(ld))
    val sum1: Int = infoList.last.sum
    val stringSum1: String = infoList.map(_.text).mkString(", ")
  }

  implicit class LocalDate2(ld: LocalDate) {
    def str2(v: Int): String = "%02d".format(v)
    val (y1, y2) = (ld.getYear / 100, ld.getYear - ld.getYear / 100 * 100)
    val sum2: Int = ld.getDayOfMonth + ld.getMonthValue + y1 + y2
    val stringSum2: String = s"${y1.str2}+${y2.str2}+${ld.getMonthValue.str2}+${ld.getDayOfMonth.str2}=$sum2"
  }
}
