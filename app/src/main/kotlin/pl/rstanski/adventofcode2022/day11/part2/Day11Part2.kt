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

        val monkeysMap = monkeys.associateBy { it.index }

        (1..20).forEach {
            monkeys.forEach { monkey ->
                monkey.takeAll().forEach { item ->
                    var newItem = monkey.operation.calculate(item)
//                    newItem = newItem / 3
                    val monkeyToThrow = monkey.test.resultOfTesting(newItem)

                    monkeysMap[monkeyToThrow]!!.catch(newItem)
                }
            }

//            if (it in listOf(1, 20, 1000)) {
            println("Round: $it")
            monkeys.forEach {
                println(it)
                println(it.items.count().toString() + ": " + it.inspectsCount)
            }
//                monkeys.forEach { monkey ->
//                    println(monkey.inspectsCount.toString() + " : " + monkey.items.count())
//                }
//            }
        }


        val mmm = monkeys.sortedByDescending { it.inspectsCount }.take(2)


        println(mmm[0].inspectsCount * mmm[1].inspectsCount)

        return mmm[0].inspectsCount * mmm[1].inspectsCount
    }
}

data class Item(val worryLevel: BigInteger, val dividableBy: Set<BigInteger>)

data class Monkey(val index: Int, val items: MutableList<Item>, val operation: Operation, val test: Test) {

    var inspectsCount = 0L

    fun takeAll(): List<Item> {
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
        val items = worryLevels.map { worryLevel ->
            val devidable = listOf(2, 3, 5, 7, 11, 13, 17, 19).map { it.toBigInteger() }
                .filter { worryLevel.mod(it) == BigInteger.ZERO }
                .toSet()
            Item(worryLevel, dividableBy = devidable)
        }


        // Operation: new = old * 19
        val parts = lines[2].drop("  Operation: new = old ".length).split(" ")
        val operationValue = when (parts[1]) {
            "old" -> BigInteger("-1")
            else -> parts[1].toBigInteger()
        }

        val devidable = if (operationValue != BigInteger("-1")) {
            listOf(2, 3, 5, 7, 11, 13, 17, 19).map { it.toBigInteger() }
                .filter { operationValue.mod(it) == BigInteger.ZERO }
                .toSet()
        } else {
            emptySet()
        }
        val operation = when (parts[0]) {
            "+" -> Add(operationValue)
            "*" -> Multiply(operationValue, devidable)
            else -> throw IllegalArgumentException("unknown operation:" + parts[0])
        }

        //  Test: divisible by 23
        val divisibleBy = lines[3].drop("  Test: divisible by ".length).toBigInteger()
        val trueTest = lines[4].drop("    If true: throw to monkey ".length).toInt()
        val falseTest = lines[5].drop("    If false: throw to monkey ".length).toInt()
        val test = Test(divisibleBy, trueTest, falseTest)

        return Monkey(index, items.toMutableList(), operation, test)
    }
}

data class Test(val divisibleBy: BigInteger, val trueTest: Int, val falseTest: Int) {

    fun resultOfTesting(item: Item): Int {
        return when (item.dividableBy.contains(divisibleBy)) {
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
            BigInteger("-1") -> Item(item.worryLevel + item.worryLevel, item.dividableBy + BigInteger.TWO)
            else -> {
                val newWorryLevel = item.worryLevel + value
                val dividable = listOf(2, 3, 5, 7, 11, 13, 17, 19).map { it.toBigInteger() }
                    .filter { newWorryLevel.mod(it) == BigInteger.ZERO }
                    .toSet()
                Item(newWorryLevel, dividable)
            }
        }
    }

}

data class Multiply(val value: BigInteger, val dividableBy: Set<BigInteger>) : Operation {
    override fun calculate(item: Item): Item {
        return when (value) {
            BigInteger("-1") -> item.copy(worryLevel = item.worryLevel * item.worryLevel)
            else -> {
                Item(item.worryLevel * value, item.dividableBy + dividableBy)
            }
        }
    }
}

private fun String.isNotListSeparator(): Boolean =
    this.isNotBlank()