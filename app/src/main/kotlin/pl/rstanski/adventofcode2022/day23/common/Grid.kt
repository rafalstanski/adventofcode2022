package pl.rstanski.adventofcode2022.day23.common

import pl.rstanski.adventofcode2022.common.Point

class Grid<T> {
    private val points = mutableMapOf<Point, T>()

    fun putPoint(point: Point, value: T) {
        points[point] = value
    }

    fun removePoint(point: Point) {
        points.remove(point)
    }

    fun putPoint(x: Int, y: Int, value: T) {
        putPoint(Point(x, y), value)
    }

    fun getPoint(point: Point): T? =
        points[point]

    fun getPoint(x: Int, y: Int): T? =
        getPoint(Point(x, y))


    fun countPoints() = points.size

    fun getArea(): Pair<Point, Point> {
        val min = Point(points.keys.minOf { it.x }, points.keys.minOf { it.y })
        val max = Point(points.keys.maxOf { it.x }, points.keys.maxOf { it.y })

        return min to max
    }
}

fun printGrid(grid: Grid<Char>, corner1: Point, corner2: Point) {
    (corner1.y..corner2.y).forEach { y ->
        print(y.toString().padStart(3))
        (corner1.x..corner2.x).forEach { x ->
            when (grid.getPoint(x, y)) {
                null -> print(".")
                else -> print(grid.getPoint(x, y))
            }
        }
        println()
    }
}