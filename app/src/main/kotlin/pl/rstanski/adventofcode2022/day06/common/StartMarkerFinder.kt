package pl.rstanski.adventofcode2022.day06.common

class StartMarkerFinder(private val preambleSize: Int) {

    fun findMarker(signal: String): Int {
        return findIndexOfFirstCharacterAfterPreambleIn(signal)
    }

    private fun findIndexOfFirstCharacterAfterPreambleIn(signal: String): Int =
        requireNotNull(findPreambleHavingOnlyDistinctCharacters(signal)) { "There is no preamble" }
            .endIndex + 1

    private fun findPreambleHavingOnlyDistinctCharacters(signal: String): Preamble? =
        signal.toList()
            .withIndex()
            .windowed(preambleSize, 1)
            .find { it.map(IndexedValue<Char>::value).hasDistinctCharacters() }
            ?.let { Preamble(it.first().index, it.last().index) }
}

private fun List<Char>.hasDistinctCharacters(): Boolean =
    this.toSet().size == this.size

private data class Preamble(
    val startIndex: Int,
    val endIndex: Int
)
