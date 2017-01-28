package com.svitovyda

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PrimesListTillLimit {

  def numbersTill(end: Int, start: Int = StartFrom): Numbers = (start to(end, 2)).toList

  @inline
  private def foldByFilter(numbers: Seq[Int], z: PrimesAndFilter, limit: Double): Numbers = {
    val (primes, _) = numbers.foldLeft[PrimesAndFilter](z) { case (z@(primes, filter), i) =>
      if (filter.exists(n => i % n == 0)) z
      else (i :: primes, if (i <= limit) i :: filter else filter)
    }
    primes.reverse
  }

  def sequentialFoldLeft(till: Int): Numbers = 2 :: foldByFilter(
    numbers = numbersTill(till),
    z = (InitialPrimes.reverse, InitialPrimes),
    limit = Math.sqrt(till)
  )

  case class Iteration(tail: Numbers, filter: Numbers, primes: Numbers) {
    def iterate: Iteration = {

      @inline
      def lazyPrimes(numbers: Numbers, filter: Numbers = Nil): Numbers = {
        numbers.foldLeft[Numbers](Nil) { case (z, i) =>
          if (filter.exists(n => i % n == 0)) z
          else i :: z
        }.reverse
      }

      val max: Int = filter.last * filter.last
      val (f, t) = lazyPrimes(tail, filter).span(_ <= max)
      Iteration(t, f, primes ++ filter)
    }
  }

  def sequentialIteration(till: Int): Numbers = {
    @tailrec
    def rec(i: Iteration): Numbers = i match {
      case Iteration(Nil, filter, primes) => primes ++ filter
      case _                              => rec(i.iterate)
    }

    2 :: rec(Iteration(numbersTill(till), InitialPrimes, Nil))
  }

  @inline
  private def foldByFilterPar(numbers: Seq[Int], z: PrimesAndFilter, limit: Double, threads: Int): Numbers = {
    val (primes, _) = numbers.foldLeft[PrimesAndFilter](z) { case (z@(primes, filter), i) =>
      if (filter.exists(n => i % n == 0)) z
      else (i :: primes, if (i <= limit) i :: filter else filter)
    }
    primes.reverse
  }

  def parallel(till: Int, threads: Int = 6): Numbers = {

    def partialPrimes(numbers: Numbers, filter: Numbers = List()): Numbers = {
      val filterLimit = Math.sqrt(numbers.last)

      foldByFilter(
        numbers = numbers,
        z = (Nil, filter.takeWhile(_ <= filterLimit)),
        limit = filterLimit
      )
    }

    def lazyPrimes(numbers: Numbers, initial: Numbers = List()): Numbers = {
      val filterLimit = Math.sqrt(numbers.last)
      val filter = initial.takeWhile(_ <= filterLimit)
      if (filter.nonEmpty) {
        numbers.foldLeft[Numbers](Nil) { case (z, i) =>
          if (filter.exists(n => i % n == 0)) z
          else i :: z
        }.reverse
      }
      else numbers
    }

    @tailrec
    def reduce(lines: List[Numbers], filter: Numbers, primes: Numbers = Nil): Numbers = lines match {
      case Nil                    => primes ++ filter
      case List(p)                => primes ++ filter ++ lazyPrimes(p, filter)
      case numbers: List[Numbers] =>

        val futures = numbers.map { l =>
          Future {lazyPrimes(l, filter)}
        }

        val result = Await.result(Future.sequence(futures), 1.minute)
        reduce(result.tail, result.head, primes ++ filter)
    }

    val step = till / threads / 2 // as we initially take only odd numbers

    // first step is filtering by initial primes + by itself. on next steps we need only lazyPrimes filtering to not repeat same calculations
    val processedOnce = Await.result(
      Future.sequence(numbersTill(till).grouped(step.toInt).toList.map { l =>
        Future {partialPrimes(l, InitialPrimes)}
      }),
      1.minute
    )

    2 :: InitialPrimes ++ reduce(processedOnce.tail, processedOnce.head)
  }
}
