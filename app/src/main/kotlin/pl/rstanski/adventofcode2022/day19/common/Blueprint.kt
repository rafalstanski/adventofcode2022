package pl.rstanski.adventofcode2022.day19.common

data class Blueprint(
    val index: Int,
    val oreRobotCost: Cost,
    val clayRobotCost: Cost,
    val obsidianRobotCost: Cost,
    val geodeRobotCosts: Cost
) {
    private val allCosts: List<Cost>
        get() = listOf(oreRobotCost, clayRobotCost, obsidianRobotCost, geodeRobotCosts)

    val maxOreCost = allCosts.maxOf { it.ore }
    val maxClayCost = allCosts.maxOf { it.clay }
    val maxObsidianCost = allCosts.maxOf { it.obsidian }

    fun robotCost(createRoobot: RobotType): Cost {
        return when (createRoobot) {
            RobotType.ORE -> oreRobotCost
            RobotType.CLAY -> clayRobotCost
            RobotType.OBSIDIAN -> obsidianRobotCost
            RobotType.GEODE -> geodeRobotCosts
        }
    }
}