package pl.rstanski.adventofcode2022.day20.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.toInts

fun main() {
    val testSolution = solvePart1(load("day20sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 3)

    val solution = solvePart1(load("day20.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val numbers = puzzle.lines.toInts()

    val encryptedFile = EncryptedFile(numbers)
    encryptedFile.mix()
    return encryptedFile.groveCoordinatesSum()
}

class EncryptedFile(originalNumbers: List<Int>) {
    private val original = originalNumbers
        .mapIndexed { index: Int, i: Int ->  NumberNode(index, i) }
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

    fun groveCoordinatesSum(): Int {
        val indexOfZero = mixed.indexOfFirst { node -> node.value == 0 }

        return listOf(1000, 2000, 3000)
            .map { (indexOfZero + it) % mixed.size }
            .sumOf { mixed[it].value }
    }
}

data class NumberNode(val index: Int, val value: Int)
