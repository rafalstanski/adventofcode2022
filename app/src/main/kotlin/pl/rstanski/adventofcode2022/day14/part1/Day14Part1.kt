package pl.rstanski.adventofcode2022.day14.part1

import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
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

        val maxX = rockPaths.maxOf { it.coordinatesList.maxOf { it.x } }
        val maxY = rockPaths.maxOf { it.coordinatesList.maxOf { it.y } }

        val grid = Grid(maxX, maxY) { 0 }

        populateGrid(grid, rockPaths)

        printGrid(grid)

//        while (true) {
        //one unit at a time
        //produced until the previous unit of sand comes to rest

//        }
        TODO()
    }

    private fun printGrid(grid: Grid<Int>) {
        (494..504).forEach { x ->
            (494..504).forEach { y ->
                when (grid.getPoint(x, y)) {
                    1 -> println("#")
                    else -> println(".")
                }
            }
        }
    }

    private fun populateGrid(grid: Grid<Int>, rockPaths: List<RockPath>) {
        rockPaths.forEach { rockPath ->
            rockPath.lines.forEach { rockLine ->
                require(rockLine.start.x == rockLine.stop.x || rockLine.start.y == rockLine.stop.y)

                when {
                    rockLine.start.x == rockLine.stop.x -> (rockLine.start.y..rockLine.stop.y).forEach { y ->
                        grid.putPoint(rockLine.start.x, y, 1)
                    }

                    rockLine.start.y == rockLine.stop.y -> (rockLine.start.x..rockLine.stop.x).forEach { x ->
                        grid.putPoint(x, rockLine.start.y, 1)
                    }
                }
            }
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
