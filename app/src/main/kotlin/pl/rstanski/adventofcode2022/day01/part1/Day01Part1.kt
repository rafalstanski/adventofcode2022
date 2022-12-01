package pl.rstanski.adventofcode2022.day01.part1

import java.math.BigInteger

fun main() {
    val data: List<String> = loadFromFile("day01/day01.txt")

    val parser = Parser(data)
    val max = parser.extract()
        .maxByOrNull { it.sum() }

    println(max?.sum())
}

class Parser(private val data: List<String>) {

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

fun loadFromFile(fileName: String): List<String> {
    val resource = Thread.currentThread().contextClassLoader.getResource(fileName)

    return requireNotNull(resource)
        .readText()
        .split("\n")
}