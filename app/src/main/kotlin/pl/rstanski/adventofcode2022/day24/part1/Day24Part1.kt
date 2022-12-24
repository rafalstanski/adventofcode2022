package pl.rstanski.adventofcode2022.day24.part1

import java.util.LinkedList
import kotlin.math.min
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day24.common.Grid
import pl.rstanski.adventofcode2022.day24.common.printGrid
import pl.rstanski.adventofcode2022.day24.part1.Direction.DOWN
import pl.rstanski.adventofcode2022.day24.part1.Direction.LEFT
import pl.rstanski.adventofcode2022.day24.part1.Direction.RIGHT
import pl.rstanski.adventofcode2022.day24.part1.Direction.UP

fun main() {
    val testSolution = solvePart1(load("day24sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 18)

    val solution = solvePart1(load("day24.txt"))
    println("solution: $solution")
}


enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

data class Blizzard(val index: Int, val position: Point, val direction: Direction, val dot: Char) {

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

private fun Char.toDirection(): Direction =
    when (this) {
        '>' -> RIGHT
        '<' -> LEFT
        '^' -> UP
        'v' -> DOWN
        else -> throw IllegalArgumentException("Unknown direction: $this")
    }

data class Me(var position: Point) {
    fun move(blizzards: List<Blizzard>, boundaries: Boundaries): List<Point> {
        val around = neighbours().map { it to isGround(it, blizzards, boundaries) }
        return around.filter { it.second }.map { it.first }
    }

    private fun isGround(position: Point, blizzards: List<Blizzard>, boundaries: Boundaries): Boolean {
        return blizzards.none { it.position == position } && boundaries.isInside(position)
    }

    private fun neighbours(): List<Point> {
        return listOf(position.down(), position.right(), position.up(), position.left())
    }
}


data class StateKey(val blizzards: List<Blizzard>, val currentPosition: Point, val myNexMove: Point)

data class State(val blizzards: List<Blizzard>, val currentPosition: Point, val myNexMove: Point, val movesCount: Int) {
    fun toKey() = StateKey(blizzards, currentPosition, myNexMove)
}

private fun solvePart1(puzzle: Puzzle): Any {
    val blizzards = mutableListOf<Blizzard>()

    puzzle.lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, dot ->
            val position = Point(x, y)
            when (dot) {
                '>', '<', '^', 'v' -> {
                    blizzards += Blizzard(blizzards.size, position, dot.toDirection(), dot)
                }
            }
        }
    }
    val maxX = puzzle.lines.first().length - 1
    val maxY = puzzle.lines.size - 1

    val start = Point(1,0)
    val exit = Point(maxX - 1, maxY)

    val exitAlgorithm = ExitAlgorithm(exit, Boundaries(maxX, maxY, start, exit))

    return exitAlgorithm.find(start, blizzards)
}

data class Boundaries(val maxX: Int, val maxY: Int, val startPosition: Point, val endPosition: Point) {
    fun isOutside(position: Point): Boolean {
        return position.x <= 0 || position.y <= 0 || position.x >= maxX || position.y >= maxY
    }

    fun isInside(position: Point): Boolean {
        return position == endPosition || !isOutside(position)
    }
}

class ExitAlgorithm(private val exitPoint: Point, private val boundaries: Boundaries) {

    var minMoves = 500

    fun find(startPoint: Point, blizzards: List<Blizzard>): Int {
        val initState = State(blizzards.toList(), startPoint, startPoint, 0)
        val statesToRun = LinkedList<State>()
        statesToRun.add(initState)

        val statesVisited = mutableSetOf<StateKey>()

        while (statesToRun.isNotEmpty()) {
            val state = statesToRun.poll()
//            printState(state)

            if (state.toKey() in statesVisited) {
//                println("been here: ${statesVisited.size}")
                continue
            } else {
                statesVisited += state.toKey()
            }

            if (state.movesCount > minMoves) {
                //stop walking
            } else {
                if (state.myNexMove == exitPoint) {
                    println("found exit: ${state.movesCount + 1}")
                    minMoves = min(minMoves, state.movesCount + 1)
                } else {
                    val nextStates = runState(state)
                    nextStates.forEach { statesToRun.add(it) }
                }
            }
        }

        return minMoves
    }

    private fun runState(state: State): List<State> {
        val currentPosition = state.myNexMove
        val blizzards = state.blizzards
        val moveCount = state.movesCount

        val blizzardsAfterMove = blizzards.map { blizzard -> blizzard.move(boundaries) }
        val possibleMoves = proposeMoves(currentPosition, blizzardsAfterMove)

        return possibleMoves.map { State(blizzardsAfterMove, currentPosition, it, moveCount + 1) } +
                State(blizzardsAfterMove, currentPosition, currentPosition, moveCount + 1)
    }

    private fun proposeMoves(position: Point, blizzards: List<Blizzard>): List<Point> {
        return neighbours(position)
            .filter { isGround(it, blizzards, boundaries) }
    }

    private fun isGround(position: Point, blizzards: List<Blizzard>, boundaries: Boundaries): Boolean {
        return blizzards.none { it.position == position } && boundaries.isInside(position)
    }

    private fun neighbours(position: Point): List<Point> {
        return listOf(position.up(), position.right(), position.down(), position.left())
    }

    private fun printState(state: State) {
        val grid = Grid<Char>()

        (0..boundaries.maxX).map { Point(it, 0) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxX).map { Point(it, boundaries.maxY) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxY).map { Point(0, it) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxY).map { Point(boundaries.maxX, it) }
            .forEach { grid.putPoint(it, '#') }

        state.blizzards.forEach { grid.putPoint(it.position, it.dot) }

        grid.putPoint(exitPoint, 'X')

        grid.putPoint(state.myNexMove, 'E')

        println("****** ${state.movesCount}  *****")
        printGrid(grid, Point(0, 0), Point(10, 7))
    }
}