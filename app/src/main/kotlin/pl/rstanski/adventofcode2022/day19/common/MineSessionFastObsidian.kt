package pl.rstanski.adventofcode2022.day19.common

import pl.rstanski.adventofcode2022.day19.common.RobotType.CLAY
import pl.rstanski.adventofcode2022.day19.common.RobotType.GEODE
import pl.rstanski.adventofcode2022.day19.common.RobotType.OBSIDIAN
import pl.rstanski.adventofcode2022.day19.common.RobotType.ORE

class MineSessionFastObsidian(private val blueprint: Blueprint, private val minutesLimit: Int) {

    fun mine(): Int {
        return recursiveMine(1, MineralsMined(), null, listOf(ORE))
    }

    private fun recursiveMine(
        minute: Int,
        mineralsMined: MineralsMined,
        createRoobot: RobotType?,
        robots: List<RobotType>
    ): Int {
        val mineralsMinedThisMinute = mineralsMined.copy()

        // create another robot
        val robotsAfterCreate = when (createRoobot) {
            null -> robots
            else -> {
                val robotCosts = blueprint.robotCost(createRoobot)
                mineralsMinedThisMinute.take(robotCosts)
                robots + createRoobot
            }
        }

        // mine
        robots.forEach { mineWithRobot(it, mineralsMinedThisMinute) }

        return when (minute) {
            minutesLimit -> mineralsMinedThisMinute.geode
            else -> {
                if (mineralsMinedThisMinute.contains(blueprint.geodeRobotCosts)) {
                    recursiveMine(minute + 1, mineralsMinedThisMinute, GEODE, robotsAfterCreate)
                } else if (robotsAfterCreate.count { it == OBSIDIAN } < blueprint.maxObsidianCost && mineralsMinedThisMinute.contains(blueprint.obsidianRobotCost)) {
                    recursiveMine(minute + 1, mineralsMinedThisMinute, OBSIDIAN, robotsAfterCreate)
                } else {
                    val max = mutableListOf<Int>()

                    if (robotsAfterCreate.count { it == CLAY } < blueprint.maxClayCost && mineralsMinedThisMinute.contains(blueprint.clayRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, CLAY, robotsAfterCreate)
                    }

                    if (robotsAfterCreate.count { it == ORE } < blueprint.maxOreCost && mineralsMinedThisMinute.contains(blueprint.oreRobotCost)) {
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