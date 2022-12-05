package pl.rstanski.adventofcode2022.day05.common

object CratesStacksBuilder {

    fun buildCratesStacks(drawing: Drawing): CratesStacks {
        val cratesIndex = drawing.indexes
            .mapIndexed { index, sign -> index to sign }
            .filter { it.second != ' ' }
            .map { it.first to it.second.digitToIntOrNull()!! }
        val stacksNumber = cratesIndex.last().second

        val cratesLayers = drawing.crates.map { cratesLine ->
            val crates = MutableList(stacksNumber) { ' ' }
            cratesIndex.map { index ->
                val create = cratesLine[index.first]
                if (create != ' ') {
                    crates[index.second - 1] = create
                }
            }

            crates.toList()
        }

        val cratesStacks = CratesStacks(stacksNumber)

        cratesLayers.reversed().forEach { cratesLayer ->
            cratesLayer.forEachIndexed { index, crate ->
                if (crate != ' ') cratesStacks.putCrateOnStack(index, crate)
            }
        }

        return cratesStacks
    }
}