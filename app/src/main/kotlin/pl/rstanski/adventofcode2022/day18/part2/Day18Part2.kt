package pl.rstanski.adventofcode2022.day18.part2

import java.util.LinkedList
import kotlin.math.absoluteValue
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.toInts

fun main() {
    val testSolution = solvePart2(load("day18sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 58)

    val solution = solvePart2(load("day18.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val positions = puzzle.lines.map { parse(it) }

    val minX = positions.minOf { it.x }
    val minY = positions.minOf { it.y }
    val minZ = positions.minOf { it.z }
    val maxX = positions.maxOf { it.x }
    val maxY = positions.maxOf { it.y }
    val maxZ = positions.maxOf { it.z }

    val min = Position(minX, minY, minZ)
    val max = Position(maxX, maxY, maxZ)

    println("min: $min, max: $max")

    val start = min - Position(1, 1, 1)
    val visited = mutableSetOf<Position>()
    val reachableAir = mutableSetOf<Position>()
    val toCheckQueue = LinkedList<Position>()

    toCheckQueue.add(start)

    while (toCheckQueue.isNotEmpty()) {
        val toCheck = toCheckQueue.poll()

        if (toCheck in visited) continue
        visited.add(toCheck)

        if (toCheck in positions) continue

        reachableAir += toCheck

        toCheck.neigbours().filter {
            it !in positions
                    && it.x >= min.x - 1 && it.x <= max.x + 1
                    && it.y >= min.y - 1 && it.y <= max.y + 1
                    && it.z >= min.z - 1 && it.z <= max.z + 1
        }.forEach {
            toCheckQueue.add(it)
        }
    }

    val allAir =
        (minX..maxX).map { x ->
            (minY..maxY).map { y ->
                (minZ..maxZ).map { z ->
                    Position(x, y, z)
                }
            }
        }.flatten().flatten().filter { it !in positions }

    val airPockets = allAir - reachableAir
    println(airPockets)

    val cubes = positions.map { Cube(it) }
    return cubes.map { it.countUncoveredSides(positions + airPockets) }.sumOf { it }
}

data class Cube(val position: Position) {

    fun countUncoveredSides(positions: List<Position>): Int {
        val potentialNeighbors = positions - position
        val neighbors = potentialNeighbors.filter {
            (it.x - position.x).absoluteValue <= 1 &&
                    (it.y - position.y).absoluteValue <= 1 &&
                    (it.z - position.z).absoluteValue <= 1
        }

        return 6 - neighbors.count { neighbor ->
            onLeftOrRight(neighbor) || onTopOrBottom(neighbor) || onFrontOrBack(neighbor)
        }
    }

    private fun onLeftOrRight(neighbor: Position): Boolean =
        (neighbor.x - position.x).absoluteValue == 1
                && (neighbor.y - position.y).absoluteValue == 0
                && (neighbor.z - position.z).absoluteValue == 0

    private fun onTopOrBottom(neighbor: Position): Boolean =
        (neighbor.x - position.x).absoluteValue == 0
                && (neighbor.y - position.y).absoluteValue == 1
                && (neighbor.z - position.z).absoluteValue == 0

    private fun onFrontOrBack(neighbor: Position): Boolean =
        (neighbor.x - position.x).absoluteValue == 0
                && (neighbor.y - position.y).absoluteValue == 0
                && (neighbor.z - position.z).absoluteValue == 1

}

fun parse(line: String): Position {
    val parts = line.split(",").toInts()
    return Position(parts[0], parts[1], parts[2])
}

data class Position(val x: Int, val y: Int, val z: Int) {
    fun left() = this.copy(x = x - 1)
    fun right() = this.copy(x = x + 1)
    fun bottom() = this.copy(y = y - 1)
    fun top() = this.copy(y = y + 1)
    fun front() = this.copy(z = z - 1)
    fun back() = this.copy(z = z + 1)

    fun neigbours(): List<Position> {
        return listOf(
            left(),
            right(),
            top(),
            bottom(),
            back(),
            front()
        )
    }

    operator fun minus(otherPosition: Position): Position =
        Position(x - otherPosition.x, y - otherPosition.y, z - otherPosition.z)
}