package pl.rstanski.adventofcode2022.day05.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day05.common.CratesStacks
import pl.rstanski.adventofcode2022.day05.common.CratesStacksBuilder
import pl.rstanski.adventofcode2022.day05.common.DrawingParser
import pl.rstanski.adventofcode2022.day05.common.InstructionsBuilder
import pl.rstanski.adventofcode2022.day05.common.MoveInstruction

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

        CrateMover9001.moveCrates(cratesStacks, instructions)

        return cratesStacks.topCrateFromAllStacks()
            .joinToString("")
    }
}

object CrateMover9001 {

    fun moveCrates(cratesStacks: CratesStacks, instructions: List<MoveInstruction>) {
        instructions.forEach { instruction ->
            runInstruction(instruction, cratesStacks)
        }
    }

    private fun runInstruction(instruction: MoveInstruction, cratesStacks: CratesStacks) {
        val crates = takeCreates(instruction, cratesStacks)
            .reversed()

        cratesStacks.putCratesOnStack(instruction.toStackNumber, crates)
    }

    private fun takeCreates(instruction: MoveInstruction, cratesStacks: CratesStacks): List<Char> =
        (1..instruction.cratesCountToMove)
            .map { cratesStacks.takeCrateFromStack(instruction.fromStackNumber) }
}