package pl.rstanski.adventofcode2022.day11.common

import java.math.BigInteger

sealed interface Operation {

    fun calculate(item: Item): BigInteger
}

data class Add(val value: BigInteger) : Operation {
    override fun calculate(item: Item): BigInteger {
        return when (value) {
            BigInteger("-1") -> item.worryLevel + item.worryLevel
            else -> item.worryLevel + value
        }
    }
}

data class Multiply(val value: BigInteger) : Operation {
    override fun calculate(item: Item): BigInteger {
        return when (value) {
            BigInteger("-1") -> item.worryLevel * item.worryLevel
            else -> item.worryLevel * value
        }
    }
}