package pl.rstanski.adventofcode2022.day19.part2

import java.time.Instant
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.common.BlueprintsParser.parseBlueprints
import pl.rstanski.adventofcode2022.day19.common.MineSessionCached

fun main() {
    val solution = solvePart2(load("day19.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val blueprints = parseBlueprints(puzzle)
    blueprints.forEach(::println)

    val qualityLevels = blueprints.take(3).map { blueprint ->
        println("${Instant.now()} start mining with blueprint: $blueprint")
        val mineSession = MineSessionCached(blueprint, 32)
        val minedGeode = mineSession.mine()
        println("${Instant.now()} Mined geode: $minedGeode")

        blueprint.index to minedGeode
    }
    qualityLevels.forEach { println("mined geode (blueprint = ${it.first}) - ${it.second}") }

    return qualityLevels.map {it.second.toLong() }
        .reduce(Long::times)
}