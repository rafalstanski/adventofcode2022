package pl.rstanski.adventofcode2022.day21.part2

import java.math.BigInteger
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
//    val testSolution = solvePart2(load("day21sample.txt"))
//    println("test solution: $testSolution")
//    check(testSolution == 1623178306L)

    val solution = solvePart2(load("day21.txt"))
    println("solution: $solution")
}

var humn: BigInteger = BigInteger.valueOf(301)
var rootLeftRight: Pair<BigInteger, BigInteger> = TWO to ZERO

private fun solvePart2(puzzle: Puzzle): Any {
    val jobs = puzzle.lines.map { JobParser.parseJob(it) }
    val jobsByName = jobs.associateBy { it.monkey }

    val start = BigInteger.valueOf(3840000000000)
    val stop = BigInteger.valueOf(3880000000000)

    //        3840000000000=(7720451903493, 7628196411405): 92255492088
//        3880000000000=(7620465371505, 7628196411405): -7731039900


    humn = start + (stop - start) / TWO
    var count = 1

    while (rootLeftRight.first != rootLeftRight.second) {
//        count++
//        if (count == 100) {
//            break
//        }

        jobsByName.getValue("root").run(jobsByName)
        if (rootLeftRight.first - rootLeftRight.second > ZERO) {
            humn = humn + (stop - humn) / TWO
        } else if (rootLeftRight.first - rootLeftRight.second < ZERO) {
            humn = start + (humn - start) / TWO
        }
//        println(humn.toString( ) + "=" + rootLeftRight.toString() + ": " + (rootLeftRight.first - rootLeftRight.second))
    }

    return humn
}

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