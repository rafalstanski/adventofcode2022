package pl.rstanski.adventofcode2022.day13.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.SplitLines

private const val PUZZLE_FILENAME = "day13.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day13Part1Solution.solve(puzzle)

    println(result)
}


object Day13Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val groups: List<List<String>> = SplitLines.split(puzzle.lines)

        return groups.mapIndexed { index, group -> parsePackets(index + 1, group) }
            .filter { it.second == ORDER.RIGHT_ORDER }
            .map {
                println(it)
                it
            }
            .sumOf { it.first }
    }

    private fun parsePackets(index: Int, group: List<String>): Pair<Int, ORDER> {
        val left = parseLine(group[0])
        val right = parseLine(group[1])
        val afterParseLeft = left.toString().replace(" ", "")
        val afterParseRight = right.toString().replace(" ", "")
        require(afterParseLeft == group[0]) { "There is a different original = ${group[0]}, parsed = $afterParseLeft"}
        require(afterParseRight == group[1]) { "There is a different original = ${group[1]}, parsed = $afterParseRight"}

        println(index)
        println(compare(left, right))
        return index to compare(left, right)
    }

    enum class ORDER {
        RIGHT_ORDER, NONE, NOT_RIGHT_ORDER
    }

    private fun compare(leftList: List<Any>, rightList: List<Any>): ORDER {
//        println("comparing left = $leftList, right = $rightList")

        val leftIterator = leftList.iterator()
        val rightIterator = rightList.iterator()

        while (leftIterator.hasNext() && rightIterator.hasNext()) {
            val left = leftIterator.next()
            val right = rightIterator.next()

            if (left is Int && right is Int) {
                if (left < right) {
                    return ORDER.RIGHT_ORDER
                }
                if (left > right) {
                    return ORDER.NOT_RIGHT_ORDER
                }
            }

            if (left is List<*> && right is List<*>) {
                val innerCompare = compare(left as List<Any>, right as List<Any>)
                if (innerCompare != ORDER.NONE) return innerCompare
//                if (left.size < right.size) return ORDER.RIGHT_ORDER
//                if (left.size > right.size) return ORDER.NOT_RIGHT_ORDER
            }

            if (left is List<*> && right is Int) {
                val innerCompare = compare(left as List<Any>, listOf<Any>(right))
                if (innerCompare != ORDER.NONE) return innerCompare
            }

            if (left is Int && right is List<*>) {
                val innerCompare = compare(listOf<Any>(left), right as List<Any>)
                if (innerCompare != ORDER.NONE) return innerCompare
            }
        }

        if ((!leftIterator.hasNext()) && rightIterator.hasNext()) {
            return ORDER.RIGHT_ORDER
        }

        if (leftIterator.hasNext() && !rightIterator.hasNext()) {
            return ORDER.NOT_RIGHT_ORDER
        }

        return ORDER.NONE
    }

    private fun parseLine(line: String): List<Any> {
        val iterator = line.drop(1).dropLast(1).iterator()
        return parseLine(iterator)
    }

    private fun parseLine(iterator: CharIterator): List<Any> {
        val values: MutableList<Any> = mutableListOf()
        val digits: MutableList<Char> = mutableListOf()

        while (iterator.hasNext()) {
            val char = iterator.next()

            if (char.isDigit()) digits.add(char)

            if (char == '[') values.add(parseLine(iterator))

            if (char == ']') {
                if (digits.isNotEmpty()) {
                    val number = digits.joinToString("").toInt()
                    values.add(number)
                }
                return values
            }

            if (char == ',') {
                if (digits.isNotEmpty()) {
                    val number = digits.joinToString("").toInt()
                    digits.clear()
                    values.add(number)
                }
            }
        }

        if (digits.isNotEmpty()) {
            val number = digits.joinToString("").toInt()
            values.add(number)
        }

        return values
    }
}


data class Packet(val values: List<Int>) {

}
