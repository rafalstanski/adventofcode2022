package pl.rstanski.adventofcode2022.day16.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    check(solvePart1(load("day16sample.txt")) == 1651)

    println("solution: " + solvePart1(load("day16.txt")))
}

var max = 0

fun solvePart1(puzzle: Puzzle): Any {
    val pipes = puzzle.lines.map(::parse)
//    val pipesByValve = pipes.associateBy { it.valve }
//
//    val graph: GraphImpl<String, Int> = GraphImpl(directed = true, 1)
//    pipes.forEach { pipe -> pipe.leadsTo.forEach { leadsTo -> graph.addArc(pipe.valve to leadsTo) } }
//
//    val activeValves = pipes.filter { it.rate > 0 }.map { it.valve } - "AA"
//    println("activeValves: $activeValves")
//    val permutationsCount = (1..activeValves.size).map(Int::toLong).reduce(Long::times)
//    println("activeValves: ${activeValves.size}, permutation: $permutationsCount")
//
//    val permutations = activeValves.permutations()
//    println("permutations: ${permutations.size}")
//
//    max = permutations.map { permutation ->
//        var currentValve = "AA"
//        var minutesLeft = 29
//        var sum = 0
//        permutation.forEach {
//            val (_, value) = shortestPath(graph, currentValve, it)
//            minutesLeft -= value.toInt()
//            val rate = pipesByValve.getValue(it).rate
//
//            sum += minutesLeft * rate
//            minutesLeft -= 1
//
//            currentValve = it
//        }
//
//        sum
//    }.maxOf { it }

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
