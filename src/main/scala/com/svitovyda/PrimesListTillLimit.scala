package com.svitovyda

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PrimesListTillLimit {

  val initialPrimes = List(3, 5, 7, 11, 13, 17, 19)
  val startFrom = 23

  private def foldByFilter(numbers: Seq[Int], z: (List[Int], List[Int]), limit: Double): List[Int] = {
    val (primes, _) = numbers.foldLeft[(List[Int], List[Int])](z) { case (z@(primes, filter), i) =>
      if (filter.exists(n => i % n == 0)) z
      else (i :: primes, if (i <= limit) i :: filter else filter)
    }
    primes.reverse
  }

  def sequential(till: Int): List[Int] = 2 :: foldByFilter(
    numbers = startFrom to(till, 2),
    z = (initialPrimes.reverse, initialPrimes),
    limit = Math.sqrt(till)
  )

  def parallel(till: Int, lines: Int = 8): List[Int] = {

    def partialPrimes(numbers: List[Int], initial: List[Int] = List()): List[Int] = {
      val filterLimit = Math.sqrt(numbers.last)

      foldByFilter(
        numbers = numbers,
        z = (Nil, initial.takeWhile(_ <= filterLimit)),
        limit = filterLimit
      )
    }

    def lazyPrimes(numbers: List[Int], initial: List[Int] = List()): List[Int] = {
      val filterLimit = Math.sqrt(numbers.last)
      val filter = initial.takeWhile(_ <= filterLimit)
      if (filter.nonEmpty) {
        numbers.foldLeft[List[Int]](Nil) { case (z, i) =>
          if (filter.exists(n => i % n == 0)) z
          else i :: z
        }.reverse
      }
      else numbers
    }

    def reduce(lines: List[List[Int]], filter: List[Int], primes: List[Int] = Nil): List[Int] = lines match {
      case Nil                      => primes ++ filter
      case List(p)                  => primes ++ filter ++ partialPrimes(p, filter)
      case numbers: List[List[Int]] =>

        val futures = numbers.map { l =>
          Future {lazyPrimes(l, filter)}
        }

        val result = Await.result(Future.sequence(futures), 1.minute)
        reduce(result.tail, result.head, primes ++ filter)
    }

    val step = till / lines / 2 // as we initially take only odd numbers

    val processedOnce = Await.result(
      Future.sequence((23 to(till, 2)).toList.grouped(step).toList.map { l =>
        Future {partialPrimes(l, initialPrimes)}
      }),
      1.minute
    )

    2 :: initialPrimes ++ reduce(processedOnce.tail, processedOnce.head)
  }
}
