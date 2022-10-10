package yatsvic.numerology.sum

object IntOps {

  def split(i: Int): Option[(Int, Int)] = if (i > 0) Some((i / 10, i % 10)) else None

  implicit class Digits(i: Int) {
    import UnfoldOps._
    def digits: List[Int] = i.unfoldLeft(split)
    def str2: String = "%02d".format(i)
  }
}
