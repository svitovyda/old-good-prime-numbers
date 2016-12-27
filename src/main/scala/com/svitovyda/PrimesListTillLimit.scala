package com.svitovyda

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PrimesListTillLimit {

  val InitialPrimes: List[Long] = List(3, 5, 7, 11, 13, 17, 19)
  val StartFrom: Long = 23

  private def foldByFilter(numbers: Seq[Long], z: (List[Long], List[Long]), limit: Double): List[Long] = {
    val (primes, _) = numbers.foldLeft[(List[Long], List[Long])](z) { case (z@(primes, filter), i) =>
      if (filter.exists(n => i % n == 0)) z
      else (i :: primes, if (i <= limit) i :: filter else filter)
    }
    primes.reverse
  }

  def sequential(till: Long): List[Long] = 2 :: foldByFilter(
    numbers = StartFrom to(till, 2),
    z = (InitialPrimes.reverse, InitialPrimes),
    limit = Math.sqrt(till)
  )

  case class Iteration(tail: List[Long], filter: List[Long], primes: List[Long]) {
    def iterate: Iteration = {

      def lazyPrimes(numbers: List[Long], filter: List[Long] = List()): List[Long] = {
        numbers.foldLeft[List[Long]](Nil) { case (z, i) =>
          if (filter.exists(n => i % n == 0)) z
          else i :: z
        }.reverse
      }

      val max: Long = filter.last * filter.last
      val (f, t) = lazyPrimes(tail, filter).span(_ <= max)
      Iteration(t, f, primes ++ filter)
    }
  }

  def sequentialIteration(till: Long): List[Long] = {
    def rec(i: Iteration): List[Long] = i match {
      case Iteration(Nil, filter, primes) => primes ++ filter
      case _ => rec(i.iterate)
    }

    2 :: rec(Iteration((StartFrom to (till, 2)).toList, InitialPrimes, Nil))
  }

  def parallel(till: Long, lines: Long = 8): List[Long] = {

    def partialPrimes(numbers: List[Long], filter: List[Long] = List()): List[Long] = {
      val filterLimit = Math.sqrt(numbers.last)

      foldByFilter(
        numbers = numbers,
        z = (Nil, filter.takeWhile(_ <= filterLimit)),
        limit = filterLimit
      )
    }

    def lazyPrimes(numbers: List[Long], initial: List[Long] = List()): List[Long] = {
      val filterLimit = Math.sqrt(numbers.last)
      val filter = initial.takeWhile(_ <= filterLimit)
      if (filter.nonEmpty) {
        numbers.foldLeft[List[Long]](Nil) { case (z, i) =>
          if (filter.exists(n => i % n == 0)) z
          else i :: z
        }.reverse
      }
      else numbers
    }

    def reduce(lines: List[List[Long]], filter: List[Long], primes: List[Long] = Nil): List[Long] = lines match {
      case Nil                      => primes ++ filter
      case List(p)                  => primes ++ filter ++ partialPrimes(p, filter)
      case numbers: List[List[Long]] =>

        val futures = numbers.map { l =>
          Future {lazyPrimes(l, filter)}
        }

        val result = Await.result(Future.sequence(futures), 1.minute)
        reduce(result.tail, result.head, primes ++ filter)
    }

    val step = till / lines / 2 // as we initially take only odd numbers

    val processedOnce = Await.result(
      Future.sequence((StartFrom to(till, 2)).toList.grouped(step.toInt).toList.map { l =>
        Future {partialPrimes(l, InitialPrimes)}
      }),
      1.minute
    )

    2 :: InitialPrimes ++ reduce(processedOnce.tail, processedOnce.head)
  }
}
