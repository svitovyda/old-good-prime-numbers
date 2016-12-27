package com.svitovyda

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object PrimesListTillLimit {

  def sequential(till: Int): List[Int] =
    2 :: (3 to(till, 2)).foldLeft[List[Int]](List[Int]()) { case (z, i) =>
      if (z.exists(n => i % n == 0)) z
      else i :: z
    }.reverse

  def parallel(till: Int, lines: Int = 16): List[Int] = {
    val step = till / lines / 2

    def partialPrimes(numbers: List[Int], initial: List[Int] = List()): List[Int] =
      numbers.foldLeft[List[Int]](Nil) { case (z, i) =>
        if (initial.exists(n => i % n == 0) || z.exists(n => i % n == 0)) z
        else i :: z
      }.reverse

    def lazyPrimes(numbers: List[Int], initial: List[Int] = List()): List[Int] =
      numbers.foldLeft[List[Int]](Nil) { case (z, i) =>
        if (initial.exists(n => i % n == 0)) z
        else i :: z
      }.reverse

    def reduce(lines: List[List[Int]], init: List[Int], primes: List[Int] = Nil): List[Int] = lines match {
      case Nil                      => primes ++ init
      case List(p)                  => primes ++ init ++ partialPrimes(p, init)
      case numbers: List[List[Int]] =>

        val futures = numbers.map { l =>
          Future {lazyPrimes(l, init)}
        }

        val result = Await.result(Future.sequence(futures), 1.minute)
        reduce(result.tail, result.head, primes ++ init)
    }

    val init = List(3, 5, 7, 11, 13, 17, 19)

    val processedOnce = Await.result(
      Future.sequence((23 to(till, 2)).toList.grouped(step / 2).toList.map { l =>
        Future { partialPrimes(l, init) }
      }),
      1.minute)

    2 :: init ++ reduce(processedOnce.tail, processedOnce.head)
  }
}
