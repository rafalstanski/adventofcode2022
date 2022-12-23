package pl.rstanski.adventofcode2022.day23.part1

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day23.common.Elf
import pl.rstanski.adventofcode2022.day23.common.Grid
import pl.rstanski.adventofcode2022.day23.common.printGrid

fun main() {
    val testSolution = solvePart1(load("day23sample.txt"))
    println("test solution: $testSolution")
    check(testSolution == 110)

    val solution = solvePart1(load("day23.txt"))
    println("solution: $solution")
}

private fun solvePart1(puzzle: Puzzle): Any {
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

    repeat(10) { round ->
        val proposedMoves = elfs.associate { it.index to it.proposeMove(grid) }

        proposedMoves.forEach { (elfIndex, proposedMove) ->
            if (proposedMove != null && proposedMoves.values.count { it == proposedMove } == 1) {
                elfs[elfIndex].makeMove(grid, proposedMove)
            }
        }
    }

    val (min, max) = grid.getArea()
    val allPoints = (max.x - min.x + 1) * (max.y - min.y + 1)

    println("$min $max $allPoints")
    println("${grid.countPoints()}")

    return allPoints - grid.countPoints()
}