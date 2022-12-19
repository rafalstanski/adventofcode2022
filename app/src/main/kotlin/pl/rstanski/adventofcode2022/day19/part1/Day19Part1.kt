package pl.rstanski.adventofcode2022.day19.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.part1.RobotType.CLAY
import pl.rstanski.adventofcode2022.day19.part1.RobotType.GEODE
import pl.rstanski.adventofcode2022.day19.part1.RobotType.OBSIDIAN
import pl.rstanski.adventofcode2022.day19.part1.RobotType.ORE

fun main() {
    val testSolution = solvePart1(load("day19sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 33)

    val solution = solvePart1(load("day19.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val blueprintsLines = puzzle.lines.map { parse(it) }

    val blueprints = blueprintsLines.map {
        Blueprint(
            index = it[0],
            oreRobotCost = Cost(ore = it[1]),
            clayRobotCost = Cost(ore = it[2]),
            obsidianRobotCost = Cost(ore = it[3], clay = it[4]),
            geodeRobotCosts = Cost(ore = it[5], obsidian = it[6]),
        )
    }

    blueprints.forEach(::println)

    //you have exactly one ore-collecting robot

    val qualityLevels = blueprints.map { blueprint ->
        val mine = mineWithBlueprint(blueprint)

        blueprint.index to mine.geode * blueprint.index
    }

    qualityLevels.forEach { println("qualityLevels: $it") }

    return qualityLevels.sumOf { it.second }
}

fun mineWithBlueprint(blueprint: Blueprint): Mine {
    println("start mining with blueprint: $blueprint")
    var minute = 1
    var mine = Mine()
    val robots = mutableListOf(Robot(ORE))

    while (minute <= 24) {
        val countOfEachRobotStart = robots.groupBy { it.type }.mapValues { entry -> entry.value.count() }
        println("minute (start): $minute: mine: $mine, robots: $countOfEachRobotStart")


        // try to create another robot
        val createdRobot = mutableListOf<Robot>()
        if (mine.contains(blueprint.geodeRobotCosts)) {
            println(blueprint.geodeRobotCosts)

            createdRobot += Robot(GEODE)
            mine.take(blueprint.geodeRobotCosts)
        } else if (mine.contains(blueprint.obsidianRobotCost)) {
            createdRobot += Robot(OBSIDIAN)
            mine.take(blueprint.obsidianRobotCost)
        } else if (mine.contains(blueprint.clayRobotCost)) {
            createdRobot += Robot(CLAY)
            mine.take(blueprint.clayRobotCost)
        } else if (mine.contains(blueprint.oreRobotCost)) {
            createdRobot += Robot(ORE)
            mine.take(blueprint.oreRobotCost)
        }

        // mine
        robots.forEach { it.mine(mine) }

        // add to list of all robots
        robots += createdRobot

        val countOfCreatedEachRobot = createdRobot.groupBy { it.type }.mapValues { entry -> entry.value.count() }
        val countOfEachRobot = robots.groupBy { it.type }.mapValues { entry -> entry.value.count() }
        println("minute (end): $minute: mine: $mine, created robots: $countOfCreatedEachRobot, robots: $countOfEachRobot")

        minute++
    }

    return mine
}

enum class RobotType {
    ORE, CLAY, OBSIDIAN, GEODE
}

data class Robot(val type: RobotType) {
    fun mine(mine: Mine) {
        when (type) {
            ORE -> mine.ore++
            CLAY -> mine.clay++
            OBSIDIAN -> mine.obsidian++
            GEODE -> mine.geode++
        }
    }

}


// Blueprint 1: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 17 clay. Each geode robot costs 3 ore and 11 obsidian.
private fun parse(line: String): List<Int> {
    val regex = Regex("(\\d+)")
    val matches = regex.findAll(line)
    return matches.map { it.groupValues[1] }.toList().map { it.toInt() }
}

data class Blueprint(
    val index: Int,
    val oreRobotCost: Cost,
    val clayRobotCost: Cost,
    val obsidianRobotCost: Cost,
    val geodeRobotCosts: Cost
)

data class Mine(var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geode: Int = 0) {
    fun contains(cost: Cost): Boolean {
        return ore - cost.ore >= 0
                && clay - cost.clay >= 0
                && obsidian - cost.obsidian >= 0
    }

    fun take(cost: Cost) {
        ore -= cost.ore
        clay -= cost.clay
        obsidian -= cost.obsidian
    }
}

data class Cost(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0)