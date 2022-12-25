package pl.rstanski.adventofcode2022.day24.common

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}


fun Char.toDirection(): Direction =
    when (this) {
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        else -> throw IllegalArgumentException("Unknown direction: $this")
    }

fun Direction.toChar(): Char =
    when (this) {
        Direction.RIGHT -> '>'
        Direction.LEFT -> '<'
        Direction.UP -> '^'
        Direction.DOWN -> 'v'
    }