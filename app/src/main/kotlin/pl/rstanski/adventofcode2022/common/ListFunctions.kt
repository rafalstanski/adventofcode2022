package pl.rstanski.adventofcode2022.common

import java.math.BigInteger

fun List<String>.toBigIntegers(): List<BigInteger> =
    this.map(String::toBigInteger)

fun List<BigInteger>.sum(): BigInteger =
    this.sumOf { it }
