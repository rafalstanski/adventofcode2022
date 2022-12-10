package pl.rstanski.adventofcode2022.day10.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day10.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day10Part2Solution.solve(puzzle)

    println(result)
}

object Day10Part2Solution {
    fun solve(puzzle: Puzzle): Any {

        val instructions = puzzle.lines.map { parseInstruction(it) }
        val processor = Processor()

        processor.executeAll(instructions)

        return ""
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


class Operation(var cycles: Int, var instruction: Instruction) {

    fun useCycle() {
        cycles -= 1
    }
}

class Processor {
    private var registerX = 1

    fun executeAll(instructions: List<Instruction>) {
        val operations = instructions.map { createOperation(it) }
        var cycle = 0
        var operationIndex = 0

        var pixelPosition = 0

        while (operationIndex < operations.size) {
            cycle += 1

            val sprintPosition = registerX - 1
            if (pixelPosition in (sprintPosition .. sprintPosition + 2)) {
                print("#")
            } else {
                print(".")
            }

            val operation = operations[operationIndex]
            operation.useCycle()
            if (operation.cycles == 0) {
                execute(operation.instruction)
                operationIndex += 1
            }

            pixelPosition++
            if (pixelPosition.mod(40) == 0) {
                pixelPosition = 0
                println()
            }
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