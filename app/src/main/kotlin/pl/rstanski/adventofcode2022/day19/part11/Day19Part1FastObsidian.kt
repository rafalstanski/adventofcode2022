package pl.rstanski.adventofcode2022.day19.part11

import java.time.Instant
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day19.part11.RobotType.CLAY
import pl.rstanski.adventofcode2022.day19.part11.RobotType.GEODE
import pl.rstanski.adventofcode2022.day19.part11.RobotType.OBSIDIAN
import pl.rstanski.adventofcode2022.day19.part11.RobotType.ORE

fun main() {
//    val testSolution = solvePart1(load("day19sample.txt"))
//    println("test solution: $testSolution")
//    check(testSolution == 33)

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

    val qualityLevels = blueprints.filter { it.index == 23 || it.index == 30 }.map { blueprint ->
        val mine = mineWithBlueprint(blueprint)

        blueprint.index to mine * blueprint.index
    }

    qualityLevels.forEach { println("qualityLevels: $it") }

    return qualityLevels.sumOf { it.second }
}


class MineSession(private val blueprint: Blueprint) {

    fun mine(): Int {
        return recursiveMine(1, MineralsMined(), null, listOf(ORE))
    }


    private fun recursiveMine(minute: Int, mineralsMined: MineralsMined, createRoobot: RobotType?, robots: List<RobotType>): Int {
        var mineralsMinedThisMinute = mineralsMined.copy()

        // create another robot
        val robotsAfterCreate = when (createRoobot) {
            null -> robots
            else -> {
                val robotCosts = blueprint.robotCosts(createRoobot)
                mineralsMinedThisMinute.take(robotCosts)
                robots + createRoobot
            }
        }

        // mine
        robots.forEach { mineWithRobot(it, mineralsMinedThisMinute) }

        return when (minute) {
            24 -> mineralsMinedThisMinute.geode
            else -> {
                if (mineralsMinedThisMinute.contains(blueprint.geodeRobotCosts)) {
                    recursiveMine(minute + 1, mineralsMinedThisMinute, GEODE, robotsAfterCreate)
                } else if (robots.count {it == OBSIDIAN} < blueprint.maxObsidianCost && mineralsMinedThisMinute.contains(blueprint.obsidianRobotCost)) {
                    recursiveMine(minute + 1, mineralsMinedThisMinute, OBSIDIAN, robotsAfterCreate)
                } else {
                    val max = mutableListOf<Int>()

                    if (robots.count {it == CLAY} < blueprint.maxClayCost && mineralsMinedThisMinute.contains(blueprint.clayRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, CLAY, robotsAfterCreate)
                    }

                    if (robots.count {it == ORE} < blueprint.maxOreCost && mineralsMinedThisMinute.contains(blueprint.oreRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, ORE, robotsAfterCreate)
                    }

                    max += recursiveMine(minute + 1, mineralsMinedThisMinute, null, robotsAfterCreate)

                    max.maxOf { it }
                }
            }
        }
    }

    private fun mineWithRobot(robotType: RobotType, mineralsMined: MineralsMined) {
        when (robotType) {
            ORE -> mineralsMined.ore++
            CLAY -> mineralsMined.clay++
            OBSIDIAN -> mineralsMined.obsidian++
            GEODE -> mineralsMined.geode++
        }
    }
}


fun mineWithBlueprint(blueprint: Blueprint): Int {
    println("${Instant.now()} start mining with blueprint: $blueprint" )
    val mineSession = MineSession(blueprint)
    val minedGeode = mineSession.mine()
    println("${Instant.now()} Mined geode: $minedGeode")
    return minedGeode

}

enum class RobotType {
    ORE, CLAY, OBSIDIAN, GEODE
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
    private val allCosts = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCosts)
    val maxOreCost = allCosts.maxOf { it.ore }
    val maxClayCost = allCosts.maxOf { it.clay }
    val maxObsidianCost = allCosts.maxOf { it.obsidian }

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

        require(ore >= 0)
        require(clay >= 0)
        require(obsidian >= 0)
    }

//    operator fun minus(cost: Cost): MineralsMined {
//        return MineralsMined(ore - cost.ore, clay - cost.clay, obsidian - cost.obsidian, geode)
//    }
}

data class Cost(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0)