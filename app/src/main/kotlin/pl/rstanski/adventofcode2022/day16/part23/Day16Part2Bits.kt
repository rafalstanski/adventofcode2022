package pl.rstanski.adventofcode2022.day16.part23

import kotlin.math.pow
import org.paukov.combinatorics3.Generator
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader.load

fun main() {
    check(solvePart2(load("day16sample.txt")) == 1707)

    println("solution: " + solvePart2(load("day16.txt")))
}

var openableToIndex: Map<String, Int> = emptyMap()

fun solvePart2(puzzle: Puzzle): Any {
    val pipes = puzzle.lines.map(::parse)
    val openableValves = pipes.filter(Pipe::openable).map { it.valve }
    openableToIndex = openableValves.mapIndexed {index, valve -> valve to 2.0.pow(index).toInt() }.toMap()
    val indexedPipes = pipes.map { IndexedPipe.fromPipe(it, openableToIndex[it.valve]) }

    val pipesByValve = indexedPipes.associateBy { it.valve }

    println("openableValves: $openableValves, size: ${openableValves.size}")

    val combination = Generator.combination(openableValves)
    println("combinations: " + combination.simple(openableValves.size / 2).count())

    val max = combination.simple(openableValves.size / 2).mapIndexed { index, c ->
        val myOpenable = c.map { pipesByValve.getValue(it).index!! }.reduce { acc, i -> acc or i }
        val eOpenable = (openableValves.toSet() - c).map { pipesByValve.getValue(it).index!! }.reduce { acc, i -> acc or i }

        println("$index: myOpenable: $myOpenable, eOpenable: $eOpenable")

        max = 0
        operate(myOpenable, "AA", listOf("AA"), 0, pipesByValve, 0, 25)
        val my = max

        max = 0
        operate(eOpenable, "AA", listOf("AA"), 0, pipesByValve, 0, 25)
        val e = max

        currentMax = if (my + e > currentMax) my + e else currentMax
        println("current max: $currentMax")
//            println("my: $my, e: $e")
        my + e
    }.maxOf { it }

    println(max)
    return max
}

var max = 0
var currentMax = 0

fun operate(
    openableValves: Int,
    currentValve: String,
    currentPath: List<String>,
    openedValves: Int,
    pipesByValve: Map<String, IndexedPipe>,
    pressure: Int,
    minutesLeft: Int
): Int {
    if (minutesLeft == 0 || openableValves == openedValves) {
        return pressure.saveIfBigger()
    }

    val pipe = pipesByValve.getValue(currentValve)
    if (pipe.index != null && (pipe.index and openableValves > 0) && (pipe.index and openedValves == 0)) {
        //open
        operate(openableValves, currentValve, listOf(currentValve), openedValves or pipe.index, pipesByValve, pressure + (pipe.rate * (minutesLeft)), minutesLeft - 1)
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


data class Pipe(val valve: String, val rate: Int, val leadsTo: List<String>) {
    val openable: Boolean
        get() = rate > 0
}

data class IndexedPipe(val valve: String, val index: Int?, val rate: Int, val leadsTo: List<String>) {
    val openable: Boolean
        get() = rate > 0

    companion object {
        fun fromPipe(pipe: Pipe, index: Int?): IndexedPipe =
            IndexedPipe(pipe.valve, index, pipe.rate, pipe.leadsTo)
    }
}


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
