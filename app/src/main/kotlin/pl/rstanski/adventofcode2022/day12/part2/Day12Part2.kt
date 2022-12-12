package pl.rstanski.adventofcode2022.day12.part2

import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day12.common.GridParser.parseGrid
import pl.rstanski.adventofcode2022.day12.common.PathFinder
import pl.rstanski.adventofcode2022.day12.common.PointFinder.findPoint

private const val PUZZLE_FILENAME = "day12.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day12Part2Solution.solve(puzzle)

    println(result)
}

object Day12Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val grid = parseGrid(puzzle)
        val bestSignal = findPoint(puzzle, 'E')

        val allStartPoints = findAllStartingPoints(grid)

        val pathFinder = PathFinder(grid)
        val shortestPathLength = allStartPoints.map { startingPoint ->
            try {
                pathFinder.findShortestPathLength(startingPoint, bestSignal)
            } catch (e: IllegalStateException) {
                Int.MAX_VALUE
            }
        }
            .minOf { it }

        return shortestPathLength
    }

    private fun findAllStartingPoints(grid: Grid<Int>) =
        (0 until grid.xSize).map { x ->
            (0 until grid.ySize).map { y ->
                Point(x, y) to grid.getPoint(x, y)
            }
        }.flatten()
            .filter { it.second == 'a'.code }
            .map { it.first }
}