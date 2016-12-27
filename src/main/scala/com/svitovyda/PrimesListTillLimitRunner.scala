package com.svitovyda

import org.scalameter._

object PrimesListTillLimitRunner {

  private val standardConfig = config(
    Key.exec.minWarmupRuns -> 5,
    Key.exec.maxWarmupRuns -> 10,
    Key.exec.benchRuns -> 5,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  @volatile var seqResult = 0
  @volatile var parResult = 0

  def main(args: Array[String]): Unit = {
    val lines = 4
    val till = 300000

    val seqTime = standardConfig measure {
      seqResult = PrimesListTillLimit.sequential(till).length
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqTime ms")

    val parTime = standardConfig measure {
      parResult = PrimesListTillLimit.parallel(till, lines).length
    }

    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $parTime ms")
    println(s"speedup: ${seqTime / parTime}")
  }
}
