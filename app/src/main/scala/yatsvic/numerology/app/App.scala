package yatsvic.numerology.app

import com.monovore.decline.{Command, CommandApp, Opts}
import yatsvic.numerology.app.HistConfig.Sum

import java.time.LocalDate
import scala.jdk.CollectionConverters._

object App {
  def main(args: Array[String]): Unit = {
    Cli.root.parse(args.toSeq) match {
      case Left(help) =>
        System.err.println(help)
      case Right(config) =>
        val report: List[String] = config match {
          case histConfig: HistConfig => ReportHist.run(histConfig)
          case listConfig: ListConfig => ReportList.run(listConfig)
        }
        System.out.println(report.mkString(System.lineSeparator()))
    }
  }
}
