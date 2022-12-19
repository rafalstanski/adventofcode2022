package pl.rstanski.adventofcode2022.day19.common

import pl.rstanski.adventofcode2022.day19.common.RobotType.CLAY
import pl.rstanski.adventofcode2022.day19.common.RobotType.GEODE
import pl.rstanski.adventofcode2022.day19.common.RobotType.OBSIDIAN
import pl.rstanski.adventofcode2022.day19.common.RobotType.ORE


data class State(
    val minute: Int,
    val mineralsMined: MineralsMined,
    val robots: Robots
)

data class Robots(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geode: Int = 0
) {
    fun add(robotType: RobotType): Robots {
        when (robotType) {
            ORE -> ore++
            CLAY -> clay++
            OBSIDIAN -> obsidian++
            GEODE -> geode++
        }
        return this
    }

    fun mine(mineralsMined: MineralsMined) {
        mineralsMined.ore += ore
        mineralsMined.clay += clay
        mineralsMined.obsidian += obsidian
        mineralsMined.geode += geode
    }
}

class MineSessionCached(private val blueprint: Blueprint, private val minutesLimit: Int) {

    private var visitedStates = mutableMapOf<State, Int>()

    fun mine(): Int {
        val robots = Robots(ore = 1)
        return recursiveMine(1, MineralsMined(), null, robots)
    }

    private fun recursiveMine(
        minute: Int,
        mineralsMined: MineralsMined,
        createRoobot: RobotType?,
        robots: Robots,
    ): Int {
        val mineralsMinedThisMinute = mineralsMined.copy()

        // create another robot
        val robotsAfterCreate = when (createRoobot) {
            null -> robots
            else -> {
                val robotCosts = blueprint.robotCost(createRoobot)
                mineralsMinedThisMinute.take(robotCosts)
                robots.copy().add(createRoobot)
            }
        }

        // mine
        robots.mine(mineralsMinedThisMinute)

        val state = State(minute, mineralsMinedThisMinute.copy(), robotsAfterCreate.copy())
        if (state in visitedStates) {
            return visitedStates[state]!!
        }

        val geode = when (minute) {
            minutesLimit -> mineralsMinedThisMinute.geode
            else -> {
                if (canBuildRobot(GEODE, mineralsMinedThisMinute)) {
                    recursiveMine(minute + 1, mineralsMinedThisMinute, GEODE, robotsAfterCreate)
                } else {
                    val max = mutableListOf<Int>()

                    if (robotsAfterCreate.obsidian < blueprint.maxObsidianCost && canBuildRobot(OBSIDIAN, mineralsMinedThisMinute)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, OBSIDIAN, robotsAfterCreate)
                    }

                    if (robotsAfterCreate.clay < blueprint.maxClayCost && canBuildRobot(CLAY, mineralsMinedThisMinute)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, CLAY, robotsAfterCreate)
                    }

                    if (robotsAfterCreate.ore < blueprint.maxOreCost && canBuildRobot(ORE, mineralsMinedThisMinute)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, ORE, robotsAfterCreate)
                    }

                    max += recursiveMine(minute + 1, mineralsMinedThisMinute, null, robotsAfterCreate)

                    max.maxOf { it }
                }
            }
        }

        visitedStates[state] = geode

        return geode
    }

    private fun canBuildRobot(robotType: RobotType, mineralsMined: MineralsMined): Boolean =
        mineralsMined.contains(blueprint.robotCost(robotType))
}