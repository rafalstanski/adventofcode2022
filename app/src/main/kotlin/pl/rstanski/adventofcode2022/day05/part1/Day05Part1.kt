package pl.rstanski.adventofcode2022.day05.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day05.common.CratesStacksBuilder.buildCratesStacks
import pl.rstanski.adventofcode2022.day05.common.DrawingParser.parseDrawing
import pl.rstanski.adventofcode2022.day05.common.InstructionsBuilder

private const val PUZZLE_FILENAME = "day05.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day05Part1Solution.solve(puzzle)

    println(result)
}

object Day05Part1Solution {

    fun solve(puzzle: Puzzle): String {
        val drawing = parseDrawing(puzzle)
        val cratesStacks = buildCratesStacks(drawing)
        val instructions = InstructionsBuilder.buildInstructions(drawing)

        instructions.forEach { instruction ->
            val crates = (1..instruction.cratesNumber).map { cratesStacks.takeCrateFromStack(instruction.from - 1) }
            crates.forEach { cratesStacks.putCrateOnStack(instruction.to - 1, it) }
        }

        return cratesStacks.topCrateFromAllStacks()
            .joinToString("")
    }
}



