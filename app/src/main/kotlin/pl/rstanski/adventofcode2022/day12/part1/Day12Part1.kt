package pl.rstanski.adventofcode2022.day12.part1

import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day12.common.Graph
import pl.rstanski.adventofcode2022.day12.common.GraphImpl
import pl.rstanski.adventofcode2022.day12.common.GridParser.parseGrid
import pl.rstanski.adventofcode2022.day12.common.PathFinder
import pl.rstanski.adventofcode2022.day12.common.PointFinder.findPoint
import pl.rstanski.adventofcode2022.day12.common.shortestPath

private const val PUZZLE_FILENAME = "day12.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day12Part1Solution.solve(puzzle)

    println(result)
}


object Day12Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val grid = parseGrid(puzzle)
        val start = findPoint(puzzle, 'S')
        val bestSignal = findPoint(puzzle, 'E')

        val graph = buildGraph(grid)
        val (path, value) = shortestPath(graph, start, bestSignal)
        println("value = $value, path = $path")

        return PathFinder(grid)
            .findShortestPathLength(start, bestSignal)
    }

    fun buildGraph(grid: Grid<Int>): Graph<Point, Int> {
        val graph = GraphImpl<Point, Int>(directed = true, defaultCost = 1)

        (0 until grid.xSize).forEach { x ->
            (0 until grid.ySize).forEach { y ->
                val point = Point(x, y)
                val pointValue = grid.getPoint(point)
                val toPointRight = grid.getPointOrNull(point.right())
                val toPointUp = grid.getPointOrNull(point.up())

                if (toPointRight != null) {
                    if (pointValue + 1 >= toPointRight) graph.addArc(point to point.right())
                    if (toPointRight + 1 >= pointValue) graph.addArc(point.right() to point)
                }

                if (toPointUp != null) {
                    if (pointValue + 1 >= toPointUp) graph.addArc(point to point.up())
                    if (toPointUp + 1 >= pointValue) graph.addArc(point.up() to point)
                }
            }
        }

        return graph
    }
}