package pl.rstanski.adventofcode2022.day13.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.common.SplitLines
import pl.rstanski.adventofcode2022.day13.common.ORDER
import pl.rstanski.adventofcode2022.day13.common.PacketComparator
import pl.rstanski.adventofcode2022.day13.common.PacketParser.parsePacket

private const val PUZZLE_FILENAME = "day13.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day13Part1Solution.solve(puzzle)

    println(result)
}


object Day13Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val groups: List<List<String>> = SplitLines.split(puzzle.lines)

        return groups.mapIndexed { index, group -> parsePackets(index + 1, group) }
            .filter { it.second == ORDER.RIGHT_ORDER }
            .sumOf { it.first }
    }

    private fun parsePackets(index: Int, group: List<String>): Pair<Int, ORDER> {
        val leftPacket = parsePacket(group[0])
        val rightPacket = parsePacket(group[1])

        return index to PacketComparator.compare(leftPacket, rightPacket)
    }
}