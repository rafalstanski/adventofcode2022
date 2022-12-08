package pl.rstanski.adventofcode2022.day08.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day08.common.ForestParser
import pl.rstanski.adventofcode2022.day08.common.Forrest
import pl.rstanski.adventofcode2022.day08.common.Tree
import pl.rstanski.adventofcode2022.day08.common.findHigherOrEqualsThen

private const val PUZZLE_FILENAME = "day08.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day08Part1Solution.solve(puzzle)

    println(result)
}

object Day08Part1Solution {

    fun solve(puzzle: Puzzle): Any {
        val forest = ForestParser.parseForest(puzzle)
        val visibleTrees = VisibleFromOutSideTreeFinder.find(forest)

        return visibleTrees.count()
    }
}

private object VisibleFromOutSideTreeFinder {

    fun find(forest: Forrest): List<Tree> =
        forest.allTrees()
            .filter { tree ->
                val isHiddenOnLeft = forest.goLeftFrom(tree).findHigherOrEqualsThen(tree) != null
                val isHiddenOnRight = forest.goRightFrom(tree).findHigherOrEqualsThen(tree) != null
                val isHiddenOnTop = forest.goTopFrom(tree).findHigherOrEqualsThen(tree) != null
                val isHiddenOnBottom = forest.goBottomFrom(tree).findHigherOrEqualsThen(tree) != null

                !(isHiddenOnLeft && isHiddenOnRight && isHiddenOnTop && isHiddenOnBottom)
            }.toList()
}