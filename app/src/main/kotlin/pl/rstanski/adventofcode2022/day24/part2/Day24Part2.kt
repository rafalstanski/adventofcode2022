package pl.rstanski.adventofcode2022.day24.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day24sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 20)

    val solution = solvePart2(PuzzleLoader.load("day24.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    TODO()
}