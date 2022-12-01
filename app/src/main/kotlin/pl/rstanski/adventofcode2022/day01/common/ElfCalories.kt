package pl.rstanski.adventofcode2022.day01.common

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.sum

data class ElfCalories(val calories: List<BigInteger>) {

    val sum: BigInteger =
        calories.sum()
}