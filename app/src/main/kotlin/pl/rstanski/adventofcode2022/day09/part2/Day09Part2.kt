package pl.rstanski.adventofcode2022.day09.part2

import kotlin.math.absoluteValue
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day09.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day09Part2Solution.solve(puzzle)

    println(result)
}

object Day09Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val moves = puzzle.lines.map { line ->
            val parts = line.split(" ")
            Move(parts[0], parts[1].toInt())
        }

        //the head (H) and tail (T) must always be touching

        println(moves)

        var h = Position(0, 0)
        val tails = MutableList(9) { Position(0, 0) }

        val tailPositions = mutableSetOf<Position>()
        tailPositions.add(tails[9 - 1])

        moves.forEach { move ->
            when (move.direction) {
                "R" -> {
                    (1..move.value).forEach {
                        h = h.right(1)

                        var t: Position
                        var tPrevious: Position = h
                        (0 until 9).forEach {
                            t = tails[it]
                            if (!t.overlapsOrNear(tPrevious)) {
                                t = t.moveNear(tPrevious)
                                if (it == 8) {
                                    tailPositions.add(t)
                                }
                            }
                            tails[it] = t
                            tPrevious = t
                        }
                    }

                } //right
                "U" -> {
                    (1..move.value).forEach {
                        h = h.up(1)

                        var t: Position
                        var tPrevious: Position = h
                        (0 until 9).forEach {
                            t = tails[it]
                            if (!t.overlapsOrNear(tPrevious)) {
                                t = t.moveNear(tPrevious)
                                if (it == 8) {
                                    tailPositions.add(t)
                                }
                            }
                            tails[it] = t
                            tPrevious = t
                        }
                    }

                } //up
                "D" -> {
                    (1..move.value).forEach {
                        h = h.down(1)

                        var t: Position
                        var tPrevious: Position = h
                        (0 until 9).forEach {
                            t = tails[it]
                            if (!t.overlapsOrNear(tPrevious)) {
                                t = t.moveNear(tPrevious)
                                if (it == 8) {
                                    tailPositions.add(t)
                                }
                            }
                            tails[it] = t
                            tPrevious = t
                        }
                    }
                } //down
                "L" -> {
                    (1..move.value).forEach {
                        h = h.left(1)

                        var t: Position
                        var tPrevious: Position = h
                        (0 until 9).forEach {
                            t = tails[it]
                            if (!t.overlapsOrNear(tPrevious)) {
                                t = t.moveNear(tPrevious)
                                if (it == 8) {
                                    tailPositions.add(t)
                                }
                            }
                            tails[it] = t
                            tPrevious = t
                        }
                    }
                } //left
            }
        }

        return tailPositions.size
    }
}

data class Move(val direction: String, val value: Int)

class Position(val x: Int, val y: Int, val previousPosition: Position? = null) {

    fun right(d: Int) = Position(x + d, y, this)
    fun left(d: Int) = Position(x - d, y, this)
    fun up(d: Int) = Position(x, y + d, this)
    fun down(d: Int) = Position(x, y - d, this)

    fun overlapsOrNear(point: Position): Boolean =
        (x - point.x).absoluteValue <= 1 && (y - point.y).absoluteValue <= 1

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y)"
    }

    fun moveNear(point: Position): Position {
        if (this.overlapsOrNear(point)) return point

        val xD = point.x - x
        val yD = point.y - y

        var xMove = 0
        var yMove = 0

        if (xD > 0) {
            xMove = 1
        }

        if (yD > 0) {
            yMove = 1
        }

        if (xD < 0) {
            xMove = -1
        }

        if (yD < 0) {
            yMove = -1
        }

        return Position(x + xMove, y + yMove)
    }
}