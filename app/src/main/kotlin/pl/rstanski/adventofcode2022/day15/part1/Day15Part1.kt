package pl.rstanski.adventofcode2022.day15.part1

import kotlin.math.max
import kotlin.math.min
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day15.common.Sensor
import pl.rstanski.adventofcode2022.day15.common.SensorParser
import pl.rstanski.adventofcode2022.day15.part1.Objects.BEACON
import pl.rstanski.adventofcode2022.day15.part1.Objects.CANNOT_BE
import pl.rstanski.adventofcode2022.day15.part1.Objects.SENSOR

private const val PUZZLE_FILENAME = "day15.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day15Part1Solution.solve(puzzle, 2000000)

    println(result)
}

object Day15Part1Solution {
    fun solve(puzzle: Puzzle, checkY: Int): Any {
        val sensors = puzzle.lines.map(SensorParser::parseSensor)

        val grid = Grid<Objects>()
        sensors.forEach {
            grid.putPoint(it.location, SENSOR)
            grid.putPoint(it.closestBeacon, BEACON)
        }

        val min = sensors.minOf { min(it.location.x, it.closestBeacon.x) - it.distance }
        val max = sensors.maxOf { max(it.location.x, it.closestBeacon.x) + it.distance }
        val xRange = (min..max)
        println("range to check $xRange")

        sensors.forEach { sensor -> putCannotBe(checkY, grid, sensor, xRange) }

//        printGrid(grid, Point(xRange.first, checkY - 15), Point(xRange.first + 100, checkY + 15))

        return xRange.count { x -> grid.getPoint(x, checkY) == CANNOT_BE }
    }

    private fun putCannotBe(checkY: Int, grid: Grid<Objects>, sensor: Sensor, xRange: IntRange) {
        xRange
            .map { x -> Point(x, checkY) }
            .filter { sensor.location.manhattanDistanceTo(it) <= sensor.distance }
            .forEach {
                if (grid.getPoint(it) == null) {
                    grid.putPoint(it, CANNOT_BE)
                }
            }
    }


    private fun printGrid(grid: Grid<Objects>, corner1: Point, corner2: Point) {
        (corner1.y..corner2.y).forEach { y ->
            print(y.toString().padStart(3))
            (corner1.x..corner2.x).forEach { x ->
                when (grid.getPoint(x, y)) {
                    SENSOR -> print("S")
                    BEACON -> print("B")
                    CANNOT_BE -> print("#")
                    else -> print(".")
                }
            }
            println()
        }
    }
}

enum class Objects {
    SENSOR, BEACON, CANNOT_BE
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
}