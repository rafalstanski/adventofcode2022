package pl.rstanski.adventofcode2022.day25.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day25sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == "2=-1=0")

    val solution = solvePart1(load("day25.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val fuelRequirements = puzzle.lines.map { parse(it) }
    val sum = fuelRequirements.sum()

    println("in fives:" + toFives(sum))
    return toSnafu(sum)
}

fun parse(line: String): Long {
    // Special Numeral-Analogue Fuel Units, SNAFU
    val normal = line.replace("-", "0").replace("=", "0")
    val minus = line.replace("[1-4]".toRegex(), "0")
        .replace("=", "2")
        .replace("-", "1")
    val pureNumber = normal.toLong(5)
    val minusNumber = minus.toLong(5)

    return pureNumber - minusNumber
}

// 0,  1,  2  3  4
// =,  -,  0, 1, 2
//-2, -1,  0, 1, 2

val snafuShift = listOf(0, 1, 2, -2, -1)
val snafuDigitShift = listOf("0", "1", "2", "=", "-")

fun toSnafu(number: Long): String {
    var total = number
    var result = ""
    while (total > 0) {
        val fiveRadixDigit = (total % 5).toInt()
        val snafuShift = snafuShift[fiveRadixDigit]
        result += snafuDigitShift[fiveRadixDigit]
        total = (total - snafuShift) / 5
    }
    return result.reversed()
}

fun toFives(number: Long): String {
    var total = number
    var result = ""
    while (total > 0) {
        result += (total % 5).toString()
        total /= 5
    }
    return result.reversed()
}