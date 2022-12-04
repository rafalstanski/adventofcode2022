package pl.rstanski.adventofcode2022.day04.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day04.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day04Part1Solution.solve(puzzle)

    println(result)
}

object Day04Part1Solution {

    fun solve(puzzle: Puzzle): Int {
        val assignmentPairs = SectionAssignmentPairsParser.parse(puzzle)

        return assignmentPairs.count { it.hasAssignmentsOverlapping() }
    }
}

object SectionAssignmentPairsParser {

    fun parse(puzzle: Puzzle): List<SectionAssignmentPairs> {
        return puzzle.lines.map {
            it.split(",")
        }.map {
            SectionAssignmentPairs(
                firstAssignedSections = parseAssignedSections(it[0]),
                secondAssignedSections = parseAssignedSections(it[1])
            )
        }
    }

    private fun parseAssignedSections(assignedSectionsDef: String): AssignedSections {
        val sections = assignedSectionsDef.split("-")
        return AssignedSections(sections[0].toInt(), sections[1].toInt())
    }
}

data class SectionAssignmentPairs(
    val firstAssignedSections: AssignedSections,
    val secondAssignedSections: AssignedSections
) {

    fun hasAssignmentsOverlapping(): Boolean =
        firstAssignedSections.contains(secondAssignedSections) ||
                secondAssignedSections.contains(firstAssignedSections)
}

data class AssignedSections(
    val startSection: Int,
    val endSection: Int
) {
    fun contains(assignedSections: AssignedSections): Boolean =
        startSection <= assignedSections.startSection &&
                endSection >= assignedSections.endSection
}