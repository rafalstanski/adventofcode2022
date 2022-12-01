package pl.rstanski.adventofcode2022.day01.part2

import java.math.BigInteger

fun main() {
    val data: List<String> = loadFromFile("day01/day01.txt")

    val parser = Parser(data)
    val max = parser.extract()
        .sortedByDescending { it.sum() }

    println(max
        .take(3)
        .map { it.sum() }
        .sumOf { it })
}

class Parser(val data: List<String>) {

    fun extract(): List<Group> {
        val dataIterator = data.iterator()

        val stack = mutableListOf<String>()
        val result = mutableListOf<Group>()

        while (dataIterator.hasNext()) {
            val line = dataIterator.next()

            if (line.isNotBlank()) {
                stack.add(line)
            } else {
                val group = Group(stack.toList().map { it.toBigInteger() })
                result.add(group)
                stack.clear()
            }
        }
        val group = Group(stack.toList().map { it.toBigInteger() })
        result.add(group)

        return result
    }
}

data class Group(val calories: List<BigInteger>) {

    fun sum(): BigInteger =
        calories.sumOf { it }
}

class CaloriesList(private val data: Iterator<String>) {

    fun nextGroup(): List<String>? {


        val result: MutableList<String> = mutableListOf()

        var line: String? = null
        while (data.hasNext() && (line?.isNotBlank() != false)) {
            line = data.next()
            if (line.isNotBlank()) {
                result.add(line)
            }
        }

        return result
    }
}

fun loadFromFile(fileName: String): List<String> {
    val resource = Thread.currentThread().contextClassLoader.getResource(fileName)

    return requireNotNull(resource)
        .readText()
        .split("\n")
}

class Day01Part1 {
}