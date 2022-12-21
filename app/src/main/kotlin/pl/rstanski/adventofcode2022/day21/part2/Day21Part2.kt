package pl.rstanski.adventofcode2022.day21.part2

import java.math.BigInteger
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart2(load("day21sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 1623178306L)

    val solution = solvePart2(load("day21.txt"))
    println("solution: $solution")
}

var humn = BigInteger.valueOf(301)
var rootLeftRight: Pair<BigInteger, BigInteger> = TWO to ZERO

private fun solvePart2(puzzle: Puzzle): Any {
    val jobs = puzzle.lines.map { JobParser.parseJob(it) }
    val jobsByName = jobs.associateBy { it.monkey }

    var start = BigInteger.valueOf(0) //        3840000000000=(7720451903493, 7628196411405): 92255492088
    var stop = BigInteger.valueOf(3880000000000) ////        3880000000000=(7620465371505, 7628196411405): -7731039900
    humn = middleValueBetween(start, stop)

    while (rootLeftRight.first != rootLeftRight.second) {
        jobsByName.getValue("root")
            .run(jobsByName)

        if (rootLeftRight.first - rootLeftRight.second > ZERO) {
            start = humn
            humn = middleValueBetween(start, stop)
        } else if (rootLeftRight.first - rootLeftRight.second < ZERO) {
            stop = humn
            humn = middleValueBetween(start, stop)
        }
    }

    return humn
}

private fun middleValueBetween(start: BigInteger, stop: BigInteger) =
    start + (stop - start) / TWO

// 3876907167497

object JobParser {

    fun parseJob(line: String): Job {
        val parts = line.split(":")
        val name = parts[0]
        val x = parts[1].trim()

        return if (x[0].isDigit()) {
            Job(name, x.toBigInteger())
        } else {
            val opParts = x.split(" ")
            Job(name, null, Operation(opParts[0], opParts[2], opParts[1][0]))
        }
    }
}

data class Operation(val left: String, val right: String, val what: Char) {

    fun calculate(leftValue: BigInteger, rightValue: BigInteger): BigInteger {
        return when(what) {
            '+' -> leftValue + rightValue
            '-' -> leftValue - rightValue
            '*' -> leftValue * rightValue
            '/' -> leftValue / rightValue
            else -> throw IllegalArgumentException()
        }
    }
}

data class Job(val monkey: String, val value: BigInteger? = null, val operation: Operation? = null) {

    fun run(jobs: Map<String, Job>): BigInteger {
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