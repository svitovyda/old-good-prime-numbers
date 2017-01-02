package com.svitovyda

import org.scalameter._

object SieveRunner {

  private val standardConfig = config(
    Key.exec.minWarmupRuns -> 10,
    Key.exec.maxWarmupRuns -> 20,
    Key.exec.benchRuns -> 20,
    Key.verbose -> true
  ) withWarmer new Warmer.Default

  @volatile var seqResult = 0
  @volatile var seqIterResult = 0
  @volatile var parResult = 0

  def main(args: Array[String]): Unit = {
    val till = 10000000

    val seqTime = standardConfig measure {
      seqResult = Sieve.sequentialBitSet(till).length
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqTime ms")

    val parTime = standardConfig measure {
      parResult = PrimesListTillLimit.parallel(till).length
    }

    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $parTime ms")
    println(s"speedup: ${seqTime / parTime}")

  }
}
