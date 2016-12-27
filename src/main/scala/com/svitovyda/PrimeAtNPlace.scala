package com.svitovyda

import scala.annotation.tailrec

object PrimeAtNPlace {

  def sequentialStream(place: Int): Int = (3 to place).foldLeft[List[Int]](List(3, 2)) { case (primes, _) =>
    Stream.from(primes.head + 2, 2).dropWhile { i =>
      primes.exists(p => i % p == 0)
    }.head :: primes
  }.head

  def sequentialReq(place: Int): Int = {
    @tailrec
    def nextPrime(primes: List[Int], n: Int): Int =
      if(!primes.exists(n % _ == 0)) n
      else nextPrime(primes, n + 2)

    (3 to place).foldLeft[List[Int]](List(3, 2)) { case (primes, _) =>
      nextPrime(primes, primes.head + 2) :: primes
    }.head
  }

  def parallel(place: Int, lines: Int): Int = {

    0
  }

}
