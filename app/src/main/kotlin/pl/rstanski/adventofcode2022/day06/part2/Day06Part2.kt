package pl.rstanski.adventofcode2022.day06.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day06.common.StartMarkerFinder

private const val PUZZLE_FILENAME = "day06.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day06Part2Solution.solve(puzzle)

    println(result)
}

object Day06Part2Solution {

    private val finder = StartMarkerFinder(14)

    fun solve(puzzle: Puzzle): Any {
        val signal: String = puzzle.singleLine

        return finder.findMarker(signal)
    }
}