package pl.rstanski.adventofcode2022.day24.common

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.day24.common.Direction.DOWN
import pl.rstanski.adventofcode2022.day24.common.Direction.LEFT
import pl.rstanski.adventofcode2022.day24.common.Direction.RIGHT
import pl.rstanski.adventofcode2022.day24.common.Direction.UP

object BlizzardParser {
    fun parseBlizzard(puzzle: Puzzle): List<Blizzard> {
        val blizzards = mutableListOf<Blizzard>()

        puzzle.lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, dot ->
                val position = Point(x, y)
                when (dot) {
                    '>', '<', '^', 'v' -> {
                        blizzards += Blizzard(blizzards.size, position, dot.toDirection())
                    }
                }
            }
        }

        return blizzards
    }
}


data class Blizzard(val index: Int, val position: Point, val direction: Direction) {

    fun move(boundaries: Boundaries): Blizzard {
        val tryNextPosition = nextPosition()
        val nextPosition = if (boundaries.isOutside(tryNextPosition)) {
            wrap(boundaries)
        } else {
            tryNextPosition
        }
        return this.copy(position = nextPosition)
    }

    private fun wrap(boundaries: Boundaries): Point {
        return when (direction) {
            LEFT -> position.copy(x = boundaries.maxX - 1)
            RIGHT -> position.copy(x = 1)
            UP -> position.copy(y = boundaries.maxY - 1)
            DOWN -> position.copy(y = 1)
        }
    }

    private fun nextPosition(): Point {
        return when (direction) {
            LEFT -> position.left()
            RIGHT -> position.right()
            UP -> position.down()
            DOWN -> position.up()
        }
    }
}