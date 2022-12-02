package pl.rstanski.adventofcode2022.day02.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day02.common.FindByOpponentChoice
import pl.rstanski.adventofcode2022.day02.common.FindByRoundOutcome
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome
import pl.rstanski.adventofcode2022.day02.common.Rule
import pl.rstanski.adventofcode2022.day02.common.RuleFilter
import pl.rstanski.adventofcode2022.day02.common.Shape
import pl.rstanski.adventofcode2022.day02.common.asRoundOutcome
import pl.rstanski.adventofcode2022.day02.common.gameRules
import pl.rstanski.adventofcode2022.day02.common.opponentChoiceAsShape

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

class RoundCalculator {

    fun calculate(roundStrategy: RoundStrategy): Int {
        val foundRule: Rule = searchByOpponentChoiceAndRoundOutcome(roundStrategy)
            .let(gameRules::findRules)
            .single()

        return foundRule.ourResponse.score + roundStrategy.expectedRoundOutcome.score
    }

    private fun searchByOpponentChoiceAndRoundOutcome(roundStrategy: RoundStrategy): RuleFilter =
        FindByOpponentChoice(roundStrategy.opponentChoice)
            .and(FindByRoundOutcome(roundStrategy.expectedRoundOutcome))
}

class RoundStrategyParser {

    fun parse(line: String): RoundStrategy {
        val parts: List<String> = line.split(" ")

        return RoundStrategy(
            opponentChoice = parts[0].opponentChoiceAsShape(),
            expectedRoundOutcome = parts[1].asRoundOutcome()
        )
    }
}

data class RoundStrategy(
    val opponentChoice: Shape,
    val expectedRoundOutcome: RoundOutcome
)