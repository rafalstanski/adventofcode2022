package pl.rstanski.adventofcode2022.day12.part1

import java.util.LinkedList
import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day12.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day12Part1Solution.solve(puzzle)

    println(result)
}


object Day12Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val grid = GridParser.parseGrid(puzzle)
        val start = PointFinder.findPoint(puzzle, 'S')
        val bestSignal = PointFinder.findPoint(puzzle, 'E')

        return PathFinder(grid)
            .findShortestPath(start, bestSignal)
    }

}

object PointFinder {

    fun findPoint(puzzle: Puzzle, point: Char): Point {
        puzzle.lines.forEachIndexed { indexY: Int, row: String ->
            row.toList().forEachIndexed { indexX: Int, height: Char ->
                if (height == point) {
                    return Point(
                        x = indexX,
                        y = puzzle.lines.size - 1 - indexY,
                    )
                }
            }
        }
        throw IllegalStateException("Unable to find point $point")
    }
}


object GridParser {

    fun parseGrid(puzzle: Puzzle): Grid<Int> {
        val rows: List<String> = puzzle.lines

        val grid = prepareEmptyGridOfSizeBasedOn(rows)
        populateGrid(grid, rows)

        return grid

    }

    private fun prepareEmptyGridOfSizeBasedOn(treesRows: List<String>): Grid<Int> =
        Grid(xSize = treesRows.first().length, ySize = treesRows.size) { 0 }

    private fun populateGrid(grid: Grid<Int>, rows: List<String>) {
        rows.forEachIndexed { indexY: Int, row: String ->
            row.toList().forEachIndexed { indexX: Int, height: Char ->
                val elevation: Int = calculateElevation(height)

                grid.putPoint(
                    x = indexX,
                    y = grid.ySize - 1 - indexY,
                    value = elevation
                )
            }
        }
    }

    private fun calculateElevation(height: Char) = when (height) {
        'S' -> 'a'
        'E' -> 'z'
        else -> height
    }.code
}

data class Node(
    val point: Point,
    val depth: Int
)

class PathFinder(private val grid: Grid<Int>) {

    fun findShortestPath(from: Point, to: Point): Int {
        val visited = mutableSetOf<Point>()
        val nodesToCheck = LinkedList<Node>()

        nodesToCheck.push(Node(from, 0))

        while (nodesToCheck.isNotEmpty()) {
            val nodeToCheck = nodesToCheck.poll()
            val currentPoint = nodeToCheck.point
            val depth = nodeToCheck.depth

            if (currentPoint == to) return depth

            if (currentPoint in visited) continue
            visited.add(currentPoint)

            val left = grid.getPointOrNullIfOutOfGrid(currentPoint.left())
            val right = grid.getPointOrNullIfOutOfGrid(currentPoint.right())
            val down = grid.getPointOrNullIfOutOfGrid(currentPoint.down())
            val up = grid.getPointOrNullIfOutOfGrid(currentPoint.up())

            val currentElevation = grid.getPoint(currentPoint)

            if (left != null && currentElevation + 1 >= left) nodesToCheck.add(Node(currentPoint.left(), depth + 1))
            if (right != null && currentElevation + 1 >= right) nodesToCheck.add(Node(currentPoint.right(), depth + 1))
            if (down != null && currentElevation + 1 >= down) nodesToCheck.add(Node(currentPoint.down(), depth + 1))
            if (up != null && currentElevation + 1 >= up) nodesToCheck.add(Node(currentPoint.up(), depth + 1))
        }

        throw IllegalStateException("Unable to find path from $from to $to")
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