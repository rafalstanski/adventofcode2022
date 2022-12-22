package pl.rstanski.adventofcode2022.day22.common

import pl.rstanski.adventofcode2022.common.Point

object PasswordCalculator {
    fun calculatePassword(currentPosition: Point, facingDirection: Point): Long =
        1000L * (currentPosition.y + 1) + 4L * (currentPosition.x + 1) + facingDirection.directionAsNumber()

    private fun Point.directionAsNumber(): Int {
        val rotate = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))

        return rotate.indexOf(this)
    }
}