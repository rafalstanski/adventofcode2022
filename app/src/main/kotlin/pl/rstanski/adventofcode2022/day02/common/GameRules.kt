package pl.rstanski.adventofcode2022.day02.common

import java.util.function.Predicate
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Draw
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Lost
import pl.rstanski.adventofcode2022.day02.common.RoundOutcome.Win
import pl.rstanski.adventofcode2022.day02.common.Shape.Paper
import pl.rstanski.adventofcode2022.day02.common.Shape.Rock
import pl.rstanski.adventofcode2022.day02.common.Shape.Scissors


val gameRules = Table(listOf(
    Rule(Rock, Rock, Draw),
    Rule(Paper, Paper, Draw),
    Rule(Scissors, Scissors, Draw),
    Rule(Rock, Paper, Win),
    Rule(Rock, Scissors, Lost),
    Rule(Paper, Rock, Lost),
    Rule(Paper, Scissors, Win),
    Rule(Scissors, Rock, Win),
    Rule(Scissors, Paper, Lost),
))

data class Table(
    private val rules: List<Rule>
) {
    fun findRules(ruleFilter: RuleFilter): List<Rule> =
        rules.filter(ruleFilter::test)
}

data class Rule(
    val opponentChoice: Shape,
    val ourResponse: Shape,
    val roundOutcome: RoundOutcome
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