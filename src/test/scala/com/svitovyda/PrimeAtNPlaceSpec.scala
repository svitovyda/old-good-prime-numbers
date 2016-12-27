package com.svitovyda

import org.scalatest.{FlatSpec, Matchers}

class PrimeAtNPlaceSpec extends FlatSpec with Matchers {

  it should "return correct seq stream prime at 500 place" in {
    PrimeAtNPlace.sequentialStream(500) shouldEqual 3571
  }

  it should "return correct seq req prime at 500 place" in {
    PrimeAtNPlace.sequentialReq(500) shouldEqual 3571
  }
}
