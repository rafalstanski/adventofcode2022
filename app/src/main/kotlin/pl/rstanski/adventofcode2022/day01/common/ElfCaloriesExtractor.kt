package pl.rstanski.adventofcode2022.day01.common

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.toBigIntegers

class ElfCaloriesExtractor {

    fun extract(puzzle: Puzzle): List<ElfCalories> {
        val itemsCaloriesIterator = puzzle.lines.iterator()

        val currentElfCalories = mutableListOf<String>()
        val elfCaloriesList = mutableListOf<ElfCalories>()

        while (itemsCaloriesIterator.hasNext()) {
            val itemCalories = itemsCaloriesIterator.next()

            if (itemCalories.isNotListSeparator()) {
                currentElfCalories.add(itemCalories)
            } else {
                val elfCalories = convertToElfCalories(currentElfCalories)
                elfCaloriesList.add(elfCalories)
                currentElfCalories.clear()
            }
        }
        val elfCalories = convertToElfCalories(currentElfCalories)
        elfCaloriesList.add(elfCalories)

        return elfCaloriesList
    }

    private fun convertToElfCalories(currentElfCalories: MutableList<String>) =
        ElfCalories(currentElfCalories.toList().toBigIntegers())
}

private fun String.isNotListSeparator(): Boolean =
    this.isNotBlank()