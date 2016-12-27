package com.svitovyda

import org.scalatest.{FlatSpec, Inspectors, Matchers}

class PrimesListTillLimitSpec extends FlatSpec with Matchers with Inspectors {

  it should "Generate correct list of primes sequentially" in {
    val result = PrimesListTillLimit.sequential(500)
    result.length shouldBe 95
    result.head shouldBe 2
    result.last shouldBe 499
  }

  it should "both seq and par should be equal for big `till`" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequential(till)
    val par = PrimesListTillLimit.parallel(till, 19)
    assert(seq == par)
  }

  it should "both seq and par should be equal for one line" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequential(till)
    val par = PrimesListTillLimit.parallel(till, 1)
    assert(seq == par)
  }

  it should "both seq and seqIteration should be equal for one line" in {
    val till = 1000
    val seq = PrimesListTillLimit.sequential(till)
    val it = PrimesListTillLimit.sequentialIteration(till)
    assert(seq == it)
  }
}
