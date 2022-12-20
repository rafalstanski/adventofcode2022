package pl.rstanski.adventofcode2022.day21.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day21sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 3)

    val solution = solvePart1(load("day21.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    TODO()
}