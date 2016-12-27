package com.svitovyda

import scala.annotation.tailrec

object PrimeAtNPlace {

  val initLength: Long = PrimesListTillLimit.InitialPrimes.length + 2 // as 2 is not included there
  private val initialPrimes = PrimesListTillLimit.InitialPrimes.reverse

  def sequentialStream(place: Long): Long = {
    (initLength to place).foldLeft[List[Long]](initialPrimes) {
      case (primes, _) =>
        Stream.from(PrimesListTillLimit.StartFrom.toInt, 2).dropWhile { i =>
          primes.exists(p => i % p == 0)
        }.head :: primes
    }.head
  }

  def sequentialReq(place: Long): Long = {
    @tailrec
    def nextPrime(primes: List[Long], n: Long): Long =
      if (!primes.exists(n % _ == 0)) n
      else nextPrime(primes, n + 2)

    (initLength to place).foldLeft[List[Long]](initialPrimes) { case (primes, _) =>
      nextPrime(primes, primes.head + 2) :: primes
    }.head
  }

  def parallel(place: Long, lines: Long): Long = {

    0
  }

}
