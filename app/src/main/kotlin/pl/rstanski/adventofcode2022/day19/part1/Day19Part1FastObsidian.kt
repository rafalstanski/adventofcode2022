package pl.rstanski.adventofcode2022.day19.part1

import java.time.Instant
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.common.BlueprintsParser.parseBlueprints
import pl.rstanski.adventofcode2022.day19.common.MineSessionFastObsidian

fun main() {
    val testSolution = solvePart1(load("day19sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 33)

    val solution = solvePart1(load("day19.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val blueprints = parseBlueprints(puzzle)
    blueprints.forEach(::println)

    val qualityLevels = blueprints.map { blueprint ->
        println("${Instant.now()} start mining with blueprint: $blueprint")
        val mineSession = MineSessionFastObsidian(blueprint, 24)
        val minedGeode = mineSession.mine()
        println("${Instant.now()} Mined geode: $minedGeode")

        blueprint.index to minedGeode
    }
    qualityLevels.forEach { println("mined geode (blueprint = ${it.first}) - ${it.second}") }

    return qualityLevels.sumOf { it.first * it.second }
}