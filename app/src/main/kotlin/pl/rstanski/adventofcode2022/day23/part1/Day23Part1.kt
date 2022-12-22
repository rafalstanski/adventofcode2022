package pl.rstanski.adventofcode2022.day23.part1

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day23sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == BigInteger("152"))

    val solution = solvePart1(load("day23.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    TODO()
}