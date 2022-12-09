package pl.rstanski.adventofcode2022.day09.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day09.common.Move
import pl.rstanski.adventofcode2022.day09.common.MovesParser.parseMoves
import pl.rstanski.adventofcode2022.day09.common.Position
import pl.rstanski.adventofcode2022.day09.common.Rope
import pl.rstanski.adventofcode2022.day09.common.RopeMoverMemorisingTailPosition

private const val PUZZLE_FILENAME = "day09.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day09Part1Solution.solve(puzzle)

    println(result)
}

object Day09Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val moves: List<Move> = parseMoves(puzzle)
        val rope = Rope(1 + 1)

        val ropeMover = RopeMoverMemorisingTailPosition(rope)
        val tailPositions: Set<Position> = ropeMover.moveAndReturnTailPositionsUsing(moves)

        return tailPositions.size
    }
}