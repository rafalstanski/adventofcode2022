package pl.rstanski.adventofcode2022.day16.part2

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day16.part1.max

fun main() {
    check(solvePart2(PuzzleLoader.load("day16sample.txt")) == 1707)

    println("solution: " + solvePart2(PuzzleLoader.load("day16.txt")))
}

fun solvePart2(puzzle: Puzzle): Any {
    val pipes = puzzle.lines.map(::parse)
    val pipesByValve = pipes.associateBy { it.valve }

    operate("AA", listOf("AA"), "AA", listOf("AA"), setOf(), pipesByValve, 0, 25)

    println(max)
    return max
}

fun operate(
    myCurrentValve: String,
    myCurrentPath: List<String>,
    eCurrentValve: String,
    eCurrentPath: List<String>,
    openedValves: Set<String>,
    pipesByValve: Map<String, Pipe>,
    pressure: Int,
    minutesLeft: Int
): Int {
    if (minutesLeft == 0) {
        return pressure.saveIfBigger()
    }

    val myPipe = pipesByValve.getValue(myCurrentValve)
    val ePipe = pipesByValve.getValue(eCurrentValve)
    val myOpen = myPipe.rate > 0 && !openedValves.contains(myCurrentValve)
    val eOpen = myPipe != ePipe && ePipe.rate > 0 && !openedValves.contains(eCurrentValve)

    if (myOpen && eOpen) {
        operate(myCurrentValve, listOf(myCurrentValve), eCurrentValve, listOf(eCurrentValve),openedValves + myCurrentValve + eCurrentValve, pipesByValve, pressure + (myPipe.rate * (minutesLeft)) + (ePipe.rate * (minutesLeft)), minutesLeft - 1)
    }
    if (myOpen && !eOpen) {
        ePipe.leadsTo
            .filterNot { it in eCurrentPath }
            .forEach {
                operate(myCurrentValve, listOf(myCurrentValve), it, eCurrentPath + it,openedValves + myCurrentValve, pipesByValve, pressure + (myPipe.rate * (minutesLeft)), minutesLeft - 1)
            }
    }
    if (!myOpen && eOpen) {
        myPipe.leadsTo
            .filterNot { it in myCurrentPath }
            .forEach {
                operate(it, myCurrentPath + it, eCurrentValve, listOf(eCurrentValve),openedValves + eCurrentValve, pipesByValve, pressure + (ePipe.rate * (minutesLeft)), minutesLeft - 1)
            }
    }

    //go into
    val myLeadsTo = myPipe.leadsTo
        .filterNot { it in myCurrentPath }

    val eLeadsTo = ePipe.leadsTo
        .filterNot { it in eCurrentPath }

    if (myLeadsTo.isEmpty()) {
        eLeadsTo
            .forEach { eLeadTo ->
                operate(myCurrentValve, myCurrentPath, eLeadTo, eCurrentPath + eLeadTo, openedValves, pipesByValve, pressure, minutesLeft - 1)
            }
    } else if (eLeadsTo.isEmpty()) {
        myLeadsTo
            .forEach { myLeadTo ->
                operate(myLeadTo, myCurrentPath + myLeadTo, eCurrentValve, eCurrentPath, openedValves, pipesByValve, pressure, minutesLeft - 1)
            }
    } else {
        val samePlace = myPipe == ePipe// && eCurrentPath == myCurrentPath

        myLeadsTo
            .forEach { myLeadTo ->
                val eLiteToWithoutMine = eLeadsTo
                    .filter { (!samePlace) || it != myLeadTo }

                if (eLiteToWithoutMine.isEmpty()) {
                    operate(myLeadTo, myCurrentPath + myLeadTo, eCurrentValve, eCurrentPath, openedValves, pipesByValve, pressure, minutesLeft - 1)
                } else {
                    eLiteToWithoutMine
                        .forEach { eLeadTo ->
                            operate(myLeadTo, myCurrentPath + myLeadTo, eLeadTo, eCurrentPath + eLeadTo, openedValves, pipesByValve, pressure, minutesLeft - 1)
                        }
                }
            }
    }

    return pressure.saveIfBigger()
}

fun Int.saveIfBigger(): Int {
    if (this > max) max = this
    return this
}

data class Pipe(val valve: String, val rate: Int, val leadsTo: List<String>)

fun parse(line: String): Pipe {
    //Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    val valve = line.drop("Valve ".length).take(2)
    val rateAndLead = line.drop("Valve AA has flow rate=".length).split(";")
    val rate = rateAndLead[0].toInt()
    val lead = if (rateAndLead[1].contains("tunnels")) {
        rateAndLead[1].drop(" tunnels lead to valves ".length).replace(" ", "").split(",")
    } else {
        rateAndLead[1].drop(" tunnel lead to valves ".length).replace(" ", "").split(",")
    }

    return Pipe(valve, rate, lead)
}
