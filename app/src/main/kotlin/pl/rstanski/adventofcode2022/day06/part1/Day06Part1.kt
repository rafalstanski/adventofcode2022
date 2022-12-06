package pl.rstanski.adventofcode2022.day06.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day06.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day06Part1Solution.solve(puzzle)

    println(result)
}

object Day06Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val subroutine: String = puzzle.lines.first()
        val characters: List<String> = subroutine.map { it.toString() }

        return Searcher.finder(characters) + 1
    }
}

object Searcher {
    fun finder(characters: List<String>): Int {
        characters.withIndex().windowed(4, 1).map { indexedValues: List<IndexedValue<String>> ->
            if (indexedValues.map { it.value }.toSet().size == 4) {
                return indexedValues.last().index
            }
        }
        throw IllegalArgumentException()
    }
}