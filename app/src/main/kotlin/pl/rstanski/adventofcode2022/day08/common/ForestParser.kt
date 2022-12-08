package pl.rstanski.adventofcode2022.day08.common

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.digitToInt

object ForestParser {

    fun parseForest(puzzle: Puzzle): Forrest {
        val treesRows: List<String> = puzzle.lines

        val forest: Forrest = prepareEmptyForestOfSizeBasedOn(treesRows)
        populateForest(forest, treesRows)

        return forest
    }

    private fun prepareEmptyForestOfSizeBasedOn(treesRows: List<String>): Forrest =
        Forrest(xSize = treesRows.first().length, ySize = treesRows.size)
    private fun populateForest(forest: Forrest, treesRows: List<String>) {
        treesRows.forEachIndexed { indexY: Int, treesRow: String ->
            treesRow.toList().forEachIndexed { indexX: Int, height: Char ->
                forest.putTree(
                    x = indexX,
                    y = forest.ySize - 1 - indexY,
                    height = height.digitToInt()
                )
            }
        }
    }
}