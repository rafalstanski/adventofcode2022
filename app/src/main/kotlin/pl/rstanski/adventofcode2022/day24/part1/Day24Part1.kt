package pl.rstanski.adventofcode2022.day24.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day24sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 110)

    val solution = solvePart1(load("day24.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    TODO()
}