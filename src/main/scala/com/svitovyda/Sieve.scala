package com.svitovyda

import scala.annotation.tailrec
import com.svitovyda.PrimesListTillLimit.Numbers

import scala.collection.BitSet

object Sieve {

  def sequentialRecursion(till: Int): Numbers = {
    val max = Math.sqrt(till).toInt

    @tailrec
    def rec(numbers: Numbers, primes: Numbers): Numbers = {
      val p = numbers.head
      if (p > max) primes.reverse ++ numbers
      else rec(numbers.tail.filter(n => !(p to(n, p)).contains(n)), p :: primes)
    }

    rec(PrimesListTillLimit.numbersTill(till, 3), List(2))
  }

  def sequentialArray(till: Int): Numbers = {
    val max = Math.sqrt(till).toInt

    val array = PrimesListTillLimit.numbersTill(till, 3).toArray

    @tailrec
    def sieve(next: Int, primes: Numbers): Numbers = {
      if (next > max) primes.reverse ++ array.filter(_ != 0)
      else {
        next / 2 - 1 until(array.length, next) foreach { i => array(i) = 0 }
        sieve(array.find(_ != 0).get, next :: primes)
      }
    }

    sieve(3, List(2))
  }

  def sequentialImBitSet(till: Int): Numbers = {
    val max = Math.sqrt(till).toInt

    @tailrec
    def sieve(next: Int, bitset: BitSet, primes: Numbers): Numbers = {
      if (next > max) primes.reverse ++ (PrimesListTillLimit.numbersTill(till, next) filterNot bitset)
      else {
        val newBitset = bitset ++ (next until(till, next + next))
        sieve(newBitset.find(n => n >= next && !newBitset(n + 2)).get + 2, newBitset - next, next :: primes)
      }
    }

    2 :: sieve(5, BitSet(3 to(till, 6): _*), List(3))
  }

  def sequentialMutBitSet(till: Int): Numbers = {
    val max = Math.sqrt(till).toInt

    val bitset = new scala.collection.mutable.BitSet()

    @tailrec
    def sieve(next: Int, primes: Numbers): Numbers = {
      if (next > max) primes.reverse ++ (PrimesListTillLimit.numbersTill(till, next) filterNot bitset)
      else {
        bitset ++= (next until(till, next + next))
        val p = bitset.find(n => n >= next && !bitset(n + 2)).get + 2
        bitset -= next
        sieve(p, next :: primes)
      }
    }

    2 :: sieve(3, Nil)
  }

  def sequentialBitSet(till: Int): List[Int] = {
    val len = (till - 3) / 2
    val bitset = new scala.collection.mutable.BitSet(len + 1)

    @inline
    def sieveNext(next: Int) = {
      val normalized = next + next + 3 // step
      val min = normalized * normalized - 3
      bitset ++= (min / 2 to(len, normalized))
    }

    (0 to (Math.sqrt(till).toInt - 3) / 2).foreach { a =>
      if(!bitset(a)) sieveNext(a)
    }
    2 :: (0 to len).filterNot {bitset}.map { pi => pi + pi + 3 }.toList
  }

}
