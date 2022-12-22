package pl.rstanski.adventofcode2022.day22.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart2(load("day22sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 0)

    val solution = solvePart2(load("day22.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    TODO()
}