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

        blueprint.index to mine * blueprint.index
    }

    qualityLevels.forEach { println("qualityLevels: $it") }

    return qualityLevels.sumOf { it.second }
}


class MineSession(private val blueprint: Blueprint) {

    fun mine(): Int {
        return recursiveMine(1, MineralsMined(), null, listOf(Robot(ORE)))
    }

    private fun recursiveMine(minute: Int, mineralsMined: MineralsMined, createRoobot: RobotType?, robots: List<Robot>): Int {
        var mineralsMinedThisMinute = mineralsMined.copy()

        // create another robot
        val robotsAfterCreate = when (createRoobot) {
            null -> robots
            else -> {
                val robotCosts = blueprint.robotCosts(createRoobot)
                mineralsMinedThisMinute.take(robotCosts)
                robots + Robot(createRoobot)
            }
        }

        // mine
        robots.forEach { it.mine(mineralsMinedThisMinute) }

        return when (minute) {
            24 -> mineralsMinedThisMinute.geode
            else -> {
                if (mineralsMined.contains(blueprint.geodeRobotCosts)) {
                    recursiveMine(minute + 1, mineralsMined, GEODE, robotsAfterCreate)
                } else {
                    val max = mutableListOf<Int>()

                    if (mineralsMined.contains(blueprint.obsidianRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMined, OBSIDIAN, robotsAfterCreate)
                    }

                    if (mineralsMined.contains(blueprint.clayRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMined, CLAY, robotsAfterCreate)
                    }

                    if (mineralsMined.contains(blueprint.oreRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMined, ORE, robotsAfterCreate)
                    }

                    max += recursiveMine(minute + 1, mineralsMinedThisMinute, null, robotsAfterCreate)

                    max.maxOf { it }
                }
            }
        }
    }
}


fun mineWithBlueprint(blueprint: Blueprint): Int {
    println("start mining with blueprint: $blueprint")
    val mineSession = MineSession(blueprint)
    return mineSession.mine()
}

enum class RobotType {
    ORE, CLAY, OBSIDIAN, GEODE
}

data class Robot(val type: RobotType) {
    fun mine(mineralsMined: MineralsMined) {
        when (type) {
            ORE -> mineralsMined.ore++
            CLAY -> mineralsMined.clay++
            OBSIDIAN -> mineralsMined.obsidian++
            GEODE -> mineralsMined.geode++
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
) {
    fun robotCosts(createRoobot: RobotType): Cost {
        return when (createRoobot) {
            ORE -> oreRobotCost
            CLAY -> clayRobotCost
            OBSIDIAN -> obsidianRobotCost
            GEODE -> geodeRobotCosts
        }
    }
}

data class MineralsMined(var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geode: Int = 0) {
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

    operator fun minus(cost: Cost): MineralsMined {
        return MineralsMined(ore - cost.ore, clay - cost.clay, obsidian - cost.obsidian, geode)
    }
}

data class Cost(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0)