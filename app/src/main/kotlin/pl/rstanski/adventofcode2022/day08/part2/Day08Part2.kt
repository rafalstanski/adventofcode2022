package pl.rstanski.adventofcode2022.day08.part2

import kotlin.streams.toList
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day08.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day08Part2Solution.solve(puzzle)

    println(result)
}

object Day08Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val treeHeights = puzzle.lines

        val grid = Grid(treeHeights.first().length, treeHeights.size)

        treeHeights.forEachIndexed {indexY: Int, line: String ->
            line.chars().toList().forEachIndexed { indexX: Int, height: Int ->
                grid.putThree(indexX, grid.ySize - 1 - indexY, height - 48)
            }
        }


        return grid.findVisible()
    }
}

class Grid(private val xSize: Int, val ySize: Int) {
    val trees: MutableList<MutableList<Int>> = MutableList(xSize) { MutableList(ySize) { 0 } }

    fun putThree(x: Int, y: Int, height: Int) {
        trees[x][y] = height
    }

    fun getThree(x: Int, y: Int): Three {
        return Three(x, y, trees[x][y])
    }

    data class Three(val x: Int, val y: Int, val height: Int)

    data class ScenicScoreXY(val x: Int, val y: Int, val score: Int)

    fun findVisible(): Int {
        var count = edgeTreesCount()
        val scores = mutableListOf<ScenicScoreXY>()

        (1..xSize - 2).forEach { x ->
            (1..ySize - 2).forEach { y ->
                val currentThree = getThree(x, y)
                val height = currentThree.height

                val left: Three? = (0..x - 1).reversed().map { getThree(it, y) }.find { it.height >= height }
                val right: Three? = (x + 1..xSize - 1).map { getThree(it, y) }.find { it.height >= height }
                val bottom: Three? = (0..y - 1).reversed().map { getThree(x, it) }.find { it.height >= height }
                val top = (y + 1..ySize - 1).map { getThree(x, it) }.find { it.height >= height }


                val dLeft = if (left != null) currentThree.x - left.x else currentThree.x
                val dRight = if (right != null) right.x - currentThree.x  else xSize - 1 - currentThree.x
                val dBottom = if (bottom != null) currentThree.y - bottom.y else currentThree.y
                val dTop = if (top != null) top.y - currentThree.y else ySize - 1 - currentThree.y

                val scenicScore: Int = dLeft * dRight * dBottom * dTop

                scores.add(ScenicScoreXY(x, y, scenicScore))
            }
        }

        println(scores)

        return scores.maxOf { it.score }
    }

    private fun edgeTreesCount() = xSize * 2 + ySize * 2 - 4
}
