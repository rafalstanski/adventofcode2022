package pl.rstanski.adventofcode2022.day21.part2

import java.math.BigDecimal
import java.math.MathContext
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart2(load("day21sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 1623178306L)

    val solution = solvePart2(load("day21.txt"))
    println("solution: $solution")
}

var humn = BigDecimal.valueOf(0)
var rootLeftRight: Pair<BigDecimal, BigDecimal> = BigDecimal.ONE to BigDecimal.TEN

private fun solvePart2(puzzle: Puzzle): Any {
    val jobs = puzzle.lines.map { JobParser.parseJob(it) }
    val jobsByName = jobs.associateBy { it.monkey }

    var start = BigDecimal.valueOf(0) //        3840000000000=(7720451903493, 7628196411405): 92255492088
    var stop = BigDecimal.valueOf(13880000000000) ////        3880000000000=(7620465371505, 7628196411405): -7731039900
    humn = middleValueBetween(start, stop)

    while (rootLeftRight.first != rootLeftRight.second) {
        jobsByName.getValue("root")
            .run(jobsByName)

        if (rootLeftRight.first > rootLeftRight.second) {
            start = humn
            humn = middleValueBetween(start, stop)
        } else if (rootLeftRight.first < rootLeftRight.second) {
            stop = humn
            humn = middleValueBetween(start, stop)
        }
    }

    return humn
}

private fun middleValueBetween(start: BigDecimal, stop: BigDecimal) =
    start + (stop - start) / BigDecimal.valueOf(2)

object JobParser {

    fun parseJob(line: String): Job {
        val parts = line.split(":")
        val name = parts[0]
        val x = parts[1].trim()

        return if (x[0].isDigit()) {
            Job(name, x.toBigDecimal())
        } else {
            val opParts = x.split(" ")
            Job(name, null, Operation(opParts[0], opParts[2], opParts[1][0]))
        }
    }
}

data class Operation(val left: String, val right: String, val what: Char) {

    fun calculate(leftValue: BigDecimal, rightValue: BigDecimal): BigDecimal {
        return when(what) {
            '+' -> leftValue + rightValue
            '-' -> leftValue - rightValue
            '*' -> leftValue * rightValue
            '/' -> leftValue.divide(rightValue, MathContext.DECIMAL64)
            else -> throw IllegalArgumentException()
        }
    }
}

data class Job(val monkey: String, val value: BigDecimal? = null, val operation: Operation? = null) {

    fun run(jobs: Map<String, Job>): BigDecimal {
        if (monkey == "humn") return humn

        if (value != null) {
            return value
        } else if (operation != null) {
            val left = jobs.getValue(operation.left).run(jobs)
            val right = jobs.getValue(operation.right).run(jobs)

            if (monkey == "root") {
                rootLeftRight = left to right
            }

            return operation.calculate(left, right)
        } else {
            throw IllegalArgumentException("")
        }
    }
}