package yatsvic.numerology.sum

import scala.annotation.tailrec

object UnfoldOps {
  implicit class Unfold[A, B](i: A) {

    type ConcatFunction = (B, List[B]) => List[B]
    type UnfoldFunction = A => Option[(A, B)]

    def unfold(concatFunction: ConcatFunction)(unfoldFunction: UnfoldFunction): List[B] = {
      @tailrec
      def unfoldHelper(arg: A)(accumList: List[B]): List[B] = {
        unfoldFunction(arg) match {
          case Some((a, b)) => unfoldHelper(a)(concatFunction(b, accumList))
          case None => accumList
        }
      }
      unfoldHelper(i)(List.empty)
    }

    def unfoldRight: UnfoldFunction => List[B] = unfold((last: B, accumList: List[B]) => accumList:+last)
    def unfoldLeft: UnfoldFunction => List[B] = unfold((head: B, accumList: List[B]) => head::accumList)
  }
}
