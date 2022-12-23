package pl.rstanski.adventofcode2022.day23.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day23.common.Elf
import pl.rstanski.adventofcode2022.day23.common.Grid
import pl.rstanski.adventofcode2022.day23.common.printGrid

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