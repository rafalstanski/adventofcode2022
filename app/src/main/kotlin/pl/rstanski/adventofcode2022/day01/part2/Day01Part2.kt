package pl.rstanski.adventofcode2022.day01.part2

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.sum
import pl.rstanski.adventofcode2022.day01.common.ElfCalories
import pl.rstanski.adventofcode2022.day01.common.ElfCaloriesExtractor

private const val PUZZLE_FILENAME = "day01.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val solution = Day01Part2Solution.solve(puzzle)

    println(solution)
}

object Day01Part2Solution {

    private val elfCaloriesExtractor = ElfCaloriesExtractor()

    fun solve(puzzle: Puzzle): BigInteger {
        val sortedByCaloriesFromBiggestToLowest = elfCaloriesExtractor.extract(puzzle)
            .sortedByDescending(ElfCalories::sum)

        return sortedByCaloriesFromBiggestToLowest
            .take(3)
            .map(ElfCalories::sum)
            .sum()
    }
}