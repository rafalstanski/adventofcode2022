package pl.rstanski.adventofcode2022.day22.part1

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.SplitLines
import pl.rstanski.adventofcode2022.common.toInts
import pl.rstanski.adventofcode2022.day22.part1.BoardParser.parseBoard
import pl.rstanski.adventofcode2022.day22.part1.InstructionsParser.parseInstructions
import pl.rstanski.adventofcode2022.day22.part1.StartingPointFinder.findStartingPoint

fun main() {
    val testSolution = solvePart1(load("day22sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 6032L)

    val solution = solvePart1(load("day22.txt"))
    println("solution: $solution")
}

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

object BoardParser {

    fun parseBoard(mapOfTheBoard: List<String>): Grid<Char> {
        val board = Grid<Char>()

        mapOfTheBoard.forEachIndexed { y, row ->
            row.forEachIndexed { x, column ->
                when (column) {
                    ' ' -> {} // board.putPoint(x,y, '#')
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


private fun solvePart1(puzzle: Puzzle): Any {
    val (mapOfTheBoard, pathYouMustFollow) = SplitLines.split(puzzle.lines)

    //open tiles (on which you can move, drawn .) and solid walls (tiles which you cannot enter, drawn #).
    val board = parseBoard(mapOfTheBoard)
    val startingPoint = findStartingPoint(mapOfTheBoard)
    val instructions = parseInstructions(pathYouMustFollow.first())

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
                            nextPosition =
                                if (facingDirection == Point(1, 0))
                                    board.getPointsY(currentPosition.y).first()
                                else if (facingDirection == Point(-1, 0))
                                    board.getPointsY(currentPosition.y).last()
                                else if (facingDirection == Point(0, 1))
                                    board.getPointsX(currentPosition.x).first()
                                else //if (facingDirection == Point(0, -1))
                                    board.getPointsX(currentPosition.x).last()
                        }
                    }
                }
            }
            is Rotate -> facingDirection = instruction.rotate(facingDirection)
        }
    }

    printGrid(board, Point(0, 0), Point(20, 12))

    println(currentPosition)
    println(facingDirection)

    return 1000L * (currentPosition.y + 1) + 4L * (currentPosition.x + 1) + numberOfDirection(facingDirection)
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

fun numberOfDirection(faceingDirection: Point): Int {
    val rotate = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))

    return rotate.indexOf(faceingDirection)
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

data class Direction(val value: Int, val turnDirection: Char) {

    fun rotate(lookingAt: Point): Point {
        val rotate = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))
        val index = rotate.indexOf(lookingAt)
        val newIndex: Int = when (turnDirection) {
            'R' -> index + 1
            'L' -> index - 1
            else -> throw IllegalArgumentException()
        }
        return rotate[newIndex.mod(4)]
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

