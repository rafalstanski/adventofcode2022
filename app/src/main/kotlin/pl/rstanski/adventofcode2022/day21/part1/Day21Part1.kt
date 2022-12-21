package pl.rstanski.adventofcode2022.day21.part1

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day21sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == BigInteger("152"))

    val solution = solvePart1(load("day21.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val jobs = puzzle.lines.map { JobParser.parseJob(it) }

    jobs.forEach(::println)

    val jobsByName = jobs.associateBy { it.monkey }

    return jobsByName.getValue("root").run(jobsByName)
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
        if (value != null) {
            return value
        } else if (operation != null) {
            val left = jobs.getValue(operation.left).run(jobs)
            val right = jobs.getValue(operation.right).run(jobs)

            return operation.calculate(left, right)
        } else {
            throw IllegalArgumentException("")
        }
    }
}