package pl.rstanski.adventofcode2022.day11.part1

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day11.common.Item
import pl.rstanski.adventofcode2022.day11.common.Monkeys
import pl.rstanski.adventofcode2022.day11.common.MonkeysExtractor.extractMonkeys

private const val PUZZLE_FILENAME = "day11.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day11Part1Solution.solve(puzzle)

    println(result)
}

object Day11Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val parsedMonkeys = extractMonkeys(puzzle)
        val monkeys = Monkeys(parsedMonkeys)

        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.takeAllItems().forEach { item ->
                    monkey.changeWorryLevel(item)
                    reliefWorryLevel(item) { it / 3.toBigInteger() }
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