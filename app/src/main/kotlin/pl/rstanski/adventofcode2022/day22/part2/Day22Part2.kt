package pl.rstanski.adventofcode2022.day22.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.SplitLines
import pl.rstanski.adventofcode2022.day22.common.Instruction
import pl.rstanski.adventofcode2022.day22.common.InstructionsParser.parseInstructions
import pl.rstanski.adventofcode2022.day22.common.Move
import pl.rstanski.adventofcode2022.day22.common.PasswordCalculator
import pl.rstanski.adventofcode2022.day22.common.Rotate
import pl.rstanski.adventofcode2022.day22.part2.BoardParser.parseBoard
import pl.rstanski.adventofcode2022.day22.part2.Edge.Bottom
import pl.rstanski.adventofcode2022.day22.part2.Edge.Left
import pl.rstanski.adventofcode2022.day22.part2.Edge.Right
import pl.rstanski.adventofcode2022.day22.part2.Edge.Top
import pl.rstanski.adventofcode2022.day22.part2.StartingPointFinder.findStartingPoint

fun main() {
    val testSolution = solvePart2(load("day22sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 0)

    val solution = solvePart2(load("day22.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val (mapOfTheBoard, pathYouMustFollow) = SplitLines.split(puzzle.lines)

    val board = parseBoard(mapOfTheBoard)
    val startingPoint = findStartingPoint(mapOfTheBoard)
    val instructions = parseInstructions(pathYouMustFollow.first())

    val cube = Cube(board, startingPoint, 4)
    val (position, facingDirection) = cube.go(instructions)

    return PasswordCalculator.calculatePassword(position, facingDirection)
}


class Side(val number: Int, val leftTop: Point, val size: Int = 4) {

    private var edgesConnections = mutableMapOf<Edge, Connection>()

    fun addConnection(onWhatEdge: Edge, toWhatSide: Int, pointOnNextSide: (Point) -> Point, newDirection: Point) {
        edgesConnections[onWhatEdge] = Connection(toWhatSide, pointOnNextSide, newDirection)
    }

    fun contains(point: Point): Boolean {
        return leftTop.x <= point.x && leftTop.y <= point.y
                && leftTop.x + size > point.x && leftTop.y + size > point.y
    }

    fun connectionAt(currentSideEdge: Edge): Connection {
        return edgesConnections[currentSideEdge]
            ?: throw IllegalStateException("No connection on edge $currentSideEdge for side $number")
    }

    fun relativePosition(absolutePosition: Point): Point {
        return absolutePosition - leftTop
    }

    fun absolutePosition(relativePosition: Point): Point {
        return relativePosition + leftTop
    }
}

data class Connection(val neighbourNumber: Int, val pointOnNextSide: (Point) -> Point, val newDirection: Point)


enum class Edge {
    Top, Bottom, Left, Right
}

class Cube(private val board: Grid<Char>, private val startingPoint: Point, private val size: Int) {

    private val sides = mutableListOf<Side>()

    init {
        addSide(1, Point(8, 0))
        addSide(2, Point(0, 4))
        addSide(3, Point(4, 4))
        addSide(4, Point(8, 4))
        addSide(5, Point(8, 8))
        addSide(6, Point(12, 8))

        sideOf(1).addConnection(Left, 3, { prev -> Point(prev.y, 0) }, Point(0, 1))
        sideOf(1).addConnection(Top, 5, { prev -> Point(prev.x, size - 1) }, Point(0, -1))
        sideOf(1).addConnection(Right, 6, { prev -> Point(size - 1, size - 1 - prev.y) }, Point(-1, 0))

        sideOf(4).addConnection(Right, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        //----
        sideOf(2).addConnection(Left, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(2).addConnection(Top, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(2).addConnection(Bottom, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))

        sideOf(3).addConnection(Top, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(3).addConnection(Bottom, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))

        sideOf(5).addConnection(Left, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(5).addConnection(Bottom, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))

        sideOf(6).addConnection(Top, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(6).addConnection(Right, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
        sideOf(6).addConnection(Bottom, 6, { prev -> Point(size - 1 - prev.y, 0) }, Point(0, 1))
    }

    fun addSide(number: Int, leftTop: Point) {
        sides += Side(number, leftTop, size)
    }

    fun go(instructions: List<Instruction>): Pair<Point, Point> {
        var currentPosition = startingPoint
        var facingDirection = Point(1, 0)

        instructions.forEach { instruction ->
            when (instruction) {
                is Move -> {
                    var stepsTaken = 0
                    var noWall = true
                    var nextPosition = currentPosition + facingDirection

                    while (stepsTaken < instruction.count && noWall) {
                        when (board.getPoint(nextPosition)) {
                            'o', '.' -> {
                                currentPosition = nextPosition
                                board.putPoint(currentPosition, 'o')
                                stepsTaken++
                                nextPosition = currentPosition + facingDirection
                            }

                            '#' -> noWall = false
                            null -> {
                                val (wrappedNextPosition, wrappedFacingDirection) = wrap(currentPosition, facingDirection)
                                if (board.getPoint(wrappedNextPosition) == '#') {
                                    noWall = false
                                } else {
                                    nextPosition = wrappedNextPosition
                                    facingDirection = wrappedFacingDirection
                                }
                            }
                        }
                    }
                }

                is Rotate -> facingDirection = instruction.rotate(facingDirection)
            }
        }

        printGrid(board, Point(0, 0), Point(20, 12))

        return currentPosition to facingDirection
    }

    private fun sideFor(point: Point): Side {
        return sides.find { it.contains(point) }!!
    }

    private fun sideOf(number: Int): Side {
        return sides.find { it.number == number }!!
    }

    private fun Point.toEdge(): Edge {
        return when (this) {
            Point(1, 0) -> Right
            Point(0, 1) -> Bottom
            Point(-1, 0) -> Left
            Point(0, -1) -> Top
            else -> throw IllegalArgumentException()
        }
    }

    private fun wrap(currentPosition: Point, facingDirection: Point): Pair<Point, Point> {
        val currentSide = sideFor(currentPosition)
        val currentSideEdge = facingDirection.toEdge()
        val connection = currentSide.connectionAt(currentSideEdge)

        val nextSide = sideOf(connection.neighbourNumber)

        val relativeCurrentPosition = currentSide.relativePosition(currentPosition)
        val absoluteNextPosition = nextSide.absolutePosition(connection.pointOnNextSide(relativeCurrentPosition))

        return absoluteNextPosition to connection.newDirection
    }
}


object BoardParser {

    fun parseBoard(mapOfTheBoard: List<String>): Grid<Char> {
        val board = Grid<Char>()

        mapOfTheBoard.forEachIndexed { y, row ->
            row.forEachIndexed { x, column ->
                when (column) {
                    ' ' -> {}
                    else -> board.putPoint(x, y, column)
                }
            }
        }

        return board
    }
}

object StartingPointFinder {
    fun findStartingPoint(mapOfTheBoard: List<String>): Point {
        val column = mapOfTheBoard.first()
            .mapIndexed { index, char -> index to char }
            .find { it.second == '.' }!!
            .first
        return Point(column, 0)
    }
}

class Grid<T> {
    private val points = mutableMapOf<Point, T>()

    fun putPoint(point: Point, value: T) {
        points[point] = value
    }

    fun putPoint(x: Int, y: Int, value: T) {
        putPoint(Point(x, y), value)
    }

    fun getPoint(point: Point): T? =
        points[point]

    fun getPoint(x: Int, y: Int): T? =
        getPoint(Point(x, y))

    fun getPointsX(x: Int): List<Point> {
        return points.keys.filter { it.x == x }.sortedBy { it.x }
    }

    fun getPointsY(y: Int): List<Point> {
        return points.keys.filter { it.y == y }.sortedBy { it.y }
    }

}

private fun printGrid(grid: Grid<Char>, corner1: Point, corner2: Point) {
    (corner1.y..corner2.y).forEach { y ->
        print(y.toString().padStart(3))
        (corner1.x..corner2.x).forEach { x ->
            when (grid.getPoint(x, y)) {
                null -> print(" ")
                else -> print(grid.getPoint(x, y))
            }
        }
        println()
    }
}