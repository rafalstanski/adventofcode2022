package pl.rstanski.adventofcode2022.day11.common

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.splitByEmptyLine

object MonkeysExtractor {

    fun extractMonkeys(puzzle: Puzzle): List<Monkey> {
        val groups = puzzle.lines.splitByEmptyLine()
        return groups.map(::convertToMonkey)
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