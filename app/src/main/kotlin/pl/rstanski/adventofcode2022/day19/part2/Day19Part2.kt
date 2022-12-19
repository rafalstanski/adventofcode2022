package pl.rstanski.adventofcode2022.day19.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.common.BlueprintsParser.parseBlueprints
import pl.rstanski.adventofcode2022.day19.common.MineSessionFastObsidian
import pl.rstanski.adventofcode2022.day19.common.minedGeodeUsing

fun main() {
    val solution = solvePart2(load("day19.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val blueprints = parseBlueprints(puzzle)
    blueprints.forEach(::println)

    val minedGeodeByBlueprint = minedGeodeUsing(blueprints.take(3)) { blueprint ->
        MineSessionFastObsidian(blueprint, 32)
    }

    return minedGeodeByBlueprint.map {it.second.toLong() }
        .reduce(Long::times)
}