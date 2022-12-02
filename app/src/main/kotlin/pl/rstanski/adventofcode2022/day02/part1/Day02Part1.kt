package pl.rstanski.adventofcode2022.day02.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day02.common.FindByOpponentChoice
import pl.rstanski.adventofcode2022.day02.common.FindByOurResponse
import pl.rstanski.adventofcode2022.day02.common.Rule
import pl.rstanski.adventofcode2022.day02.common.RuleFilter
import pl.rstanski.adventofcode2022.day02.common.Shape
import pl.rstanski.adventofcode2022.day02.common.gameRules
import pl.rstanski.adventofcode2022.day02.common.opponentChoiceAsShape
import pl.rstanski.adventofcode2022.day02.common.ourResponseAsShape

private const val PUZZLE_FILENAME = "day02.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day02Part1Solution.solve(puzzle)

    println(result)
}

object Day02Part1Solution {

    private val roundStrategyParser = RoundStrategyParser()
    private val roundCalculator = RoundCalculator()

    fun solve(puzzle: Puzzle): Int {
        return puzzle.lines
            .map(roundStrategyParser::parse)
            .map(roundCalculator::calculate)
            .sum()
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

    fun calculate(roundStrategy: RoundStrategy): Int {
        val foundRule: Rule = searchByOpponentChoiceAndOurResponse(roundStrategy)
            .let(gameRules::findRules)
            .single()

        return roundStrategy.response.score + foundRule.roundOutcome.score
    }

    private fun searchByOpponentChoiceAndOurResponse(roundStrategy: RoundStrategy): RuleFilter =
        FindByOpponentChoice(roundStrategy.opponentChoice)
            .and(FindByOurResponse(roundStrategy.response))
}