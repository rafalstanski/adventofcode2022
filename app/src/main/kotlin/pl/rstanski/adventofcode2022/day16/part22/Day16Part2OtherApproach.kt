package pl.rstanski.adventofcode2022.day16.part22

import org.paukov.combinatorics3.Generator
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day16.common.Pipe
import pl.rstanski.adventofcode2022.day16.common.PipeParser

fun main() {
    check(solvePart2(load("day16sample.txt")) == 1707)

    println("solution: " + solvePart2(load("day16.txt")))
}

fun solvePart2(puzzle: Puzzle): Any {
    val pipes = puzzle.lines.map(PipeParser::parse)
    val pipesByValve = pipes.associateBy { it.valve }
    val openableValves = pipes.filter { it.rate > 0 }.sortedBy { it.rate }.map { it.valve }
    println("openableValves: $openableValves, size: ${openableValves.size}")

    val combination = Generator.combination(openableValves)
    println("combinations: " + combination.simple(openableValves.size / 2).count())

    val max = combination.simple(openableValves.size / 2).mapIndexed { index, c ->
            val myOpenable = c.toSet()
            val eOpenable = openableValves.toSet() - myOpenable

            println("$index: myOpenable: $myOpenable, eOpenable: $eOpenable")

            max = 0
            operate(myOpenable, "AA", listOf("AA"), setOf(), pipesByValve, 0, 25)
            val my = max

            max = 0
            operate(eOpenable, "AA", listOf("AA"), setOf(), pipesByValve, 0, 25)
            val e = max

//            println("my: $my, e: $e")
            my + e
    }.maxOf { it }

    println(max)
    return max
}

var max = 0

fun operate(
    openableValves: Set<String>,
    currentValve: String,
    currentPath: List<String>,
    openedValves: Set<String>,
    pipesByValve: Map<String, Pipe>,
    pressure: Int,
    minutesLeft: Int
): Int {
    if (minutesLeft == 0 || openableValves.size == openedValves.size) {
        return pressure.saveIfBigger()
    }

    val pipe = pipesByValve.getValue(currentValve)
    if (currentValve in openableValves && currentValve !in openedValves) {
        //open
        operate(openableValves, currentValve, listOf(currentValve), openedValves + currentValve, pipesByValve, pressure + (pipe.rate * (minutesLeft)), minutesLeft - 1)
    }

    //go into
    pipe.leadsTo
        .filterNot { it in currentPath }
        .forEach {
            operate(openableValves, it, currentPath + it, openedValves, pipesByValve, pressure, minutesLeft - 1)
        }

    return pressure.saveIfBigger()
}

fun Int.saveIfBigger(): Int {
    if (this > max) max = this
    return this
}