package pl.rstanski.adventofcode2022.day17.part1

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day17.common.Cave
import pl.rstanski.adventofcode2022.day17.common.printGrid

fun main() {
    val testSolution = solvePart1(load("day17sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 3068)

    val solution = solvePart1(load("day17.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val directions = parse(puzzle.singleLine)

    //seven units wide
    //Each rock appears so that its left edge is two units away from the left wall
    //bottom edge is three units above the highest rock in the room (or the floor, if there isn't one).

    val cave = Cave()

    var directionIndex = 0
    var shapeIndex = 0
    var rock = Shapes.createRock(shapeIndex)
    rock.placeRock(cave)
    var rocksStopped = 0

    while (rocksStopped < 2022) {
        val direction = directions[directionIndex]
        directionIndex++
        directionIndex = directionIndex.mod(directions.size)

        rock.push(direction, cave)

        if (! rock.moveDown(cave)) {
            rocksStopped++
            rock.paint(cave)

            shapeIndex++
            shapeIndex = shapeIndex.mod(5)
            rock = Shapes.createRock(shapeIndex)
            rock.placeRock(cave)
        }
    }
    printGrid(cave, Point(0,0), Point(6, 50))

    return cave.top + 1
}

object Shapes {

    private val rocks = listOf(
        ::HLine, ::Cross, ::L, ::VLine, ::Square
    )

    fun createRock(shapeIndex: Int): Rock {
        val creator = rocks[shapeIndex]
        return creator()
    }
}
sealed class Rock(var position: Point = Point(0,0)) {

    open fun points(): List<Point> {
        TODO()
    }

    private fun positionedPoints(): List<Point> {
        return points().map { it + position }
    }

    fun placeRock(cave: Cave) {
        position = Point(2, 3 + cave.top + 1)
    }

    fun push(direction: Direction, cave: Cave) {
        val moved = positionedPoints().map { direction.move(it) }
        val outOfBound = moved.find { it.x < 0 || it.x >= 7 || cave.getPoint(it) == 1 }
        if (outOfBound == null) {
            position = direction.move(position)
        }
    }

    fun moveDown(cave: Cave): Boolean {
        val moved = positionedPoints().map { it.down() }
        val outOfBound = moved.find { it.y < 0 || cave.getPoint(it) == 1 }
        if (outOfBound == null) {
            position = position.down()
            return true
        } else {
            return false
        }
    }

    fun paint(cave: Cave) {
        positionedPoints().forEach {cave.putPoint(it, 1)}
    }
}

class HLine : Rock() {

    private val points = listOf(
        Point(0,0), Point(1,0), Point(2,0), Point(3,0)
    )
    override fun points(): List<Point> {
        return points
    }
}

class VLine : Rock() {

    private val points = listOf(
        Point(0,3),
        Point(0,2),
        Point(0,1),
        Point(0,0)
    )
    override fun points(): List<Point> {
        return points
    }
}

class Square : Rock() {

    private val points = listOf(
        Point(0,1), Point(1,1),
        Point(0,0), Point(1,0)
    )
    override fun points(): List<Point> {
        return points
    }
}


class Cross : Rock() {

    private val points = listOf(
                        Point(1,2),
        Point(0,1), Point(1,1), Point(2,1),
                        Point(1,0),
    )
    override fun points(): List<Point> {
        return points
    }
}

class L : Rock() {

    private val points = listOf(
                                            Point(2,2),
                                            Point(2,1),
        Point(0,0), Point(1,0), Point(2,0),
    )
    override fun points(): List<Point> {
        return points
    }
}


sealed interface Direction {

    fun move(point: Point): Point
}


object Left : Direction {
    override fun move(point: Point): Point {
        return point.left()
    }

}

object Right : Direction {
    override fun move(point: Point): Point {
        return point.right()
    }
}

private fun parse(line: String): List<Direction> {
    return line.toCharArray().map { dir ->
        when (dir) {
            '<' -> Left
            '>' -> Right
            else -> throw IllegalArgumentException()
        }
    }
}
