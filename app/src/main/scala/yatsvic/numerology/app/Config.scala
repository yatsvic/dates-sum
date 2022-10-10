package yatsvic.numerology.app

import java.time.LocalDate
import scala.jdk.CollectionConverters._

sealed trait Config {
  def datesRange: Config.Dates
  def short: Boolean
}
object Config {
  case class Dates(min: LocalDate, max: LocalDate) {
    def datesList: List[LocalDate] = min.datesUntil(max).iterator().asScala.toList
  }
}

case class HistConfig(
  datesRange: Config.Dates,
  histType: HistConfig.Sum,
  short: Boolean,
) extends Config
object HistConfig {
  enum Sum {
    case one, two
  }
  object Sum {
    val stringValues: String = Sum.values.map(e => s"'${e.toString}'").toList.mkString(", ")
  }
}

case class ListConfig(
  datesRange: Config.Dates,
  target: ListConfig.Target,
  short: Boolean,
) extends Config
object ListConfig {
  sealed trait Target
  case class One(SumOne: Int) extends Target
  case class Two(sumTwo: Int) extends Target
  case class Both(sumOne: Int, sumTwo: Int) extends Target
}
