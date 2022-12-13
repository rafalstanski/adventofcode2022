package pl.rstanski.adventofcode2022.day13.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day13.common.ORDER.NONE
import pl.rstanski.adventofcode2022.day13.common.ORDER.NOT_RIGHT_ORDER
import pl.rstanski.adventofcode2022.day13.common.ORDER.RIGHT_ORDER
import pl.rstanski.adventofcode2022.day13.common.PacketComparator
import pl.rstanski.adventofcode2022.day13.common.PacketParser

private const val PUZZLE_FILENAME = "day13.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day13Part2Solution.solve(puzzle)

    println(result)
}



object Day13Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val dividerPacket2 = "[[2]]"
        val dividerPacket6 = "[[6]]"
        val packetsLines = puzzle.lines.filter(String::isNotBlank) + dividerPacket2 + dividerPacket6

        val sortedPackets = packetsLines.map(PacketParser::parsePacket)
            .sortedWith { a, b ->
                when (PacketComparator.compare(a, b)) {
                    RIGHT_ORDER -> -1
                    NONE -> 0
                    NOT_RIGHT_ORDER -> 1
                }
            }
        val sortedPacketsAsStrings = sortedPackets.map(List<Any>::packetAsString)

        val dividerPacket2Position = sortedPacketsAsStrings.indexOf(dividerPacket2) + 1
        val dividerPacket6Position = sortedPacketsAsStrings.indexOf(dividerPacket6) + 1

        return dividerPacket2Position * dividerPacket6Position
    }
}

private fun List<Any>.packetAsString() =
    toString().replace(" ", "")