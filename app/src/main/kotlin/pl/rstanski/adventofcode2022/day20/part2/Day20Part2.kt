package pl.rstanski.adventofcode2022.day20.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.toLongs

fun main() {
    val testSolution = solvePart2(load("day20sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 1623178306L)

    val solution = solvePart2(load("day20.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val numbers = puzzle.lines.toLongs()

    val encryptedFile = EncryptedFile(numbers)
    repeat(10) {
        encryptedFile.mix()
    }
    return encryptedFile.groveCoordinatesSum()
}

class EncryptedFile(originalNumbers: List<Long>) {
    private val original = originalNumbers
        .mapIndexed { index: Int, i: Long ->  NumberNode(index, i * 811589153) }
        .toMutableList()
    private val mixed = original.toMutableList()

    fun mix() {
        original.forEach { number: NumberNode ->
            val currentIndex = mixed.indexOf(number)
            val newIndex = (currentIndex + number.value).mod(mixed.size - 1)

            mixed.removeAt(currentIndex)
            mixed.add(newIndex, number)
        }
    }

    fun groveCoordinatesSum(): Long {
        val indexOfZero = mixed.indexOfFirst { node -> node.value == 0L }

        return listOf(1000, 2000, 3000)
            .map { (indexOfZero + it) % mixed.size }
            .sumOf { mixed[it].value }
    }
}

data class NumberNode(val index: Int, val value: Long)