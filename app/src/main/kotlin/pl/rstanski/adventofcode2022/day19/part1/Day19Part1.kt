package pl.rstanski.adventofcode2022.day19.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.common.BlueprintsParser.parseBlueprints
import pl.rstanski.adventofcode2022.day19.common.MineSessionCached
import pl.rstanski.adventofcode2022.day19.common.minedGeodeUsing

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

    val minedGeodeByBlueprint = minedGeodeUsing(blueprints) { blueprint ->
        MineSessionCached(blueprint, 24)
    }

    return minedGeodeByBlueprint.sumOf { it.first * it.second }
}