package com.svitovyda

import org.scalatest.{FlatSpec, Inspectors, Matchers}

class SieveSpec extends FlatSpec with Matchers with Inspectors {

  val till = 1000
  val correct: Numbers = PrimesListTillLimit.parallel(till)

  it should "sequentialArray should work correct" in {
    val check = Sieve.sequentialArray(till)
    assert(check == correct)
  }

  it should "sequentialImBitSet should work correct" in {
    val check = Sieve.sequentialImBitSet(till)
    assert(check == correct)
  }

  it should "sequentialMutBitSet should work correct" in {
    val check = Sieve.sequentialMutBitSet(till)
    assert(check == correct)
  }

  it should "sequentialBitSet should work correct" in {
    val check = Sieve.sequentialBitSet(till)
    assert(check == correct)
  }

  it should "seqPrimitive should be correct" in {
    val seqPrimitive = Sieve.sequentialRecursion(till)
    assert(seqPrimitive == correct)
  }

}
