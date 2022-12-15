package pl.rstanski.adventofcode2022.day14.part1

import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.PointOutOfGridException
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.toInts

private const val PUZZLE_FILENAME = "day14.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day14Part1Solution.solve(puzzle)

    println(result)
}


object Day14Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val rockPaths = puzzle.lines.map { parseRockPath(it) }

        val maxX = rockPaths.maxOf { it.coordinatesList.maxOf(Point::x) } + 1
        val maxY = rockPaths.maxOf { it.coordinatesList.maxOf(Point::y) } + 1

        val grid = Grid(maxX, maxY) { 0 }

        populateGrid(grid, rockPaths)
        printGrid(grid)

        val sand = Point(500, 0)

        var sandCount = 0
        while (true) {
            try {
                tryToPutSand(grid, sand)
            } catch ( e : PointOutOfGridException) {
                return sandCount
            }
            sandCount += 1
        }
    }

    private fun tryToPutSand(grid: Grid<Int>, sand: Point) {
        val tile = grid.getPoint(sand.up())
        grid.putPoint(sand, -1)

        if (tile <= 0) {
            tryToPutSand(grid, sand.up())
        } else {
            val leftDown = grid.getPoint(sand.up().left())
            val rightDown = grid.getPoint(sand.up().right())

            if (leftDown <= 0) {
                tryToPutSand(grid, sand.up().left())
            } else if (rightDown <= 0) {
                tryToPutSand(grid, sand.up().right())
            } else {
                grid.putPoint(sand, 2)
            }
        }
    }

    private fun printGrid(grid: Grid<Int>) {
        (0..9).forEach { y ->
            print("$y")
            (493..504).forEach { x ->
                when (grid.getPointOrNull(x, y)) {
                    -1 -> print("x")
                    1 -> print("#")
                    2 -> print("o")
                    else -> print(".")
                }
            }
            println()
        }
    }

    private fun populateGrid(grid: Grid<Int>, rockPaths: List<RockPath>) {
        rockPaths.forEach { rockPath ->
            rockPath.lines.forEach { rockLine ->
                require(rockLine.start.x == rockLine.stop.x || rockLine.start.y == rockLine.stop.y)

                when {
                    rockLine.start.x == rockLine.stop.x -> populateVertical(grid, rockLine)
                    rockLine.start.y == rockLine.stop.y -> populateHorizontal(grid, rockLine)
                }
            }
        }
    }

    private fun populateVertical(grid: Grid<Int>, rockLine: RockLine) {
        val startY = Math.min(rockLine.start.y, rockLine.stop.y)
        val stopY = Math.max(rockLine.start.y, rockLine.stop.y)

        (startY..stopY).forEach { y ->
            grid.putPoint(rockLine.start.x, y, 1)
        }
    }

    private fun populateHorizontal(grid: Grid<Int>, rockLine: RockLine) {
        val startX = Math.min(rockLine.start.x, rockLine.stop.x)
        val stopX = Math.max(rockLine.start.x, rockLine.stop.x)

        (startX..stopX).forEach { x ->
            grid.putPoint(x, rockLine.start.y, 1)
        }
    }


    private fun parseRockPath(pathLine: String): RockPath {
        val values = pathLine.split(" -> ")
            .map { parseCoordinates(it) }

        return RockPath(values)
    }

    private fun parseCoordinates(coordinatesLine: String): Point {
        val parts = coordinatesLine.split(",").toInts()
        return Point(parts[0], parts[1])
    }
}

data class RockPath(val coordinatesList: List<Point>) {

    val lines: List<RockLine> = coordinatesList.windowed(2, 1).map { RockLine(it[0], it[1]) }

}

data class RockLine(val start: Point, val stop: Point)
