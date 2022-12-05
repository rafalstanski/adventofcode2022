package pl.rstanski.adventofcode2022.day05.common

class CratesStacks(stacksNumber: Int) {
    private val stacks: List<CratesStack>

    init {
        require(stacksNumber > 0)
        stacks = List(stacksNumber) { CratesStack() }
    }

    fun putCratesOnStack(stackNumber: Int, crates: List<Char>) {
        crates.forEach { putCrateOnStack(stackNumber, it) }
    }

    private fun putCrateOnStack(stackNumber: Int, crate: Char) {
        stacks[stackNumber - 1].putCrate(crate)
    }

    fun takeCrateFromStack(stackNumber: Int): Char =
        stacks[stackNumber - 1].takeCrate()

    fun topCrateFromAllStacks(): List<Char> =
        stacks.map(CratesStack::topCrate)
}