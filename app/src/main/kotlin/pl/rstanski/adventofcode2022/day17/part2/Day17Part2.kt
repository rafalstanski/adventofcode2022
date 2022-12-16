package pl.rstanski.adventofcode2022.day17.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day17sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 0)

    val solution = solvePart2(PuzzleLoader.load("day17.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    puzzle.lines.map { parse(it) }

    TODO()
}

private fun parse(line: String) {

}