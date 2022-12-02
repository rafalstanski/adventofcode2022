package pl.rstanski.adventofcode2022.day02.common

import java.util.function.Predicate
import pl.rstanski.adventofcode2022.day02.common.Shape.Paper
import pl.rstanski.adventofcode2022.day02.common.Shape.Rock
import pl.rstanski.adventofcode2022.day02.common.Shape.Scissors

enum class Shape(val score: Int) {
    Rock(1), Paper(2), Scissors(3)
}

enum class RoundOutcome(val score: Int) {
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

val gameRules = Table(listOf(
    Rule(Rock, Rock, RoundOutcome.Draw),
    Rule(Paper, Paper, RoundOutcome.Draw),
    Rule(Scissors, Scissors, RoundOutcome.Draw),
    Rule(Rock, Paper, RoundOutcome.Win),
    Rule(Rock, Scissors, RoundOutcome.Lost),
    Rule(Paper, Rock, RoundOutcome.Lost),
    Rule(Paper, Scissors, RoundOutcome.Win),
    Rule(Scissors, Rock, RoundOutcome.Win),
    Rule(Scissors, Paper, RoundOutcome.Lost),
))

data class Table(
    private val rules: List<Rule>
) {
    fun findRules(ruleFilter: RuleFilter): List<Rule> =
        rules.filter(ruleFilter::test)
}

data class Rule(
    val opponentChoice: Shape, val ourResponse: Shape, val roundOutcome: RoundOutcome
)

typealias RuleFilter = Predicate<Rule>

class FindByOpponentChoice(private val opponentChoice: Shape) : RuleFilter {
    override fun test(rule: Rule) = rule.opponentChoice == opponentChoice
}

class FindByOurResponse(private val ourResponse: Shape) : RuleFilter {
    override fun test(rule: Rule) = rule.ourResponse == ourResponse
}

class FindByRoundOutcome(private val roundOutcome: RoundOutcome) : RuleFilter {
    override fun test(rule: Rule) = rule.roundOutcome == roundOutcome
}