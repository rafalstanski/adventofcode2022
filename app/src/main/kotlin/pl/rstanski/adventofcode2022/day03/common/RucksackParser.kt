package pl.rstanski.adventofcode2022.day03.common

object RucksackParser {
    fun parseAsRucksack(line: String): Rucksack =
        Rucksack(line.toList())
}