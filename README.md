# Old good prime numbers
## Few experiments inspired by Coursera "Parallel programming" course

Just wanted to try some ideas of optimyzing prime numbers algorithms, didn't intended to implement the quickest one.

`PrimesListTillLimit` - get all primes till the limit.
Speedup comparing to sequential is ~3 times for `300000`, ~6 for `3 mln`. The bigger limit is - the bigger difference.
Passing initial primes `(3, 5, 7, 11, 13, 17, 19)` affects performance significantly.

`PrimeAtNPlaceRunner` - get the N-th prime number.
* Sequential implementations - just played there with two implementations as was curious how much longer it will take by using Sequence. The speedup using recursion - 2 times.
* Parallel implementation _tba_

_(tba)_ Parallel Prime Sieve
