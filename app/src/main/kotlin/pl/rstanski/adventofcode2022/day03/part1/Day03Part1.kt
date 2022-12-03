package pl.rstanski.adventofcode2022.day03.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

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
            .sumOf { PriorityMapper.priorityOf(it) }
    }
}

object RucksackParser {
    fun parseAsRucksack(line: String): Rucksack =
        Rucksack(line.toList())
}

data class Rucksack(val items: List<Char>) {
    init {
        require(items.size.mod(2) == 0)
    }

    val firstCompartment = items.subList(0, items.size / 2)

    val secondCompartment = items.subList(items.size / 2, items.size)

    fun commonItemsInCompartments(): Set<Char> =
        firstCompartment.intersect(secondCompartment.toSet())
}

object PriorityMapper {

    private val smallLetters = 'a'..'z'
    private val bigLetters = 'A'..'Z'

    fun priorityOf(item: Char): Int =
        when {
            smallLetters.contains(item) -> calculatePriorityForSmallLetter(item)
            bigLetters.contains(item) -> calculatePriorityForBigLetter(item)
            else -> throw IllegalArgumentException("Unknown item: $item")
        }

    private fun calculatePriorityForSmallLetter(item: Char): Int =
        item - 'a' + 1

    private fun calculatePriorityForBigLetter(item: Char): Int =
        item - 'A' + 27
}