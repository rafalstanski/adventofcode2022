package pl.rstanski.adventofcode2022.day12.common

import java.util.LinkedList
import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Point

data class Node(
    val point: Point,
    val depth: Int
)

class PathFinder(private val grid: Grid<Int>) {

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

            val left = grid.getPointOrNull(currentPoint.left())
            val right = grid.getPointOrNull(currentPoint.right())
            val down = grid.getPointOrNull(currentPoint.down())
            val up = grid.getPointOrNull(currentPoint.up())

            val currentElevation = grid.getPoint(currentPoint)

            if (left != null && currentElevation + 1 >= left) nodesToCheck.add(Node(currentPoint.left(), depth + 1))
            if (right != null && currentElevation + 1 >= right) nodesToCheck.add(Node(currentPoint.right(), depth + 1))
            if (down != null && currentElevation + 1 >= down) nodesToCheck.add(Node(currentPoint.down(), depth + 1))
            if (up != null && currentElevation + 1 >= up) nodesToCheck.add(Node(currentPoint.up(), depth + 1))
        }

        throw IllegalStateException("Unable to find path from $from to $to")
    }
}