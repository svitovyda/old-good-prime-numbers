package com.svitovyda

import org.scalameter._

object PrimeAtNPlaceRunner {

  private val standardConfig = config(
    Key.exec.minWarmupRuns -> 2,
    Key.exec.maxWarmupRuns -> 4,
    Key.exec.benchRuns -> 3,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  @volatile var seqStrResult: Int = 0
  @volatile var seqRecResult: Int = 0
  @volatile var parResult: Int = 0

  def main(args: Array[String]): Unit = {
    val place: Int = 20000

    val seqRecTime = standardConfig measure {
      seqRecResult = PrimeAtNPlace.sequentialReq(place)
    }

    println(s"parallel result = $seqRecResult")
    println(s"parallel balancing time: $seqRecTime ms")

    val seqStrTime = standardConfig measure {
      seqStrResult = PrimeAtNPlace.sequentialStream(place / 10)
    }
    println(s"sequential result = $seqStrResult")
    println(s"sequential balancing time: $seqStrTime ms")

    println(s"speedup: ${seqStrTime / seqRecTime}")
  }
}
