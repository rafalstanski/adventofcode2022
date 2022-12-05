package pl.rstanski.adventofcode2022.day05.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day05.common.CratesStacks
import pl.rstanski.adventofcode2022.day05.common.CratesStacksBuilder.buildCratesStacks
import pl.rstanski.adventofcode2022.day05.common.DrawingParser.parseDrawing
import pl.rstanski.adventofcode2022.day05.common.InstructionsBuilder
import pl.rstanski.adventofcode2022.day05.common.MoveInstruction

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

        CrateMover9000.moveCrates(cratesStacks, instructions)

        return cratesStacks.topCrateFromAllStacks()
            .joinToString("")
    }
}

object CrateMover9000 {

    fun moveCrates(cratesStacks: CratesStacks, instructions: List<MoveInstruction>) {
        instructions.forEach { instruction ->
            runInstruction(instruction, cratesStacks)
        }
    }

    private fun runInstruction(instruction: MoveInstruction, cratesStacks: CratesStacks) {
        val crates = takeCreates(instruction, cratesStacks)

        cratesStacks.putCratesOnStack(instruction.toStackNumber, crates)
    }

    private fun takeCreates(instruction: MoveInstruction, cratesStacks: CratesStacks): List<Char> =
        (1..instruction.cratesCountToMove)
            .map { cratesStacks.takeCrateFromStack(instruction.fromStackNumber) }
}

