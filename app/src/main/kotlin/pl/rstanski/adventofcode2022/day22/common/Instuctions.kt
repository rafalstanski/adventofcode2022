package pl.rstanski.adventofcode2022.day22.common

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.toInts

object InstructionsParser {

    fun parseInstructions(instructionsLine: String): List<Instruction> {
        val moves = Regex("(\\d+)")
            .findAll(instructionsLine)
            .map { it.groupValues[1] }.toList()
            .toInts()
            .map { Move(it) }

        val rotates = Regex("([RL])")
            .findAll(instructionsLine)
            .map { it.groupValues[1] }
            .toList()
            .map { Rotate(it.first()) }

        val instructions = mutableListOf<Instruction>(moves.first())
        (rotates.indices).forEach {
            instructions += rotates[it]
            instructions += moves[it + 1]
        }

        return instructions
    }
}

sealed class Instruction

data class Move(val count: Int) : Instruction()

data class Rotate(val turnDirection: Char) : Instruction() {

    private val rotate = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))
    fun rotate(lookingAt: Point): Point {
        val index = rotate.indexOf(lookingAt)
        val newIndex: Int = when (turnDirection) {
            'R' -> index + 1
            'L' -> index - 1
            else -> throw IllegalArgumentException()
        }
        return rotate[newIndex.mod(4)]
    }
}