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
var previousDiff : BigInteger = BigInteger.valueOf(92255492088)
var rootLeftRight: Pair<BigInteger, BigInteger> = TWO to ZERO

private fun solvePart2(puzzle: Puzzle): Any {
    val jobs = puzzle.lines.map { JobParser.parseJob(it) }
    val jobsByName = jobs.associateBy { it.monkey }

    var start = BigInteger.valueOf(3840000000000)
    var stop = BigInteger.valueOf(3880000000000)

    //        3840000000000=(7720451903493, 7628196411405): 92255492088
//        3880000000000=(7620465371505, 7628196411405): -7731039900


    humn = start + (stop - start) / TWO

//    while (rootLeftRight.first != rootLeftRight.second) {
//        jobsByName.getValue("root").run(jobsByName)
//        if (rootLeftRight.first - rootLeftRight.second > ZERO) {
//            start = humn
//            humn = start + (stop - start) / TWO
//        } else if (rootLeftRight.first - rootLeftRight.second < ZERO) {
//            stop = humn
//            humn = start + (stop - start) / TWO
//        }
//        if (previousDiff >= (rootLeftRight.first - rootLeftRight.second).abs()) {
//            println("($start, $stop) $humn: ${rootLeftRight.first - rootLeftRight.second} = (${rootLeftRight.first} ${rootLeftRight.second}")
//            previousDiff = (rootLeftRight.first - rootLeftRight.second).abs()
//        }
//    }

    println(BigInteger.valueOf(3).divide(BigInteger.valueOf(2)))

    humn = BigInteger.valueOf(3876907167497)
//    humn = BigInteger.valueOf(301)
    jobsByName.getValue("root").run(jobsByName)
    println(rootLeftRight)

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