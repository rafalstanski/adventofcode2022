package pl.rstanski.adventofcode2022.day12.part1

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day12.common.GridParser.parseGrid
import pl.rstanski.adventofcode2022.day12.common.PathFinder
import pl.rstanski.adventofcode2022.day12.common.PointFinder.findPoint

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

        return PathFinder(grid)
            .findShortestPathLength(start, bestSignal)
    }
}

class Grid2(val xSize: Int, val ySize: Int) {

    private val points = MutableList(xSize) { MutableList(ySize) { 0 } }
    var bestSignal: Point = Point(0, 0)
    val paths: MutableSet<Int> = mutableSetOf()
    val pathsDetails: MutableMap<Int, Set<Point>> = mutableMapOf()
    val graph = GraphImpl<Point, Int>(directed = true, defaultCost = 1)

    fun putPoint(x: Int, y: Int, height: Int) {
        points[x][y] = height
    }

    fun printPoint(path: Set<Point>) {
        (0 until ySize).forEach { y ->
            (0 until xSize).forEach { x ->
                val point = Point(x, ySize - 1 - y)
                if (point in path) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }


    }

    fun buildGraph() {
        (0 until xSize).forEach { x ->
            (0 until ySize).forEach { y ->
                val fromPoint = getPoint(x, y)!!
                val toPointRight = getPoint(x + 1, y)
                val toPointUp = getPoint(x, y + 1)
                if (toPointRight != null) {
                    when {
                        fromPoint == toPointRight -> {
                            graph.addArc(Point(x, y) to Point(x + 1, y), 1)
                            graph.addArc(Point(x + 1, y) to Point(x, y), 1)
                        }

                        fromPoint + 1 == toPointRight -> graph.addArc(Point(x, y) to Point(x + 1, y), 1)
                        fromPoint == toPointRight + 1 -> graph.addArc(Point(x + 1, y) to Point(x, y), 1)
                    }
                }

                if (toPointUp != null) {
                    when {
                        fromPoint == toPointUp -> {
                            graph.addArc(Point(x, y) to Point(x, y + 1), 1)
                            graph.addArc(Point(x, y + 1) to Point(x, y), 1)
                        }

                        fromPoint + 1 == toPointUp -> graph.addArc(Point(x, y) to Point(x, y + 1), 1)
                        fromPoint == toPointUp + 1 -> graph.addArc(Point(x, y + 1) to Point(x, y), 1)
                    }
                }
            }
        }
    }

    fun findPathFrom(start: Point, visitedPoints: MutableSet<Point>, depth: Int) {
        if (start == bestSignal) {
            println("found")
            paths += depth
            pathsDetails[depth] = visitedPoints
            return
        }

        if (start in visitedPoints) {
            // we've been here
            return
        }

        val left = getPoint(start.x - 1, start.y)
        val right = getPoint(start.x + 1, start.y)
        val down = getPoint(start.x, start.y - 1)
        val up = getPoint(start.x, start.y + 1)

        val current = getPoint(start.x, start.y)!!

        visitedPoints += start

        if (left != null && current + 1 >= left) findPathFrom(Point(start.x - 1, start.y), visitedPoints, depth + 1)
        if (right != null && current + 1 >= right) findPathFrom(Point(start.x + 1, start.y), visitedPoints, depth + 1)
        if (down != null && current + 1 >= down) findPathFrom(Point(start.x, start.y - 1), visitedPoints, depth + 1)
        if (up != null && current + 1 >= up) findPathFrom(Point(start.x, start.y + 1), visitedPoints, depth + 1)
    }

    fun getPoint(point: Point): Int? {
        return getPoint(point.x, point.y)
    }

    fun getPoint(x: Int, y: Int): Int? {
        return if (x == -1 || x == xSize || y == -1 || y == ySize)
            return null
        else
            return points[x][y]
    }
}