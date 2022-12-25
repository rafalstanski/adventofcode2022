package pl.rstanski.adventofcode2022.day24.part2

import java.util.LinkedList
import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day24.common.Blizzard
import pl.rstanski.adventofcode2022.day24.common.BlizzardParser.parseBlizzard
import pl.rstanski.adventofcode2022.day24.common.Boundaries
import pl.rstanski.adventofcode2022.day24.common.Grid
import pl.rstanski.adventofcode2022.day24.common.printGrid
import pl.rstanski.adventofcode2022.day24.common.toChar

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day24sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 54)

    val solution = solvePart2(PuzzleLoader.load("day24.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val blizzards = parseBlizzard(puzzle)

    val maxX = puzzle.lines.first().length - 1
    val maxY = puzzle.lines.size - 1
    val start = Point(1, 0)
    val exit = Point(maxX - 1, maxY)

    val step1 = ExitAlgorithm(Boundaries(maxX, maxY, exit)).find(0, start, exit, blizzards)
    val step2 = ExitAlgorithm(Boundaries(maxX, maxY, start)).find(step1.first, exit, start, step1.second)
    val step3 = ExitAlgorithm(Boundaries(maxX, maxY, exit)).find(step2.first, start, exit, step2.second)

    return step3.first
}

data class StateKey(val movesCount: Int, val currentPosition: Point, val myNexMove: Point)

data class State(val blizzards: List<Blizzard>, val currentPosition: Point, val myNexMove: Point, val movesCount: Int) {
    fun toKey() = StateKey(movesCount, currentPosition, myNexMove)
}

class ExitAlgorithm(private val boundaries: Boundaries) {

    fun find(movesCount: Int, startPoint: Point, exitPoint: Point, blizzards: List<Blizzard>): Pair<Int, List<Blizzard>> {
        val initState = State(blizzards.toList(), startPoint, startPoint, movesCount)
        val statesToRun = LinkedList<State>()
        statesToRun.add(initState)

        val statesVisited = mutableSetOf<StateKey>()

        while (statesToRun.isNotEmpty()) {
            val state = statesToRun.poll()
//            printState(state)

            if (state.toKey() in statesVisited) {
                continue
            } else {
                statesVisited += state.toKey()
            }

            if (state.myNexMove == exitPoint) {
                println("found exit: ${state.movesCount}")
                return state.movesCount to state.blizzards
            } else {
                val nextStates = runState(state)
                nextStates.forEach { statesToRun.add(it) }
            }
        }

        throw IllegalStateException()
    }

    private fun runState(state: State): List<State> {
        val currentPosition = state.myNexMove
        val blizzards = state.blizzards
        val moveCount = state.movesCount

        val blizzardsAfterMove = blizzards.map { blizzard -> blizzard.move(boundaries) }
        val possibleMoves = proposeMoves(currentPosition, blizzardsAfterMove)

        val possibleNextStates = possibleMoves.map { State(blizzardsAfterMove, currentPosition, it, moveCount + 1) }
        val stayInPlace = when (currentPosition in blizzardsAfterMove.map { it.position }) {
            true -> emptyList()
            else -> listOf(State(blizzardsAfterMove, currentPosition, currentPosition, moveCount + 1))
        }

        return possibleNextStates + stayInPlace
    }

    private fun proposeMoves(position: Point, blizzards: List<Blizzard>): List<Point> {
        return neighbours(position)
            .filter { isGround(it, blizzards, boundaries) }
    }

    private fun isGround(position: Point, blizzards: List<Blizzard>, boundaries: Boundaries): Boolean {
        return blizzards.none { it.position == position } && boundaries.isInside(position)
    }

    private fun neighbours(position: Point): List<Point> {
        return listOf(position.up(), position.right(), position.down(), position.left())
    }

    private fun printState(state: State) {
        val grid = Grid<Char>()

        (0..boundaries.maxX).map { Point(it, 0) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxX).map { Point(it, boundaries.maxY) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxY).map { Point(0, it) }
            .forEach { grid.putPoint(it, '#') }
        (0..boundaries.maxY).map { Point(boundaries.maxX, it) }
            .forEach { grid.putPoint(it, '#') }

        state.blizzards.forEach { grid.putPoint(it.position, it.direction.toChar()) }

        grid.putPoint(state.myNexMove, 'E')

        println("****** ${state.movesCount}  *****")
        printGrid(grid, Point(0, 0), Point(10, 7))
    }
}