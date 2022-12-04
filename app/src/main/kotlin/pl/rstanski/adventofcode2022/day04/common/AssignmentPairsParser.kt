package pl.rstanski.adventofcode2022.day04.common

import pl.rstanski.adventofcode2022.common.Puzzle

object AssignmentPairsParser {

    fun parseAssignmentPairs(puzzle: Puzzle): List<SectionAssignmentPairs> =
        puzzle.lines
            .map { it.split(",") }
            .map {
                SectionAssignmentPairs(
                    firstSectionAssignment = SectionAssignmentParser.parseSectionAssignment(it[0]),
                    secondSectionAssignment = SectionAssignmentParser.parseSectionAssignment(it[1])
                )
            }
}

object SectionAssignmentParser {

    fun parseSectionAssignment(assignmentLine: String): SectionAssignment =
        assignmentLine.split("-")
            .let { SectionAssignment(it[0].toInt(), it[1].toInt()) }
}