package pl.rstanski.adventofcode2022.day05.common

import pl.rstanski.adventofcode2022.common.Puzzle

object DrawingParser {

    fun parseDrawing(puzzle: Puzzle): Drawing {
        val separatorIndex = puzzle.lines.indexOf("")
        val cratesDrawing = puzzle.lines.subList(0, separatorIndex)
        val instructionsDrawing = puzzle.lines.subList(separatorIndex + 1, puzzle.lines.size)

        return Drawing(
            crates = cratesDrawing.dropLast(1),
            indexes = cratesDrawing.last(),
            instructions = instructionsDrawing
        )
    }
}