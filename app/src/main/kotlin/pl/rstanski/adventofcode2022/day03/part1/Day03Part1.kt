package pl.rstanski.adventofcode2022.day03.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day03.common.PriorityMapper.priorityOf
import pl.rstanski.adventofcode2022.day03.common.Rucksack
import pl.rstanski.adventofcode2022.day03.common.RucksackParser

private const val PUZZLE_FILENAME = "day03.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day03Part1Solution.solve(puzzle)

    println(result)
}

object Day03Part1Solution {

    fun solve(puzzle: Puzzle): Int {
        val rucksacks: List<Rucksack> = puzzle.lines
            .map(RucksackParser::parseAsRucksack)

        return rucksacks
            .map(Rucksack::commonItemsInCompartments)
            .map { it.single() }
            .sumOf(::priorityOf)
    }
}