package pl.rstanski.adventofcode2022.day23.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day23.part2.Direction.east
import pl.rstanski.adventofcode2022.day23.part2.Direction.north
import pl.rstanski.adventofcode2022.day23.part2.Direction.south
import pl.rstanski.adventofcode2022.day23.part2.Direction.west

fun main() {
    val testSolution = solvePart2(PuzzleLoader.load("day23sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 20)

    val solution = solvePart2(PuzzleLoader.load("day23.txt"))
    println("solution: $solution")
}

private fun solvePart2(puzzle: Puzzle): Any {
    val grid = Grid<Char>()
    val elfs = mutableListOf<Elf>()

    puzzle.lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, dot ->
            when (dot) {
                '#' -> {
                    val position = Point(x, y)
                    grid.putPoint(x, y, '#')
                    elfs += Elf(elfs.size, position)
                }
            }
        }
    }
    printGrid(grid, Point(0, 0), Point(20, 12))

    var round = 0
    while (true) {
        round++
        val proposedMoves = elfs.associate { it.index to it.proposeMove(grid) }

        if (proposedMoves.values.all { it == null }) break

        proposedMoves.forEach { (elfIndex, proposedMove) ->
            if (proposedMove != null && proposedMoves.values.count { it == proposedMove } == 1) {
                elfs[elfIndex].makeMove(grid, proposedMove)
            }
        }
    }

    return round
}

enum class Direction {
    north, south, west, east
}

data class Elf(val index: Int, var position: Point) {

    private val propositions = listOf(north, south, west, east)
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
            north -> position.down()
            south -> position.up()
            west -> position.left()
            east -> position.right()
        }
    }

    private fun neighboursExistsAt(direction: Direction, grid: Grid<Char>): Boolean {
        val neighbours = when (direction) {
            north -> (-1..1).map { Point(position.x + it, position.y - 1) }
            south -> (-1..1).map { Point(position.x + it, position.y + 1) }
            west -> (-1..1).map { Point(position.x - 1, position.y + it) }
            east -> (-1..1).map { Point(position.x + 1, position.y + it) }
        }
        return neighbours.any { grid.getPoint(it) != null }
    }


    fun makeMove(grid: Grid<Char>, proposedMove: Point) {
        grid.removePoint(position)
        position = proposedMove
        grid.putPoint(proposedMove, '#')
    }
}


class Grid<T> {
    private val points = mutableMapOf<Point, T>()

    fun putPoint(point: Point, value: T) {
        points[point] = value
    }

    fun removePoint(point: Point) {
        points.remove(point)
    }

    fun putPoint(x: Int, y: Int, value: T) {
        putPoint(Point(x, y), value)
    }

    fun getPoint(point: Point): T? =
        points[point]

    fun getPoint(x: Int, y: Int): T? =
        getPoint(Point(x, y))


    fun countPoints() = points.size

    fun getArea(): Pair<Point, Point> {
        val min = Point(points.keys.minOf { it.x }, points.keys.minOf { it.y })
        val max = Point(points.keys.maxOf { it.x }, points.keys.maxOf { it.y })

        return min to max
    }
}

fun printGrid(grid: Grid<Char>, corner1: Point, corner2: Point) {
    (corner1.y..corner2.y).forEach { y ->
        print(y.toString().padStart(3))
        (corner1.x..corner2.x).forEach { x ->
            when (grid.getPoint(x, y)) {
                null -> print(".")
                else -> print(grid.getPoint(x, y))
            }
        }
        println()
    }
}