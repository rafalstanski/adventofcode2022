package pl.rstanski.adventofcode2022.day05.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day05.common.CratesStacksBuilder
import pl.rstanski.adventofcode2022.day05.common.DrawingParser
import pl.rstanski.adventofcode2022.day05.common.InstructionsBuilder

private const val PUZZLE_FILENAME = "day05.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day05Part2Solution.solve(puzzle)

    println(result)
}

object Day05Part2Solution {

    fun solve(puzzle: Puzzle): String {
        val drawing = DrawingParser.parseDrawing(puzzle)
        val cratesStacks = CratesStacksBuilder.buildCratesStacks(drawing)
        val instructions = InstructionsBuilder.buildInstructions(drawing)

        instructions.forEach { instruction ->
            val crates = (1..instruction.cratesNumber).map { cratesStacks.takeCrateFromStack(instruction.from - 1) }
            crates.reversed().forEach { cratesStacks.putCrateOnStack(instruction.to - 1, it) }
        }

        return cratesStacks.topCrateFromAllStacks()
            .joinToString("")
    }
}