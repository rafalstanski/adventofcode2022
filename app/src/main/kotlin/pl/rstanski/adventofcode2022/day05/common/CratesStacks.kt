package pl.rstanski.adventofcode2022.day05.common

class CratesStacks(stacksNumber: Int) {
    private val stacks: List<CratesStack>

    init {
        require(stacksNumber > 0)
        stacks = List(stacksNumber) { CratesStack() }
    }

    fun putCrateOnStack(stackIndex: Int, crate: Char) {
        stacks[stackIndex].putCrate(crate)
    }

    fun takeCrateFromStack(stackIndex: Int): Char {
        return stacks[stackIndex].takeCrate()
    }

    fun topCrateFromAllStacks(): List<Char> {
        return stacks.map { it.topCrate() }
    }

    fun print() {
        println(stacks.map { it.topCrate() }.joinToString(""))
    }
}