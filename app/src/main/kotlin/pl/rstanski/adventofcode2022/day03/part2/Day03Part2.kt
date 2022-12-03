package pl.rstanski.adventofcode2022.day03.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day03.common.PriorityMapper.priorityOf
import pl.rstanski.adventofcode2022.day03.common.Rucksack
import pl.rstanski.adventofcode2022.day03.common.RucksackParser
import pl.rstanski.adventofcode2022.day03.part2.Grouper.groupRucksacks

private const val PUZZLE_FILENAME = "day03.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day03Part2Solution.solve(puzzle)

    println(result)
}

object Day03Part2Solution {

    fun solve(puzzle: Puzzle): Int {
        val groups: List<Group> = puzzle.lines
            .map(RucksackParser::parseAsRucksack)
            .let(::groupRucksacks)

        return groups
            .map(Group::commonItem)
            .map { it.single() }
            .sumOf(::priorityOf)
    }
}

object Grouper {

    fun groupRucksacks(rucksacks: List<Rucksack>): List<Group> {
        require(rucksacks.size.mod(3) == 0)

        return rucksacks.windowed(3, 3)
            .map(::Group)
    }
}

data class Group(val rucksacks: List<Rucksack>) {

    fun commonItem(): Set<Char> =
        rucksacks
            .map(Rucksack::items)
            .map { it.toSet() }
            .reduce { firstItems, secondItems -> firstItems intersect secondItems }
}