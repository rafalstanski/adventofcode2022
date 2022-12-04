package pl.rstanski.adventofcode2022.day05.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day05.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day05Part1Solution.solve(puzzle)

    println(result)
}

object Day05Part1Solution {

    fun solve(puzzle: Puzzle): Int {
        TODO()
    }
}