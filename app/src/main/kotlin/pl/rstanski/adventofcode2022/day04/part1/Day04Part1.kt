package pl.rstanski.adventofcode2022.day04.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day04.common.AssignmentPairsParser
import pl.rstanski.adventofcode2022.day04.common.SectionAssignmentPairs

private const val PUZZLE_FILENAME = "day04.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day04Part1Solution.solve(puzzle)

    println(result)
}

object Day04Part1Solution {

    fun solve(puzzle: Puzzle): Int {
        val assignmentPairs = AssignmentPairsParser.parseAssignmentPairs(puzzle)

        return assignmentPairs.count(SectionAssignmentPairs::hasAssignmentContainsOtherOne)
    }
}