package pl.rstanski.adventofcode2022.day07.part1

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day07.common.DirectoryNode
import pl.rstanski.adventofcode2022.day07.common.TerminalLine
import pl.rstanski.adventofcode2022.day07.common.TerminalLinesParser.parseTerminalLines
import pl.rstanski.adventofcode2022.day07.common.TerminalOutputFilesystemCreator.recreateFromTerminalOutput

private const val PUZZLE_FILENAME = "day07.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day07Part1Solution.solve(puzzle)

    println(result)
}


object Day07Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val terminalLines: List<TerminalLine> = parseTerminalLines(puzzle.lines)

        val root: DirectoryNode = recreateFromTerminalOutput(terminalLines)

        val allDirectories = listOf(root) + root.findAllDirectoriesRecursively()

        return allDirectories.filter { it.size() <= BigInteger("100000") }
            .sumOf { it.size() }
    }
}