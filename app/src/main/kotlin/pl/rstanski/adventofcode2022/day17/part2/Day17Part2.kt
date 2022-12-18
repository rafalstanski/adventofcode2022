package pl.rstanski.adventofcode2022.day17.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day17.common.Cave

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day17sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 1514285714288L)

    val solution = solvePart2(PuzzleLoader.load("day17.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val directions = parse(puzzle.singleLine)

    val cave = Cave()

    var directionIndex = 0
    var shapeIndex = 0
    var rock = Shapes.createRock(shapeIndex)
    rock.placeRock(cave)
    var rocksStopped = 0L
    var cyclesHeight = 0L

    val patterns = mutableMapOf<TipState, Pair<Int, Long>>()

    while (rocksStopped < 1000000000000L) {
        val direction = directions[directionIndex]
        directionIndex++
        directionIndex = directionIndex.mod(directions.size)

        rock.push(direction, cave)

        if (!rock.moveDown(cave)) {
            rocksStopped++
            rock.paint(cave)

            val tipState = TipState(shapeIndex, directionIndex, tipOf(cave, 20))

            if (patterns.containsKey(tipState)) {
//                println("!!!!!!! pattern matched: $tipState")
                val (previousTop, previousRocksStopped) = patterns[tipState]!!
                val heightDiff = cave.top - previousTop
                val stoppedDiff = rocksStopped - previousRocksStopped
                val cyclesCount = (1000000000000L - rocksStopped) / stoppedDiff
                cyclesHeight += cyclesCount * heightDiff
                rocksStopped += stoppedDiff * cyclesCount
            }
            patterns[tipState] = cave.top to rocksStopped

            shapeIndex++
            shapeIndex = shapeIndex.mod(5)
            rock = Shapes.createRock(shapeIndex)
            rock.placeRock(cave)
        }
    }
//    printGrid(cave, Point(0, 0), Point(6, 50))

    return cave.top + cyclesHeight + 1
}

fun tipOf(cave: Cave, height: Int): Set<Point> {
    return (cave.top downTo cave.top - (height - 1)).map { y ->
        (0..6).map { x ->
            Point(x, y)
        }.filter { cave.getPoint(it) != null }
    }
        .flatten()
        .map { Point(it.x, it.y - (cave.top - (height - 1))) }
        .toSet()
}

data class TipState(
    val shapeIndex: Int,
    val directionIndex: Int,
    val tipPoints: Set<Point>
)

object Shapes {

    private val rocks = listOf(
        ::HLine, ::Cross, ::L, ::VLine, ::Square
    )

    fun createRock(shapeIndex: Int): Rock {
        val creator = rocks[shapeIndex]
        return creator()
    }
}

sealed class Rock(var position: Point = Point(0, 0)) {

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
        positionedPoints().forEach { cave.putPoint(it, 1) }
    }
}

class HLine : Rock() {

    private val points = listOf(
        Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)
    )

    override fun points(): List<Point> {
        return points
    }
}

class VLine : Rock() {

    private val points = listOf(
        Point(0, 3),
        Point(0, 2),
        Point(0, 1),
        Point(0, 0)
    )

    override fun points(): List<Point> {
        return points
    }
}

class Square : Rock() {

    private val points = listOf(
        Point(0, 1), Point(1, 1),
        Point(0, 0), Point(1, 0)
    )

    override fun points(): List<Point> {
        return points
    }
}


class Cross : Rock() {

    private val points = listOf(
        Point(1, 2),
        Point(0, 1), Point(1, 1), Point(2, 1),
        Point(1, 0),
    )

    override fun points(): List<Point> {
        return points
    }
}

class L : Rock() {

    private val points = listOf(
        Point(2, 2),
        Point(2, 1),
        Point(0, 0), Point(1, 0), Point(2, 0),
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