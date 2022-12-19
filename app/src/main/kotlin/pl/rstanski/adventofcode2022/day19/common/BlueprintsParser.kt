package pl.rstanski.adventofcode2022.day19.common

import pl.rstanski.adventofcode2022.common.Puzzle

object BlueprintsParser {
    private val findOnlyNumbers = Regex("(\\d+)")

    fun parseBlueprints(puzzle: Puzzle): List<Blueprint> {
        val blueprintsNumbersPerLine = puzzle.lines.map(::extractNumbers)

        // Blueprint 1: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 3 ore and 11 obsidian.
        return blueprintsNumbersPerLine.map {
            Blueprint(
                index = it[0],
                oreRobotCost = Cost(ore = it[1]),
                clayRobotCost = Cost(ore = it[2]),
                obsidianRobotCost = Cost(ore = it[3], clay = it[4]),
                geodeRobotCosts = Cost(ore = it[5], obsidian = it[6]),
            )
        }
    }

    private fun extractNumbers(line: String): List<Int> =
        findOnlyNumbers.findAll(line)
            .map { it.groupValues[1] }.toList().map { it.toInt() }
}