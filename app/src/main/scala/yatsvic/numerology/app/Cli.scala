package yatsvic.numerology.app

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits._
import java.time.LocalDate
import com.monovore.decline._
import java.time.format.DateTimeParseException
import Config.Dates

object Cli {
  def validatedDate(string: String): ValidatedNel[String, LocalDate] = Validated
    .catchOnly[DateTimeParseException](LocalDate.parse(string)).leftMap(e => s"Invalid Date: $string").toValidatedNel

  implicit val datesArg: Argument[Dates] = new Argument[Dates] {
    override def read(string: String): ValidatedNel[String, Dates] =
      string.split(":", 2) match {
        case Array(min, max) => (validatedDate(min), validatedDate(max))
          .mapN(Dates.apply)
          .andThen { dates =>
            if (dates.min.isBefore(dates.max)) dates.validNel
            else s"Invalid Range: ${dates.min} expected to be before ${dates.max}".invalidNel
          }
        case _ => s"Invalid min:max dates pair: $string".invalidNel
      }
    override def defaultMetavar: String = "min:max"
  }

  val datesOpt: Opts[Dates] = Opts.option[Dates](long = "dates", help = "Dates range in format yyyy-MM-dd:yyyy-MM-dd")

  val histSumOpt: Opts[HistConfig.Sum] = Opts
    .option[String](long = "sum", help = s"Sum type one of ${HistConfig.Sum.stringValues}")
    .mapValidated { string =>
      Validated.catchOnly[java.lang.IllegalArgumentException](HistConfig.Sum.valueOf(string))
        .leftMap(e => NonEmptyList.one(s"${e.getMessage}, should be one of ${HistConfig.Sum.stringValues}"))
    }

  val sum1Opt: Opts[Option[Int]] = Opts.option[Int](long = "sum1", help = "Sum 1 Value 1-9")
    .validate("sum1 should be 1-9")(s => s > 0 && s < 10).orNone
  val sum2Opt: Opts[Option[Int]] = Opts.option[Int](long = "sum2", help = "Sum 2 Value Positive")
    .validate("sum2 should be positive")(s => s > 0).orNone
  val listTargetOpt: Opts[ListConfig.Target] = (sum1Opt, sum2Opt).mapN((s1, s2) => (s1, s2)) // identity doesn't work here
    .mapValidated { sums =>
      sums match {
        case (Some(s1), Some(s2)) => ListConfig.Both(s1, s2).validNel
        case (Some(s1), None) => ListConfig.One(s1).validNel
        case (None, Some(s2)) => ListConfig.Two(s2).validNel
        case (None, None) => " At least one of sum1 or sum2 should be specified".invalidNel
      }
    }

  val shortFlag: Opts[Boolean] = Opts.flag(long = "short", help = "Print short format").orFalse

  val histOpts: Opts[HistConfig] = (datesOpt, histSumOpt, shortFlag).mapN(HistConfig.apply)
  val histSubCommand: Opts[HistConfig] = Opts.subcommand(
      name = "hist",
      help = "Print histogram of dates count, 'hist --help' for details"
    )(histOpts)

  val listOpts: Opts[ListConfig] = (datesOpt, listTargetOpt, shortFlag).mapN(ListConfig.apply)
  val listSubCommand: Opts[ListConfig] = Opts.subcommand(
      name = "list",
      help = "Print list of dates, 'list --help' for details"
    )(listOpts)

  val root: Command[Config] =
    Command(name = "", header = "Print histogram or dates")(histSubCommand orElse listSubCommand)
}
