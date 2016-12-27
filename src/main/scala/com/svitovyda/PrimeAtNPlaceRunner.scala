package com.svitovyda

import org.scalameter._

object PrimeAtNPlaceRunner {

  private val standardConfig = config(
    Key.exec.minWarmupRuns -> 5,
    Key.exec.maxWarmupRuns -> 10,
    Key.exec.benchRuns -> 5,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  @volatile var seqStrResult = 0
  @volatile var seqRecResult = 0
  @volatile var parResult = 0

  def main(args: Array[String]): Unit = {
    val place = 10000

    val seqStrTime = standardConfig measure {
      seqStrResult = PrimeAtNPlace.sequentialStream(place)
    }
    println(s"sequential result = $seqStrResult")
    println(s"sequential balancing time: $seqStrTime ms")

    val seqRecTime = standardConfig measure {
      seqRecResult = PrimeAtNPlace.sequentialReq(place)
    }

    println(s"parallel result = $seqRecResult")
    println(s"parallel balancing time: $seqRecTime ms")
    println(s"speedup: ${seqStrTime / seqRecTime}")
  }
}
