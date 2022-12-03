package pl.rstanski.adventofcode2022.day03.common

object PriorityMapper {

    private val smallLetters = 'a'..'z'
    private val bigLetters = 'A'..'Z'

    fun priorityOf(item: Char): Int =
        when {
            smallLetters.contains(item) -> calculatePriorityForSmallLetter(item)
            bigLetters.contains(item) -> calculatePriorityForBigLetter(item)
            else -> throw IllegalArgumentException("Unknown item: $item")
        }

    private fun calculatePriorityForSmallLetter(item: Char): Int =
        item - 'a' + 1

    private fun calculatePriorityForBigLetter(item: Char): Int =
        item - 'A' + 27
}