package pl.rstanski.adventofcode2022.day17.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    val testSolution = solvePart1(load("day17sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 0)

    val solution = solvePart1(load("day17.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    puzzle.lines.map { parse(it) }

    TODO()
}

private fun parse(line: String) {

}
