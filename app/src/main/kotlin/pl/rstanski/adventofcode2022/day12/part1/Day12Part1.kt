package pl.rstanski.adventofcode2022.day12.part1

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
        val pointsRows = puzzle.lines

        val grid = Grid(pointsRows.first().length, pointsRows.size)
        var start: Point = Point(0, 0)
        var bestSignal: Point = Point(0, 0)

        pointsRows.forEachIndexed { indexY: Int, pointsRow: String ->
            pointsRow.toList().forEachIndexed { indexX: Int, height: Char ->
                var elevation:Char = height

                if (height == 'S') {
                    //start
                    start = Point(indexX, grid.ySize - 1 - indexY)
                    elevation = 'a'

                    println("Start point: $start - $elevation")
                }

                if (height == 'E') {
                    //best signal
                    bestSignal = Point(indexX, grid.ySize - 1 - indexY)
                    grid.bestSignal = bestSignal
                    elevation = 'z'

                    println("best signal: $bestSignal - $elevation")
                }

                grid.putPoint(
                    x = indexX,
                    y = grid.ySize - 1 - indexY,
                    height = elevation.code
                )
            }
        }

        grid.findPathFrom(start, mutableSetOf())

        println(grid.paths)

        return grid.paths.minOf { it }
    }

}


data class Point(val x: Int, val y: Int)

class Grid(val xSize: Int, val ySize: Int) {

    private val points = MutableList(xSize) { MutableList(ySize) { 0 } }
    var bestSignal: Point = Point(0, 0)
    val paths: MutableSet<Int> = mutableSetOf()

    fun putPoint(x: Int, y: Int, height: Int) {
        points[x][y] = height
    }

    fun findPathFrom(start: Point, visitedPoints: Set<Point>) {
        if (start == bestSignal) {
            println("found")
            paths += visitedPoints.size
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

        if (left != null && current + 1 >= left) findPathFrom(Point(start.x - 1, start.y), visitedPoints + start)
        if (right != null && current + 1 >= right) findPathFrom(Point(start.x + 1, start.y), visitedPoints + start)
        if (down != null && current + 1 >= down) findPathFrom(Point(start.x, start.y - 1), visitedPoints + start)
        if (up != null && current + 1 >= up) findPathFrom(Point(start.x, start.y + 1), visitedPoints + start)
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