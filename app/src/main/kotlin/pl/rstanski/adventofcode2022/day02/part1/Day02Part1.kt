package pl.rstanski.adventofcode2022.day02.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Draw
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Lost
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Win
import pl.rstanski.adventofcode2022.day02.common.Shape
import pl.rstanski.adventofcode2022.day02.common.Shape.Paper
import pl.rstanski.adventofcode2022.day02.common.Shape.Rock
import pl.rstanski.adventofcode2022.day02.common.Shape.Scissors
import pl.rstanski.adventofcode2022.day02.common.opponentChoiceAsShape
import pl.rstanski.adventofcode2022.day02.common.ourResponseAsShape

private const val PUZZLE_FILENAME = "day02/day02.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day02Part1Solution.solve(puzzle)

    println(result)
}

object Day02Part1Solution {

    private val roundStrategyParser = RoundStrategyParser()
    private val roundCalculator = RoundCalculator()

    fun solve(puzzle: Puzzle): Any {
        return puzzle.lines
            .map(roundStrategyParser::parse)
            .map(roundCalculator::calculate)
            .sumOf { it.toBigInteger() }
    }
}

private class RoundStrategyParser {

    fun parse(line: String): RoundStrategy {
        val parts: List<String> = line.split(" ")

        return RoundStrategy(
            opponentChoice = parts[0].opponentChoiceAsShape(),
            response = parts[1].ourResponseAsShape()
        )
    }
}

private data class RoundStrategy(
    val opponentChoice: Shape,
    val response: Shape
)

private class RoundCalculator {

    private val rules: Map<Pair<Shape, Shape>, RoundOutcome> = mapOf(
        (Rock to Rock) to Draw,
        (Paper to Paper) to Draw,
        (Scissors to Scissors) to Draw,

        (Rock to Paper) to Win,
        (Rock to Scissors) to Lost,

        (Paper to Rock) to Lost,
        (Paper to Scissors) to Win,

        (Scissors to Rock) to Win,
        (Scissors to Paper) to Lost,
    )
    fun calculate(roundStrategy: RoundStrategy): Int {
        val round = roundStrategy.opponentChoice to roundStrategy.response
        val roundOutcome = rules[round] ?: throw IllegalStateException("Unknown round: $round")

        return roundStrategy.response.score + roundOutcome.score
    }
}

