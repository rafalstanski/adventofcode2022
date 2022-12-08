package pl.rstanski.adventofcode2022.day08.part1

import kotlin.streams.toList
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day08.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day08Part1Solution.solve(puzzle)

    println(result)
}


object Day08Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val treeHeights = puzzle.lines

        val grid = Grid(treeHeights.first().length, treeHeights.size)

        treeHeights.forEachIndexed {indexY: Int, line: String ->
            line.chars().toList().forEachIndexed { indexX: Int, height: Int ->
                grid.putTrees(indexX, indexY, height - 48)
            }
        }


        return grid.findVisible()
    }
}

class Grid(private val xSize: Int, val ySize: Int) {
    val trees: MutableList<MutableList<Int>> = MutableList(xSize) { MutableList(ySize) { 0 } }

    fun putTrees(x: Int, y: Int, height: Int) {
        trees[x][y] = height
    }

    fun getTrees(x: Int, y: Int): Int {
        return trees[x][y]
    }

    fun findVisible(): Int {
        var count = edgeTreesCount()

        (1..xSize - 2).forEach { x ->
            (1..ySize - 2).forEach { y ->
                val height = getTrees(x, y)

                val onTheLeft = (0..x - 1).find { getTrees(it, y) >= height } != null
                val onTheRight = (x + 1..xSize - 1).find { getTrees(it, y) >= height } != null
                val fromBottom = (0..y - 1).find { getTrees(x, it) >= height } != null
                val fromTop = (y + 1..ySize - 1).find { getTrees(x, it) >= height } != null

                if (!(onTheLeft && onTheRight && fromTop && fromBottom)) count++
            }
        }

        return count
    }

    private fun edgeTreesCount() = xSize * 2 + ySize * 2 - 4
}
