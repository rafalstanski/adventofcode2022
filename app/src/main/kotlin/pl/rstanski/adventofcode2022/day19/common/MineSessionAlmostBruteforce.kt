package pl.rstanski.adventofcode2022.day19.common

class MineSessionAlmostBruteforce(private val blueprint: Blueprint, private val minutesLimit: Int) : MineSession {

    override fun mine(): Int {
        return recursiveMine(1, MineralsMined(), null, listOf(RobotType.ORE))
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
                    recursiveMine(minute + 1, mineralsMinedThisMinute, RobotType.GEODE, robotsAfterCreate)
                } else {
                    val max = mutableListOf<Int>()

                    if (robots.count { it == RobotType.OBSIDIAN } < blueprint.maxObsidianCost && mineralsMinedThisMinute.contains(blueprint.obsidianRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, RobotType.OBSIDIAN, robotsAfterCreate)
                    }

                    if (robots.count { it == RobotType.CLAY } < blueprint.maxClayCost && mineralsMinedThisMinute.contains(blueprint.clayRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, RobotType.CLAY, robotsAfterCreate)
                    }

                    if (robots.count { it == RobotType.ORE } < blueprint.maxOreCost && mineralsMinedThisMinute.contains(blueprint.oreRobotCost)) {
                        max += recursiveMine(minute + 1, mineralsMinedThisMinute, RobotType.ORE, robotsAfterCreate)
                    }

                    max += recursiveMine(minute + 1, mineralsMinedThisMinute, null, robotsAfterCreate)

                    max.maxOf { it }
                }
            }
        }
    }

    private fun mineWithRobot(robotType: RobotType, mineralsMined: MineralsMined) {
        when (robotType) {
            RobotType.ORE -> mineralsMined.ore++
            RobotType.CLAY -> mineralsMined.clay++
            RobotType.OBSIDIAN -> mineralsMined.obsidian++
            RobotType.GEODE -> mineralsMined.geode++
        }
    }
}