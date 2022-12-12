package pl.rstanski.adventofcode2022.common

class Grid<T>(
    val xSize: Int,
    val ySize: Int,
    init: () -> T
) {

    private val points = MutableList(xSize) { MutableList(ySize) { init() } }

    fun putPoint(point: Point, value: T) {
        putPoint(point.x, point.y, value)
    }

    fun putPoint(x: Int, y: Int, value: T) {
        points[x][y] = value
    }

    fun getPoint(point: Point): T =
        getPoint(point.x, point.y)

    fun getPoint(x: Int, y: Int): T =
        getPointOrNullIfOutOfGrid(x, y)
            ?: throw PointOutOfGridException(Point(x, y))

    fun getPointOrNullIfOutOfGrid(point: Point): T? =
        getPointOrNullIfOutOfGrid(point.x, point.y)

    fun getPointOrNullIfOutOfGrid(x: Int, y: Int): T? =
        if (x < 0 || x >= xSize || y < 0 || y >= ySize)
            null
        else
            points[x][y]
}

data class Point(
    val x: Int,
    val y: Int
) {

    fun left() = Point(x - 1, y)
    fun right() = Point(x + 1, y)
    fun up() = Point(x, y + 1)
    fun down() = Point(x, y - 1)
}

class PointOutOfGridException(point: Point) : IllegalArgumentException("Point $point is out of grid")
