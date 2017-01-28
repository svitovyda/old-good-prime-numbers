package com

package object svitovyda {
  type Numbers = List[Int]
  type PrimesAndFilter = (Numbers, Numbers)

  val InitialPrimes: Numbers = List(3, 5, 7, 11, 13, 17, 19)
  val StartFrom: Int = 23
}
