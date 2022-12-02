package pl.rstanski.adventofcode2022.day01.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day01.common.ElfCalories
import pl.rstanski.adventofcode2022.day01.common.ElfCaloriesExtractor

private const val PUZZLE_FILENAME = "day01.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val elfCaloriesExtractor = ElfCaloriesExtractor(puzzle)
    val max = elfCaloriesExtractor.extract()
        .maxByOrNull(ElfCalories::sum)

    println(max?.sum)
}