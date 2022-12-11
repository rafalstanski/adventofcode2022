package pl.rstanski.adventofcode2022.day11.part2

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day11.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day11Part2Solution.solve(puzzle)

    println(result)
}

object Day11Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val monkeys = MonkeysExtractor.extract(puzzle)
        val monkeysByIndex = monkeys.associateBy(Monkey::index)

        val reductor = monkeys.map { it.test.divisibleBy }.reduce { acc, bigInteger -> acc * bigInteger }



        repeat(10000) {
            monkeys.forEach { monkey ->
                monkey.takeAllItems().forEach { item ->
                    var newItem = monkey.operation.calculate(item)
                    newItem = Item(newItem.worryLevel.mod(reductor))
                    val monkeyToThrow = monkey.test.resultOfTesting(newItem)

                    monkeysByIndex[monkeyToThrow]!!.catch(newItem)
                }
            }

            if ((it + 1) in listOf(1, 20, 1000)) {
                println("Round: ${it+1}")
                monkeys.forEach { monkey ->
                    println("monkey ${monkey.index}: ${monkey.inspectsCount}")
                }
            }
        }

        val twoMostInspected = monkeys.sortedByDescending(Monkey::inspectsCount)
            .take(2)

        return twoMostInspected[0].inspectsCount * twoMostInspected[1].inspectsCount
    }
}

data class Item(val worryLevel: BigInteger)

data class Monkey(val index: Int, val items: MutableList<Item>, val operation: Operation, val test: Test) {

    var inspectsCount = 0L

    fun takeAllItems(): List<Item> {
        val returnItems = items.toList()
        inspectsCount += returnItems.size
        items.clear()

        return returnItems
    }

    fun catch(newItem: Item) {
        items.add(newItem)
    }
}

object MonkeysExtractor {

    fun extract(puzzle: Puzzle): List<Monkey> {
        val linesIterator = puzzle.lines.iterator()

        val currentGroup = mutableListOf<String>()
        val monkeys = mutableListOf<Monkey>()

        while (linesIterator.hasNext()) {
            val line = linesIterator.next()

            if (line.isNotListSeparator()) {
                currentGroup.add(line)
            } else {
                val monkey = convertToMonkey(currentGroup)
                monkeys.add(monkey)
                currentGroup.clear()
            }
        }
        val monkey = convertToMonkey(currentGroup)
        monkeys.add(monkey)

        return monkeys
    }

    private fun convertToMonkey(lines: List<String>): Monkey {
        // Monkey 0:
        val index = lines[0].dropLast(1).split(" ")[1].toInt()

        // Starting items: 79, 98
        val worryLevels = lines[1].drop("  Starting items: ".length).split(", ").map(String::toBigInteger)
        val items = worryLevels.map(::Item)


        // Operation: new = old * 19
        val parts = lines[2].drop("  Operation: new = old ".length).split(" ")
        val operationValue = when (parts[1]) {
            "old" -> BigInteger("-1")
            else -> parts[1].toBigInteger()
        }

        val operation = when (parts[0]) {
            "+" -> Add(operationValue)
            "*" -> Multiply(operationValue)
            else -> throw IllegalArgumentException("unknown operation:" + parts[0])
        }

        //  Test: divisible by 23
        val divisibleBy = lines[3].drop("  Test: divisible by ".length).toBigInteger()
        //    If true: throw to monkey 2
        val trueTest = lines[4].drop("    If true: throw to monkey ".length).toInt()
        //   If false: throw to monkey 0
        val falseTest = lines[5].drop("    If false: throw to monkey ".length).toInt()
        val test = Test(divisibleBy, trueTest, falseTest)

        return Monkey(index, items.toMutableList(), operation, test)
    }
}

data class Test(val divisibleBy: BigInteger, val trueTest: Int, val falseTest: Int) {

    fun resultOfTesting(item: Item): Int {
        return when (item.worryLevel.mod(divisibleBy) == BigInteger.ZERO) {
            true -> trueTest
            false -> falseTest
        }
    }
}

sealed interface Operation {

    fun calculate(item: Item): Item
}

data class Add(val value: BigInteger) : Operation {
    override fun calculate(item: Item): Item {
        return when (value) {
            BigInteger("-1") -> Item(item.worryLevel + item.worryLevel)
            else -> Item(item.worryLevel + value)
        }
    }
}

// (a + b) mod x = ((a mod x) + (b mod x)) mod x
// (a * b) mod x = ((a mod x) * (b mod x)) mod x

data class Multiply(val value: BigInteger) : Operation {
    override fun calculate(item: Item): Item {
        return when (value) {
            BigInteger("-1") -> Item(item.worryLevel * item.worryLevel)
            else -> Item(item.worryLevel * value)
        }
    }
}

private fun String.isNotListSeparator(): Boolean =
    this.isNotBlank()