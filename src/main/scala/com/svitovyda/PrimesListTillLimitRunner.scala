package com.svitovyda

import org.scalameter._

object PrimesListTillLimitRunner {

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
    val lines = 8
    val till = 1000000

    val seqTime = standardConfig measure {
      seqResult = PrimesListTillLimit.sequential(till).length
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqTime ms")

    val parTime = standardConfig measure {
      parResult = PrimesListTillLimit.parallel(till, lines).length
    }

    if(till < 200000) {
      val seqIterTime = standardConfig measure {
        seqIterResult = PrimesListTillLimit.sequentialIteration(till).length
      }
      println(s"sequential iteration result = $seqIterResult")
      println(s"sequential iteration balancing time: $seqIterTime ms")

      println(s"speedup iter: ${seqTime / seqIterTime}")
      println(s"speedup par over iter: ${seqIterTime / parTime}")
    }

    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $parTime ms")
    println(s"speedup: ${seqTime / parTime}")
  }
}
