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

    val cube = Cube(board, startingPoint)
    val (position, facingDirection) = cube.go(instructions)

    return PasswordCalculator.calculatePassword(position, facingDirection)
}

class Side(val number: Int, val leftTop: Point, val size: Int = 4, neighbourSides: List<Pair<Edge, Pair<Edge, Int>>> = emptyList()) {

    fun contains(point: Point): Boolean {
        return leftTop.x <= point.x && leftTop.y <= point.y
            && leftTop.x + size > point.x && leftTop.y + size > point.y
    }

    fun findStickFor(neighbourSide: Int): Pair<Edge, Edge> {
        TODO("Not yet implemented")
    }

    fun neighbourAt(currentSideEdge: Edge): Triple<Int, Int, Int> {
        TODO("Not yet implemented")
    }
}


enum class Edge {
    Top, Bottom, Left, Right
}

class Cube(private val board: Grid<Char>, private val startingPoint: Point) {

    private val sides = mutableListOf<Side>()

    init {
        sides += Side(1, Point(8, 0))
        sides += Side(2, Point(0, 4))
        sides += Side(3, Point(4, 4))
        sides += Side(4, Point(8, 4))
        sides += Side(5, Point(8, 8))
        sides += Side(6, Point(12, 8))
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
                                val (wrappedNextPosition, wrappedFacingDirection) = wrap(currentPosition, nextPosition, facingDirection)
                                if (board.getPoint(nextPosition) == '#') {
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

    private val transitions: Map<Pair<Int, Int>, Transition> = mutableMapOf()

    sealed interface Transition {
        fun transit(nextPosition: Point, facingDirection: Point): Pair<Point, Point>
    }

    object NoChangesTransition : Transition {
        override fun transit(nextPosition: Point, facingDirection: Point): Pair<Point, Point> {
            return nextPosition to facingDirection
        }
    }

    sealed class Change

    object TakeX : Change()
    object TakeY : Change()
    object TakeMin : Change()
    object TakeMax : Change()
    object TakeXReversed : Change()

    object TakeYReversed : Change()

    data class ChangeXYTransition(val x: Change, val y: Change) : Transition {
        override fun transit(nextPosition: Point, facingDirection: Point): Pair<Point, Point> {
            return Point(nextPosition.y, nextPosition.x) to facingDirection
        }
    }

    data class DirectionTransition(val x: Int, val y:Int): Transition {
        override fun transit(nextPosition: Point, facingDirection: Point): Pair<Point, Point> {
            TODO("Not yet implemented")
        }

    }



    private fun sideFor(point: Point): Side {
        return sides.find { it.contains(point) }!!
    }

    private fun Point.toEdge(): Edge {
        TODO()
    }

    private fun wrap(currentPosition: Point, nextPosition: Point, facingDirection: Point): Pair<Point, Point> {
        val currentSide = sideFor(currentPosition)
        val currentSideEdge = facingDirection.toEdge()
        val (neighbourSide, rotate, negate) = currentSide.neighbourAt(currentSideEdge)

        when (rotate) {
            0 -> currentPosition
            90 -> Point()
            180 -> {}
            270 -> {}
            else -> throw IllegalArgumentException("Unknown rotate: $rotate")
        }

        // private val rotate = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))



        val nextSide = sideFor(nextPosition)

        if (currentSide.number == nextSide.number) {
            return nextPosition to facingDirection
        } else {
            val stickFromTo = currentSide.findStickFor(nextSide.number)
            TODO()
        }
    }

    private fun transition(fromEdge: Edge, toEdge: Edge) {
        // Top, Bottom, Left, Right
        val edgeTransition = fromEdge to toEdge

        when (edgeTransition) {
            Top to Bottom -> listOf(NoChangesTransition)
            Top to Left -> listOf(ChangeXYTransition(TakeMin, TakeX), DirectionTransition(1, 0))
            Top to Right -> listOf(ChangeXYTransition(TakeMax, TakeXReversed), DirectionTransition(-1, 0))
            Top to Top -> listOf(DirectionTransition(0, -1))
        }

        Top to Bottom
        Top to Left
        Top to Right
        Top to Top

        Bottom to Bottom
        Bottom to Left
        Bottom to Right
        Bottom to Top

        Left to Bottom
        Left to Left
        Left to Right
        Left to Top

        Right to Bottom
        Right to Left
        Right to Right
        Right to Top
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