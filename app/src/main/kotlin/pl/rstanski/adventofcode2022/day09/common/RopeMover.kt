package pl.rstanski.adventofcode2022.day09.common


class RopeMoverMemorisingTailPosition(private val rope: Rope) {

    private val ropeMover = RopeMover(rope)

    fun moveAndReturnTailPositionsUsing(moves: List<Move>): Set<Position> {
        val tailPositions = mutableSetOf<Position>()
        tailPositions.add(rope.tailPosition())

        ropeMover.moveUsing(moves) {
            tailPositions.add(it.tailPosition())
        }

        return tailPositions
    }
}

class RopeMover(private val rope: Rope) {

    fun moveUsing(moves: List<Move>, ropeAfterEachMove: (Rope) -> Unit = {}) {
        moves.forEach { move ->
            repeat(move.value) {
                moveRopeInDirectionOf(move.direction)
                ropeAfterEachMove(rope)
            }
        }
    }

    private fun moveRopeInDirectionOf(direction: Direction) {
        when (direction) {
            Direction.LEFT -> rope.moveHeadLeft()
            Direction.RIGHT -> rope.moveHeadRight()
            Direction.UP -> rope.moveHeadUp()
            Direction.DOWN -> rope.moveHeadDown()
        }
    }

}