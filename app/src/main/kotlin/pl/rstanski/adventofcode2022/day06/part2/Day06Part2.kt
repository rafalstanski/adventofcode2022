package pl.rstanski.adventofcode2022.day06.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day06.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day06Part2Solution.solve(puzzle)

    println(result)
}

object Day06Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        TODO()
    }
}