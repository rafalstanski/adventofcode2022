package pl.rstanski.adventofcode2022.day11.part2

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day11.common.Item
import pl.rstanski.adventofcode2022.day11.common.Monkeys
import pl.rstanski.adventofcode2022.day11.common.MonkeysExtractor

private const val PUZZLE_FILENAME = "day11.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day11Part2Solution.solve(puzzle)

    println(result)
}

object Day11Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val parsedMonkeys = MonkeysExtractor.extractMonkeys(puzzle)
        val monkeys = Monkeys(parsedMonkeys)

        val reductor = parsedMonkeys.map { it.test.divisibleBy }.reduce { acc, next -> acc * next }

        repeat(10000) {
            monkeys.forEach { monkey ->
                monkey.takeAllItems().forEach { item ->
                    monkey.changeWorryLevel(item)
                    reliefWorryLevel(item) { it.mod(reductor) }
                    val monkeyToThrow = monkey.decideWhereToThrow(item)
                    monkeys.monkeyByIndex(monkeyToThrow)
                        .catch(item)
                }
            }
        }

        return calculateMonkeyBusiness(monkeys)
    }

    private fun reliefWorryLevel(item: Item, modify: (BigInteger) -> BigInteger) {
        item.worryLevel = modify(item.worryLevel)
    }

    private fun calculateMonkeyBusiness(monkeys: Monkeys): Long {
        val twoMostInspected = monkeys.sortedFromMostInspectsCount()
            .take(2)

        return twoMostInspected[0].inspectsCount * twoMostInspected[1].inspectsCount
    }
}