package pl.rstanski.adventofcode2022.day05.part1

import java.util.ArrayDeque
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day05.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day05Part1Solution.solve(puzzle)

    println(result)
}

object Day05Part1Solution {

    fun solve(puzzle: Puzzle): Int {
        val separatorIndex = puzzle.lines.indexOf("")
        val instructionsDrawing = puzzle.lines.subList(separatorIndex + 1, puzzle.lines.size)

        val cratesDrawing = puzzle.lines.subList(0, separatorIndex)
        val cratesIndex = cratesDrawing.last()
            .mapIndexed { index, sign -> index to sign }
            .filter { it.second != ' ' }
            .map { it.first to it.second.digitToIntOrNull()!! }
        val stacksNumber = cratesIndex.last().second

        val cratesLayers = cratesDrawing.dropLast(1).map { cratesLine ->
            val crates = MutableList(stacksNumber) { ' ' }
            cratesIndex.map { index ->
                val create = cratesLine[index.first]
                if (create != ' ') {
                    crates[index.second - 1] = create
                }
            }

            crates.toList()
        }

        val cratesStacks = CratesStacks(stacksNumber)

        cratesLayers.reversed().forEach { cratesLayer ->
            println(cratesLayer)
            cratesLayer.forEachIndexed { index, crate ->
                if (crate != ' ') cratesStacks.putCrateOnStack(index, crate)
            }
        }

//        cratesStacks.printStacks()
        //  0  1  2   3  4 5
        //move 1 from 2 to 1
        val instructions = instructionsDrawing.map { instructionDrawing ->
            val parts = instructionDrawing.split(" ")
            Instruction(parts[1].toInt(), parts[3].toInt(), parts[5].toInt())
        }

        instructions.forEach { instruction ->
            val crates = (1..instruction.cratesNumber).map { cratesStacks.takeCrateFromStack(instruction.from - 1) }
            crates.forEach { cratesStacks.putCrateOnStack(instruction.to - 1, it) }
        }

        println("*******")

        cratesStacks.print()
        TODO()
    }
}

data class Instruction(
    val cratesNumber: Int,
    val from: Int,
    val to: Int
)

class CratesStacks(stacksNumber: Int) {
    private val stacks: List<CratesStack>

    init {
        stacks = List<CratesStack>(stacksNumber) { CratesStack() }
    }

    fun putCrateOnStack(stackIndex: Int, crate: Char) {
        stacks[stackIndex].putCrate(crate)
    }

    fun takeCrateFromStack(stackIndex: Int): Char {
        return stacks[stackIndex].takeCrate()
    }

    fun printStacks() {
        stacks.forEach { it.printStacks() }
    }

    fun print() {
        println(stacks.map { it.topCrate() }.joinToString(""))
    }
}

class CratesStack {
    private val stack = ArrayDeque<Char>()

    fun putCrate(crate: Char) {
        stack.push(crate)
    }

    fun takeCrate(): Char {
        return stack.pop()
    }

    fun topCrate(): Char {
        return stack.peek()
    }

    fun printStacks() {
        println(stack)
    }
}