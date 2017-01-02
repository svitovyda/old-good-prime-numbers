package com.svitovyda

import com.svitovyda.PrimesListTillLimit.Numbers

import scala.annotation.tailrec

object PrimeAtNPlace {

  val initLength: Int = PrimesListTillLimit.InitialPrimes.length + 2 // as 2 is not included there
  private val initialPrimes = PrimesListTillLimit.InitialPrimes.reverse

  def sequentialStream(place: Int): Int = {
    (initLength to place).foldLeft[Numbers](initialPrimes) {
      case (primes, _) =>
        Stream.from(PrimesListTillLimit.StartFrom.toInt, 2).dropWhile { i =>
          primes.exists(p => i % p == 0)
        }.head :: primes
    }.head
  }

  def sequentialReq(place: Int): Int = {
    @tailrec
    def nextPrime(primes: Numbers, n: Int): Int =
      if (!primes.exists(n % _ == 0)) n
      else nextPrime(primes, n + 2)

    (initLength to place).foldLeft[Numbers](initialPrimes) { case (primes, _) =>
      nextPrime(primes, primes.head + 2) :: primes
    }.head
  }

  def sequentialIterations(place: Int): Int = {
    0
  }

  def parallel(place: Int, lines: Int): Int = {

    0
  }

}
