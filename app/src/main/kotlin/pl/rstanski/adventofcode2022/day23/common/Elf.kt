package pl.rstanski.adventofcode2022.day23.common

import pl.rstanski.adventofcode2022.common.Point

data class Elf(val index: Int, var position: Point) {

    private val propositions = listOf(Direction.north, Direction.south, Direction.west, Direction.east)
    private var propositionIndex = -1

    fun proposeMove(grid: Grid<Char>): Point? {
        val neighboursExists = (1..4)
            .map { propositionAt(propositionIndex + it) }
            .map { it to neighboursExistsAt(it, grid) }

        val proposedMove = if (neighboursExists.count { !it.second }  == 4 || neighboursExists.count { it.second }  == 4) {
            // no neighbours for all sides
            null
        } else {
            // propose for the first non-existing
            propositionFor(neighboursExists.find { !it.second }!!.first)
        }
        propositionIndex++

        return proposedMove
    }

    private fun propositionAt(index: Int): Direction {
        return propositions[index % propositions.size]
    }

    private fun propositionFor(direction: Direction): Point {
        return when (direction) {
            Direction.north -> position.down()
            Direction.south -> position.up()
            Direction.west -> position.left()
            Direction.east -> position.right()
        }
    }

    private fun neighboursExistsAt(direction: Direction, grid: Grid<Char>): Boolean {
        val neighbours = when (direction) {
            Direction.north -> (-1..1).map { Point(position.x + it, position.y - 1) }
            Direction.south -> (-1..1).map { Point(position.x + it, position.y + 1) }
            Direction.west -> (-1..1).map { Point(position.x - 1, position.y + it) }
            Direction.east -> (-1..1).map { Point(position.x + 1, position.y + it) }
        }
        return neighbours.any { grid.getPoint(it) != null }
    }


    fun makeMove(grid: Grid<Char>, proposedMove: Point) {
        grid.removePoint(position)
        position = proposedMove
        grid.putPoint(proposedMove, '#')
    }
}