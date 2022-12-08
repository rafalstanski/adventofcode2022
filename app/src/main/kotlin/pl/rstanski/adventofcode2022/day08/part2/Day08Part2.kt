package pl.rstanski.adventofcode2022.day08.part2

import kotlin.math.absoluteValue
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day08.common.ForestParser.parseForest
import pl.rstanski.adventofcode2022.day08.common.Forrest
import pl.rstanski.adventofcode2022.day08.common.Tree
import pl.rstanski.adventofcode2022.day08.common.findHigherOrEqualsThen

private const val PUZZLE_FILENAME = "day08.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day08Part2Solution.solve(puzzle)

    println(result)
}

object Day08Part2Solution {

    fun solve(puzzle: Puzzle): Any {
        val forest = parseForest(puzzle)
        val maxScenicScore = MaxScoreTreeHousePlaceFinder.find(forest)

        return maxScenicScore.score
    }
}

private object MaxScoreTreeHousePlaceFinder {

    fun find(forest: Forrest): ScenicScore {
        val scenicScores = forest.allTrees()
            .map { tree ->
                val left = forest.goLeftFrom(tree).findHigherOrEqualsThen(tree)
                val right = forest.goRightFrom(tree).findHigherOrEqualsThen(tree)
                val top = forest.goTopFrom(tree).findHigherOrEqualsThen(tree)
                val bottom = forest.goBottomFrom(tree).findHigherOrEqualsThen(tree)

                ScenicScore(
                    tree = tree,
                    distanceLeft = tree.calculateDistanceTo(left, valueIfNoTree = tree.x),
                    distanceRight = tree.calculateDistanceTo(right, valueIfNoTree = forest.xSize - 1 - tree.x),
                    distanceTop = tree.calculateDistanceTo(top, valueIfNoTree = forest.ySize - 1 - tree.y),
                    distanceBottom = tree.calculateDistanceTo(bottom, valueIfNoTree = tree.y)
                )
            }

        return scenicScores.maxByOrNull(ScenicScore::score)!!
    }
}

private fun Tree.calculateDistanceTo(toTree: Tree?, valueIfNoTree: Int): Int =
    when (toTree) {
        null -> valueIfNoTree
        else -> (this.x - toTree.x + this.y - toTree.y).absoluteValue
    }

private data class ScenicScore(
    val tree: Tree,
    val distanceLeft: Int,
    val distanceRight: Int,
    val distanceTop: Int,
    val distanceBottom: Int
) {
    val score = distanceLeft * distanceRight * distanceTop * distanceBottom
}