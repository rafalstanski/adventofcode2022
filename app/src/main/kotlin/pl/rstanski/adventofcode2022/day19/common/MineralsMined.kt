package pl.rstanski.adventofcode2022.day19.common

data class MineralsMined(
    var ore: Int = 0,
    var clay: Int = 0,
    var obsidian: Int = 0,
    var geode: Int = 0
) {
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
}