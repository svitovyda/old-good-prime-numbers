package com.svitovyda

import org.scalatest.{FlatSpec, Inspectors, Matchers}

class PrimesListTillLimitSpec extends FlatSpec with Matchers with Inspectors {

  val till = 10000
  val correct: Numbers = PrimesListTillLimit.parallel(till)

  it should "Generate correct list of primes sequentially" in {
    val result = PrimesListTillLimit.sequentialFoldLeft(500)
    result.length shouldBe 95
    result.head shouldBe 2
    result.last shouldBe 499
  }

  it should "both seq and par should be equal for big `till` and 19 threads" in {
    val seq = PrimesListTillLimit.sequentialFoldLeft(till)
    val par = PrimesListTillLimit.parallel(till, 19)
    assert(seq == par)
  }

  it should "par should be correct for one thread" in {
    val par = PrimesListTillLimit.parallel(till, 1)
    assert(correct == par)
  }

  it should "seqIteration should be correct" in {
    val seqIteration = PrimesListTillLimit.sequentialIteration(till)
    assert(seqIteration == correct)
  }

}
