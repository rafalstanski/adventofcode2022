package pl.rstanski.adventofcode2022.day09.common

import kotlin.math.absoluteValue
import kotlin.math.sign

class Rope(knotsCount: Int) {
    private val knots: List<Knot>

    init {
        require(knotsCount >= 2)
        knots = List(knotsCount) { Knot() }
    }

    fun tailPosition(): Position =
        knots.last().currentPosition()

    fun moveHeadRight() {
        head.moveBy(1, 0)
        moveTailAlong()
    }

    fun moveHeadLeft() {
        head.moveBy(-1, 0)
        moveTailAlong()
    }

    fun moveHeadUp() {
        head.moveBy(0, 1)
        moveTailAlong()
    }

    fun moveHeadDown() {
        head.moveBy(0, -1)
        moveTailAlong()
    }

    private val head: Knot
        get() = knots.first()

    private fun moveTailAlong() {
        knots.drop(1).forEachIndexed { previousKnotIndex, tailKnot ->
            tailKnot.moveNearTo(knots[previousKnotIndex])
        }
    }
}

class Knot {
    private var position = Position(0, 0)

    fun currentPosition() = position

    fun moveBy(x: Int, y: Int) {
        position = Position(position.x + x, position.y + y)
    }

    fun moveNearTo(otherKnot: Knot) {
        while (!position.overlapsOrNearTo(otherKnot.position)) {
            val xDelta = otherKnot.position.x - position.x
            val yDelta = otherKnot.position.y - position.y

            moveBy(
                x = xDelta.sign, // if (xDelta.absoluteValue > 0) 1 * xDelta.sign else 0
                y = yDelta.sign // if (yDelta.absoluteValue > 0) 1 * yDelta.sign else 0
            )
        }
    }
}

data class Position(
    val x: Int,
    val y: Int
) {

    fun overlapsOrNearTo(otherPosition: Position): Boolean =
        (x - otherPosition.x).absoluteValue <= 1 && (y - otherPosition.y).absoluteValue <= 1
}