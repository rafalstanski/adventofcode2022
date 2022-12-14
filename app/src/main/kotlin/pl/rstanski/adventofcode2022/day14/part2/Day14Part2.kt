package pl.rstanski.adventofcode2022.day14.part2

import kotlin.math.max
import kotlin.math.min
import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.toInts

private const val PUZZLE_FILENAME = "day14.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day14Part2Solution.solve(puzzle)

    println(result)
}


object Day14Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val paths = puzzle.lines
            .map { parseCoordinates(it) }

        val maxX = paths.flatten().maxOf { it.x } + 1
        val maxY = paths.flatten().maxOf { it.y } + 1

        val grid = Grid(maxX, maxY) { 0 }

        paths.forEach {
            it.windowed(2, 1).forEach { (start, stop) ->
                grid.drawHorizontal(start, stop)
                grid.drawVertical(start, stop)
            }
        }

        val sandSource = Point(500, 0)
        var sandCount = 0

        while (grid.getPoint(sandSource) == 0) {
            putSand(grid, sandSource)
            sandCount += 1
        }

        return sandCount
    }

    private fun putSand(grid: Grid<Int>, sandSource: Point) {
        val tileUnder = grid.getPoint(sandSource.up())

    }

    private fun parseCoordinates(line: String): List<Point> {
        return line.split(" -> ").map {
            val parts = it.split(",").toInts()
            Point(parts[0], parts[1])
        }
    }
}

private fun Grid<Int>.drawHorizontal(start: Point, stop: Point) {
    if (start.y == stop.y) {
        val min = min(start.x, stop.x)
        val max = max(start.x, stop.x)
        (min..max).forEach { x -> this.putPoint(x, start.y, 0) }
    }
}

private fun Grid<Int>.drawVertical(start: Point, stop: Point) {
    if (start.x == stop.x) {
        val min = min(start.y, stop.y)
        val max = max(start.y, stop.y)
        (min..max).forEach { y -> this.putPoint(start.x, y, 0) }
    }
}