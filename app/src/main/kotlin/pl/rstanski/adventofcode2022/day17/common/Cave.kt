package pl.rstanski.adventofcode2022.day17.common

import pl.rstanski.adventofcode2022.common.Point

class Cave {

    private val points = mutableMapOf<Point, Int>()
    var top: Int = -1

    fun putPoint(point: Point, value: Int) {
        points[point] = value
        if (point.y > top) top = point.y
    }

    fun putPoint(x: Int, y: Int, value: Int) {
        putPoint(Point(x, y), value)
    }

    fun getPoint(point: Point): Int? =
        points[point]

    fun getPoint(x: Int, y: Int): Int? =
        getPoint(Point(x, y))


}

fun printGrid(cave: Cave, corner1: Point, corner2: Point) {
//    (corner1.y..corner2.y).forEach { y ->
    (corner2.y downTo corner1.y).forEach { y ->
        print(y.toString().padStart(3))
        (corner1.x..corner2.x).forEach { x ->
            when (cave.getPoint(x, y)) {
                1 -> print("#")
                else -> print(".")
            }
        }
        println()
    }
}