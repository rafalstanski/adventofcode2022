package pl.rstanski.adventofcode2022.day21.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart2(load("day21sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 1623178306L)

    val solution = solvePart2(load("day21.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    TODO()
}