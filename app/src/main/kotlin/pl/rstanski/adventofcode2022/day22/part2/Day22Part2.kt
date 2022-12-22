package pl.rstanski.adventofcode2022.day22.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.SplitLines
import pl.rstanski.adventofcode2022.day22.common.BoardParser.parseBoard
import pl.rstanski.adventofcode2022.day22.common.Grid
import pl.rstanski.adventofcode2022.day22.common.Instruction
import pl.rstanski.adventofcode2022.day22.common.InstructionsParser.parseInstructions
import pl.rstanski.adventofcode2022.day22.common.Move
import pl.rstanski.adventofcode2022.day22.common.PasswordCalculator
import pl.rstanski.adventofcode2022.day22.common.Rotate
import pl.rstanski.adventofcode2022.day22.common.StartingPointFinder.findStartingPoint
import pl.rstanski.adventofcode2022.day22.common.printGrid
import pl.rstanski.adventofcode2022.day22.part2.Edge.Bottom
import pl.rstanski.adventofcode2022.day22.part2.Edge.Left
import pl.rstanski.adventofcode2022.day22.part2.Edge.Right
import pl.rstanski.adventofcode2022.day22.part2.Edge.Top


val goRight = Point(1, 0)
val goLeft = Point(-1, 0)
val goUp = Point(0, -1)
val goDown = Point(0, 1)

fun main() {
    val testSolution = solvePart2(load("day22sample.txt"), 4) { cube, size ->
        cube.addSide(1, Point(8, 0))

        cube.addSide(2, Point(0, 4))
        cube.addSide(3, Point(4, 4))
        cube.addSide(4, Point(8, 4))

        cube.addSide(5, Point(8, 8))
        cube.addSide(6, Point(12, 8))


        cube.sideOf(1)
            .addConnection(Left, 3, { Point(it.y, 0) }, goDown)
            .addConnection(Top, 5, { Point(it.x, size - 1) }, goUp)
            .addConnection(Right, 6, { Point(size - 1, size - 1 - it.y) }, goLeft)

        cube.sideOf(4)
            .addConnection(Right, 6, { Point(size - 1 - it.y, 0) }, goDown)

        cube.sideOf(2)
            .addConnection(Left, 6, { Point(size - 1 - it.y, size - 1) }, goUp)
            .addConnection(Top, 1, { Point(size - 1 - it.x, 0) }, goDown)
            .addConnection(Bottom, 5, { Point(size - 1 - it.x, size - 1) }, goDown)

        cube.sideOf(3)
            .addConnection(Top, 1, { Point(0, it.x) }, Point(1, 0))
            .addConnection(Bottom, 5, { Point(0, size - 1 - it.x) }, goRight)

        cube.sideOf(5)
            .addConnection(Left, 3, { Point(size - 1 - it.y, 0) }, goDown)
            .addConnection(Bottom, 2, { Point(size - 1 - it.x, size - 1) }, goUp)

        cube.sideOf(6)
            .addConnection(Top, 4, { Point(size - 1, size - 1 - it.x) }, goLeft)
            .addConnection(Right, 1, { Point(0, size - 1 - it.y) }, goLeft)
            .addConnection(Bottom, 2, { Point(0, size - 1 - it.x) }, goRight)
    }
    println("test solution: $testSolution")
    check(testSolution == 5031L)

    val solution = solvePart2(load("day22.txt"), 50) { cube, size ->
        val max = size - 1

        cube.addSide(2, Point(size * 1, 0))
        cube.addSide(3, Point(size * 2, 0))

        cube.addSide(5, Point(size * 1, size * 1))

        cube.addSide(7, Point(0, size * 2))
        cube.addSide(8, Point(size * 1, size * 2))

        cube.addSide(10, Point(0, size * 3))


        cube.sideOf(2)
            .addConnection(Top, 10, { Point(0, it.x) }, goRight)
            .addConnection(Left, 7, { Point(0, max - it.y) }, goRight)

        cube.sideOf(3)
            .addConnection(Top, 10, { Point(it.x, max) }, goUp)
            .addConnection(Right, 8, { Point(max, max - it.y) }, goLeft)
            .addConnection(Bottom, 5, { Point(max, it.x) }, goLeft)

        cube.sideOf(5)
            .addConnection(Left, 7, { Point(it.y, 0) }, goDown)
            .addConnection(Right, 3, { Point(it.y, max) }, goUp)

        cube.sideOf(8)
            .addConnection(Bottom, 10, { Point(max, it.x) }, goLeft)
            .addConnection(Right, 3, { Point(max, max - it.y) }, goLeft)

        cube.sideOf(7)
            .addConnection(Top, 5, { Point(0, it.x) }, goRight)
            .addConnection(Left, 2, {Point(0, max - it.y)}, goRight)

        cube.sideOf(10)
            .addConnection(Left, 2, { Point(it.y,0) }, goDown)
            .addConnection(Right, 8, { Point(it.y, max) }, goUp)
            .addConnection(Bottom, 3, { Point(it.x, 0) }, goDown)
    }
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle, size: Int, configuration: (Cube, Int) -> Unit): Any {
    val (mapOfTheBoard, pathYouMustFollow) = SplitLines.split(puzzle.lines)

    val board = parseBoard(mapOfTheBoard)
    val startingPoint = findStartingPoint(mapOfTheBoard)
    val instructions = parseInstructions(pathYouMustFollow.first())

    val cube = Cube(board, startingPoint, size, configuration)
    val (position, facingDirection) = cube.go(instructions)

    println("$position, $facingDirection")

    return PasswordCalculator.calculatePassword(position, facingDirection)
}

class Cube(
    private val board: Grid<Char>,
    private val startingPoint: Point,
    private val size: Int,
    configuration: (Cube, Int) -> Unit
) {

    private val sides = mutableListOf<Side>()

    init {
        configuration(this, size)
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
                                val (wrappedNextPosition, wrappedFacingDirection) = wrap(
                                    currentPosition,
                                    facingDirection
                                )
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

    fun sideOf(number: Int): Side {
        return sides.find { it.number == number }!!
    }

    private fun Point.toEdge(): Edge {
        return when (this) {
            Point(1, 0) -> Right
            Point(0, 1) -> Bottom
            Point(-1, 0) -> Left
            Point(0, -1) -> Top
            else -> throw IllegalArgumentException("Unknown edge: $this")
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


class Side(val number: Int, private val leftTop: Point, private val size: Int = 4) {

    private var edgesConnections = mutableMapOf<Edge, Connection>()

    fun addConnection(onWhatEdge: Edge, toWhatSide: Int, pointOnNextSide: (Point) -> Point, newDirection: Point): Side {
        edgesConnections[onWhatEdge] = Connection(toWhatSide, pointOnNextSide, newDirection)
        return this
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