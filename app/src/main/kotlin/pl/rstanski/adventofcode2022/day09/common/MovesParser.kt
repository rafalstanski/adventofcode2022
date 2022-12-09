package pl.rstanski.adventofcode2022.day09.common

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.day09.common.Direction.DOWN
import pl.rstanski.adventofcode2022.day09.common.Direction.LEFT
import pl.rstanski.adventofcode2022.day09.common.Direction.RIGHT
import pl.rstanski.adventofcode2022.day09.common.Direction.UP

object MovesParser {

    fun parseMoves(puzzle: Puzzle): List<Move> =
        puzzle.lines.map(::parseMove)

    private fun parseMove(line: String): Move {
        val parts: List<String> = line.split(" ")
        return Move(parts[0].toDirection(), parts[1].toInt())
    }
}

private fun String.toDirection(): Direction =
    when (this) {
        "R" -> RIGHT
        "L" -> LEFT
        "U" -> UP
        "D" -> DOWN
        else -> throw IllegalArgumentException("Unknown direction: $this")
    }

data class Move(
    val direction: Direction,
    val value: Int
)

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}