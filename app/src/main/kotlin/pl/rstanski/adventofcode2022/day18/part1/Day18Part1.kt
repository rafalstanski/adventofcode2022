package pl.rstanski.adventofcode2022.day18.part1

import kotlin.math.absoluteValue
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.common.toInts

fun main() {
    val testSolution = solvePart1(load("day18sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 64)

    val solution = solvePart1(load("day18.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
    val positions = puzzle.lines.map { parse(it) }

    val cubes = positions.map { Cube(it) }
    return cubes.map { it.countUncoveredSides(positions) }.sumOf { it }
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

data class Position(val x: Int, val y: Int, val z: Int)
