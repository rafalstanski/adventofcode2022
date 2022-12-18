package pl.rstanski.adventofcode2022.day18.part2

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

    println("$minX, $minY, $minZ")
    println("$maxX, $maxY, $maxZ")
    val min = Position(minX, minY, minZ)
    val max = Position(maxX, maxY, maxZ)

    val airpockets = (minX + 1..maxX - 1).map { x ->
        (minY + 1..maxY - 1).map { y ->
            (minZ + 1..maxZ - 1).map { z ->
                AirCube(Position(x, y, z), min, max)
            }
        }
    }.flatten()
        .flatten()
        .filter { it.position !in positions }
        .filter { it.isAirPocket(positions) }

    val airPocketsPositions = airpockets.map { it.position }
    println(airPocketsPositions)

    val positionsWithAirPockets = positions + airPocketsPositions

    val cubes = positions.map { Cube(it) }
    return cubes.map { it.countUncoveredSides(positionsWithAirPockets) }.sumOf { it }
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

data class AirCube(val position: Position, val min: Position, val max: Position) {

    fun isAirPocket(positions: List<Position>): Boolean {
        val of = setOf(
            (position.x + 1..max.x).map { position.copy(x = it) }.find { it in positions },
            (min.x..position.x - 1).map { position.copy(x = it) }.find { it in positions },
            (position.y + 1..max.y).map { position.copy(y = it) }.find { it in positions },
            (min.y..position.y - 1).map { position.copy(y = it) }.find { it in positions },
            (position.z + 1..max.z).map { position.copy(z = it) }.find { it in positions },
            (min.z..position.z - 1).map { position.copy(z = it) }.find { it in positions }
        )
        if (position == Position(2, 2, 5)) {
            println("checking airpocket: $this: $of")
            println(position !in positions)
        }
        return of.toList().size == 6
    }
}

fun parse(line: String): Position {
    val parts = line.split(",").toInts()
    return Position(parts[0], parts[1], parts[2])
}

data class Position(val x: Int, val y: Int, val z: Int)