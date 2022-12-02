package pl.rstanski.adventofcode2022.day02.common

import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Draw
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Lost
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Win
import pl.rstanski.adventofcode2022.day02.common.Shape.Paper
import pl.rstanski.adventofcode2022.day02.common.Shape.Rock
import pl.rstanski.adventofcode2022.day02.common.Shape.Scissors

interface ScoreAware {
    val score: Int

    operator fun plus(secondScoreAware: ScoreAware): Int =
        this.score + secondScoreAware.score
}

enum class Shape(override val score: Int) : ScoreAware {
    Rock(1), Paper(2), Scissors(3)
}

enum class RoundOutcome(override val score: Int) : ScoreAware {
    Lost(0), Draw(3), Win(6)
}

fun String.opponentChoiceAsShape(): Shape =
    when (this) {
        "A" -> Rock
        "B" -> Paper
        "C" -> Scissors
        else -> throw IllegalArgumentException("Unknown sign for OpponentChoice: $this")
    }

fun String.ourResponseAsShape(): Shape =
    when (this) {
        "X" -> Rock
        "Y" -> Paper
        "Z" -> Scissors
        else -> throw IllegalArgumentException("Unknown sign for Response: $this")
    }

fun String.asRoundOutcome(): RoundOutcome =
    when (this) {
        "X" -> Lost
        "Y" -> Draw
        "Z" -> Win
        else -> throw IllegalArgumentException("Unknown sign for RoundOutcome: $this")
    }

