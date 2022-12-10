package pl.rstanski.adventofcode2022.day10.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day10.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day10Part1Solution.solve(puzzle)

    println(result)
}

object Day10Part1Solution {

    fun solve(puzzle: Puzzle): Any {

        val instructions = puzzle.lines.map { parseInstruction(it) }
        val processor = Processor()

        processor.executeAll(instructions)

        return processor.sum()
    }

    private fun parseInstruction(line: String): Instruction {
        val split = line.split(" ")
        return when (split[0]) {
            "noop" -> Noop
            "addx" -> Addx(split[1].toInt())
            else -> throw IllegalArgumentException()
        }
    }
}


class Operation(var cycles: Int, val instruction: Instruction) {

    fun useCycle() {
        cycles -= 1
    }
}

class Processor {
    private var registerX = 1
    private var cyclesSnapshots = mutableMapOf<Int, Int>()

    fun sum(): Int =
        cyclesSnapshots.map { entry -> entry.key * entry.value }
            .sum()

    fun executeAll(instructions: List<Instruction>) {
        val operations = instructions.map(::createOperation)
        var cycle = 0
        var operationIndex = 0
        while (operationIndex < operations.size) {
            cycle += 1

            takeRegisterXSnapshot(cycle)

            val operation = operations[operationIndex]
            operation.useCycle()
            if (operation.cycles == 0) {
                execute(operation.instruction)
                operationIndex += 1
            }
        }
    }

    private fun takeRegisterXSnapshot(cycles: Int) {
        if (cycles in arrayOf(20, 60, 100, 140, 180, 220)) {
            cyclesSnapshots[cycles] = registerX
        }
    }

    private fun createOperation(instruction: Instruction): Operation =
        when (instruction) {
            is Noop -> Operation(1, instruction)
            is Addx -> Operation(2, instruction)
        }

    private fun execute(instruction: Instruction) {
        when (instruction) {
            is Addx -> registerX += instruction.value
        }
    }
}

sealed class Instruction

object Noop : Instruction()

data class Addx(val value: Int) : Instruction()