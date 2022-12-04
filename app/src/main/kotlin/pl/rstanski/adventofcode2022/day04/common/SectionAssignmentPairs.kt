package pl.rstanski.adventofcode2022.day04.common

data class SectionAssignmentPairs(
    val firstSectionAssignment: SectionAssignment,
    val secondSectionAssignment: SectionAssignment
) {

    fun hasAssignmentContainsOtherOne(): Boolean =
        firstSectionAssignment.contains(secondSectionAssignment) ||
                secondSectionAssignment.contains(firstSectionAssignment)

    fun hasAssignmentsOverlapping(): Boolean =
        firstSectionAssignment.overlaps(secondSectionAssignment)
}