package com.svitovyda

import org.scalatest.{FlatSpec, Inspectors, Matchers}

class SieveSpec extends FlatSpec with Matchers with Inspectors {

  it should "sequentialArray should work correct" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequentialFoldLeft(till)
    val it = Sieve.sequentialArray(till)
    assert(seq == it)
  }

  it should "sequentialImBitSet should work correct" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequentialFoldLeft(till)
    val it = Sieve.sequentialImBitSet(till)
    assert(seq == it)
  }

  it should "sequentialMutBitSet should work correct" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequentialFoldLeft(till)
    val it = Sieve.sequentialMutBitSet(till)
    assert(seq == it)
  }

  it should "sequentialBitSet should work correct" in {
    val till = 1000
    val correct = PrimesListTillLimit.parallel(till)
    val sequentialBitSet = Sieve.sequentialBitSet(till)
    assert(correct == sequentialBitSet)
  }

}
