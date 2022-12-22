package pl.rstanski.adventofcode2022.day22.common

import pl.rstanski.adventofcode2022.common.Point

object BoardParser {

    fun parseBoard(mapOfTheBoard: List<String>): Grid<Char> {
        val board = Grid<Char>()

        mapOfTheBoard.forEachIndexed { y, row ->
            row.forEachIndexed { x, column ->
                when (column) {
                    ' ' -> {}
                    else -> board.putPoint(x, y, column)
                }
            }
        }

        return board
    }
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

    fun getPointsX(x: Int): List<Point> {
        return points.keys.filter { it.x == x }.sortedBy { it.x }
    }

    fun getPointsY(y: Int): List<Point> {
        return points.keys.filter { it.y == y }.sortedBy { it.y }
    }

}

fun printGrid(grid: Grid<Char>, corner1: Point, corner2: Point) {
    (corner1.y..corner2.y).forEach { y ->
        print(y.toString().padStart(3))
        (corner1.x..corner2.x).forEach { x ->
            when (grid.getPoint(x, y)) {
                null -> print(" ")
                else -> print(grid.getPoint(x, y))
            }
        }
        println()
    }
}