package pl.rstanski.adventofcode2022.day12.common

import java.util.LinkedList
import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point

data class Node(
    val point: Point,
    val depth: Int
)

class PathFinder(private val grid: Grid<Int>) {

    //BFS
    fun findShortestPathLength(from: Point, to: Point): Int {
        val visited = mutableSetOf<Point>()
        val nodesToCheck = LinkedList<Node>()

        nodesToCheck.push(Node(from, 0))

        while (nodesToCheck.isNotEmpty()) {
            val nodeToCheck = nodesToCheck.poll()
            val currentPoint = nodeToCheck.point
            val depth = nodeToCheck.depth

            if (currentPoint == to) return depth

            if (currentPoint in visited) continue
            visited.add(currentPoint)

            val currentElevation = grid.getPoint(currentPoint)
            pointsAdjacentTo(currentPoint).forEach { (adjacentPoint, elevation) ->
                if (elevation != null && currentElevation + 1 >= elevation)
                    nodesToCheck.add(Node(adjacentPoint, depth + 1))
            }
        }

        throw IllegalStateException("Unable to find path from $from to $to")
    }

    private fun pointsAdjacentTo(currentPoint: Point): List<Pair<Point, Int?>> =
        listOf(currentPoint.left(), currentPoint.right(), currentPoint.up(), currentPoint.down()).map {
            it to grid.getPointOrNull(it)
        }
}