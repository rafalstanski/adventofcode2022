package pl.rstanski.adventofcode2022.day01.part1

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day01.common.ElfCalories
import pl.rstanski.adventofcode2022.day01.common.ElfCaloriesExtractor

private const val PUZZLE_FILENAME = "day01.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val solution = Day01Part1Solution.solve(puzzle)

    println(solution)
}

object Day01Part1Solution {

    private val elfCaloriesExtractor = ElfCaloriesExtractor()

    fun solve(puzzle: Puzzle): BigInteger {
        val max: ElfCalories? = elfCaloriesExtractor.extract(puzzle)
            .maxByOrNull(ElfCalories::sum)

        return requireNotNull(max).sum
    }
}