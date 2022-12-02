package pl.rstanski.adventofcode2022.day02.part1

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day02.part1.RoundOutcome.Draw
import pl.rstanski.adventofcode2022.day02.part1.RoundOutcome.Lost
import pl.rstanski.adventofcode2022.day02.part1.RoundOutcome.Win

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

    private val rules : Map<Pair<OpponentChoiceType, ResponseType>, RoundOutcome> = mapOf(
        (OpponentChoiceType.Rock to ResponseType.Rock) to Draw,
        (OpponentChoiceType.Paper to ResponseType.Paper) to Draw,
        (OpponentChoiceType.Scissors to ResponseType.Scissors) to Draw,
        (OpponentChoiceType.Rock to ResponseType.Paper) to Win,
        (OpponentChoiceType.Rock to ResponseType.Scissors) to Lost,
        (OpponentChoiceType.Paper to ResponseType.Rock) to Lost,
        (OpponentChoiceType.Paper to ResponseType.Scissors) to Win,
        (OpponentChoiceType.Scissors to ResponseType.Rock) to Win,
        (OpponentChoiceType.Scissors to ResponseType.Paper) to Lost,
    )

    fun calculate(roundStrategy: RoundStrategy): Int {
        val round = roundStrategy.opponentChoice to roundStrategy.response
        val roundOutcome = rules[round] ?: throw IllegalStateException("Unknown round: $round")

        return roundStrategy.response.score + roundOutcome.score
    }
}

class RoundStrategyParser {

    fun parse(line: String): RoundStrategy {
        val parts: List<String> = line.split(" ")

        return RoundStrategy(
            opponentChoice = OpponentChoiceType.toType(parts[0]),
            response = ResponseType.toType(parts[1])
        )
    }
}

data class RoundStrategy(
    val opponentChoice: OpponentChoiceType,
    val response: ResponseType
)

enum class OpponentChoiceType(private val sign: String) {
    Rock("A"), Paper("B"), Scissors("C");

    companion object {
        fun toType(sign: String): OpponentChoiceType {
            return values()
                .find { it.sign == sign }
                ?: throw IllegalArgumentException("Unknown sign for OpponentChoice: $sign")
        }
    }
}

enum class ResponseType(private val sign: String, val score: Int) {
    Rock("X", 1), Paper("Y", 2), Scissors("Z", 3);

    companion object {
        fun toType(sign: String): ResponseType {
            return values()
                .find { it.sign == sign }
                ?: throw IllegalArgumentException("Unknown sign for Response: $sign")
        }
    }
}

enum class RoundOutcome(val score: Int) {
    Lost(0), Draw(3), Win(6)
}