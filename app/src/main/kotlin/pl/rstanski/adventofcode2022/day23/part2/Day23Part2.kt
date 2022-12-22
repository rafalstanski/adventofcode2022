package pl.rstanski.adventofcode2022.day23.part2

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day23sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == BigInteger("152"))

    val solution = solvePart2(PuzzleLoader.load("day23.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    TODO()
}