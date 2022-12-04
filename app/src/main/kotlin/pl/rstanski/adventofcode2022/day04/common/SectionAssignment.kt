package pl.rstanski.adventofcode2022.day04.common

data class SectionAssignment(
    val startSection: Int,
    val endSection: Int
) {
    fun contains(sectionAssignment: SectionAssignment): Boolean =
        startSection <= sectionAssignment.startSection &&
                endSection >= sectionAssignment.endSection

    fun overlaps(sectionAssignment: SectionAssignment): Boolean =
        (asRange() intersect sectionAssignment.asRange())
            .isNotEmpty()

    private fun asRange(): IntRange =
        startSection..endSection
}