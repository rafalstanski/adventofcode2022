package pl.rstanski.adventofcode2022.day11.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.toInts

private const val PUZZLE_FILENAME = "day11.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day11Part1Solution.solve(puzzle)

    println(result)
}

object Day11Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val monkeys = MonkeysExtractor.extract(puzzle)

        val monkeysMap = monkeys.associateBy { it.index }

//        var monkey = monkeysMap[0]!!

        (1..20).forEach {
            monkeys.forEach { monkey ->
                monkey.takeAll().forEach { item ->
                    var newItem = monkey.operation.calculate(item)
                    newItem = newItem / 3
                    val monkeyToThrow = monkey.test.resultOfTesting(newItem)

                    monkeysMap[monkeyToThrow]!!.catch(newItem)
                }
            }
        }

//        monkeys.forEach {
//            println(it)
//            println(it.inspectsCount)
//        }

        val mmm = monkeys.sortedByDescending { it.inspectsCount }.take(2)


        println(mmm[0].inspectsCount * mmm[1].inspectsCount)

        return mmm[0].inspectsCount * mmm[1].inspectsCount
    }
}
//val startingItems: List<Int>
data class Monkey(val index: Int, val items: MutableList<Int>, val operation: Operation, val test: Test) {

    var inspectsCount = 0
    fun takeFirst(): Int {
        return items.removeFirst()
    }

    fun takeAll(): List<Int> {
        val returnItems = items.toList()
        inspectsCount += returnItems.size
        items.clear()

        return returnItems
    }

    fun catch(newItem: Int) {
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
        val items = lines[1].drop("  Starting items: ".length).split(", ").toInts()

        // Operation: new = old * 19
        val parts = lines[2].drop("  Operation: new = old ".length).split(" ")
        val operationValue = when (parts[1]) {
            "old" -> -1
            else -> parts[1].toInt()
        }
        val operation = when (parts[0]) {
            "+" -> Add(operationValue)
            "*" -> Multiply(operationValue)
            else -> throw IllegalArgumentException("unknown operation:" + parts[0])
        }

        //  Test: divisible by 23
        val divisibleBy = lines[3].drop("  Test: divisible by ".length).toInt()
        val trueTest = lines[4].drop("    If true: throw to monkey ".length).toInt()
        val falseTest = lines[5].drop("    If false: throw to monkey ".length).toInt()
        val test = Test(divisibleBy, trueTest, falseTest)

        return Monkey(index, items.toMutableList(), operation, test)
    }
}

data class Test(val divisibleBy: Int, val trueTest: Int, val falseTest: Int) {

    fun resultOfTesting(item: Int): Int {
        return when(item.mod(divisibleBy) == 0) {
            true -> trueTest
            false -> falseTest
        }
    }
}
sealed interface Operation {

    fun calculate(item: Int): Int
}

data class Add(val value: Int): Operation {
    override fun calculate(item: Int): Int {
        return when (value) {
            -1 -> item + item
            else -> item + value
        }
    }

}
data class Multiply(val value: Int): Operation {
    override fun calculate(item: Int): Int {
        return when (value) {
            -1 -> item * item
            else -> item * value
        }
    }
}

private fun String.isNotListSeparator(): Boolean =
    this.isNotBlank()