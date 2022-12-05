package pl.rstanski.adventofcode2022.day05.common

import pl.rstanski.adventofcode2022.common.isNotBlank

object CratesStacksBuilder {

    fun buildCratesStacks(drawing: Drawing): CratesStacks {
        // 01234567890
        // [Z]     [P]
        //  1   2   3
        val stackNumberToIndexList: List<StackNumberToIndex> = stackNumberToIndexMapping(drawing)
        val stacksCount: Int = stackNumberToIndexList.size

        val cratesStacks = CratesStacks(stacksCount)

        stackNumberToIndexList
            .map { it.stackNumber to cratesToPutOnStack(it.index, drawing) }
            .forEach { (stackNumber, crates) ->
                cratesStacks.putCratesOnStack(stackNumber, crates)
            }

        return cratesStacks
    }

    private fun stackNumberToIndexMapping(drawing: Drawing): List<StackNumberToIndex> =
        drawing.indexes
            .mapIndexed { index, stackNumber -> stackNumber to index }
            .filter { it.first.isNotBlank() }
            .map { StackNumberToIndex(it.first.digitToInt(), it.second) }

    private fun cratesToPutOnStack(cratesStackIndex: Int, drawing: Drawing): List<Char> =
        drawing.crates
            .map { it[cratesStackIndex] }
            .filter { it.isNotBlank() }
            .reversed()
}

private data class StackNumberToIndex(
    val stackNumber: Int,
    val index: Int
)