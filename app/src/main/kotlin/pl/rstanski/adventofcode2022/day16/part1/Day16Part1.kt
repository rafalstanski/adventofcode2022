package pl.rstanski.adventofcode2022.day16.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load
import pl.rstanski.adventofcode2022.day16.common.Pipe
import pl.rstanski.adventofcode2022.day16.common.PipeParser

fun main() {
    check(solvePart1(load("day16sample.txt")) == 1651)

    println("solution: " + solvePart1(load("day16.txt")))
}

var max = 0

fun solvePart1(puzzle: Puzzle): Any {
    val pipes = puzzle.lines.map(PipeParser::parse)
    val pipesByValve = pipes.associateBy { it.valve }

    operate("AA", listOf("AA"), setOf(), pipesByValve, 0, 29)

    println(max)
    return max
}

fun operate(
    currentValve: String,
    currentPath: List<String>,
    openedValves: Set<String>,
    pipesByValve: Map<String, Pipe>,
    pressure: Int,
    minutesLeft: Int
): Int {
    if (minutesLeft == 0) {
//        println("end")
        return pressure.saveIfBigger()
    }

    val pipe = pipesByValve.getValue(currentValve)
    if (pipe.rate > 0 && !openedValves.contains(currentValve)) {
        //open
        if (!openedValves.contains(currentValve)) {
            operate(currentValve, listOf(currentValve), openedValves + currentValve, pipesByValve, pressure + (pipe.rate * (minutesLeft)), minutesLeft - 1)
        }
    }

    //go into
    pipe.leadsTo
        .filterNot { it in currentPath }
        .forEach {
        operate(it, currentPath + it, openedValves, pipesByValve, pressure, minutesLeft - 1)
    }

    return pressure.saveIfBigger()
}

fun Int.saveIfBigger(): Int {
    if (this > max) max = this
    return this
}