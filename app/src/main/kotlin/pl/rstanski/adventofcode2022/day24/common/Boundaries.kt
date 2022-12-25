package pl.rstanski.adventofcode2022.day24.common

import pl.rstanski.adventofcode2022.common.Point

data class Boundaries(val maxX: Int, val maxY: Int, val endPosition: Point) {
    fun isOutside(position: Point): Boolean {
        return position.x <= 0 || position.y <= 0 || position.x >= maxX || position.y >= maxY
    }

    fun isInside(position: Point): Boolean {
        return position == endPosition || !isOutside(position)
    }
}