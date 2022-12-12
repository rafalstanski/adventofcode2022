package pl.rstanski.adventofcode2022.day12.common

import pl.rstanski.adventofcode2022.common.Grid
import pl.rstanski.adventofcode2022.common.Puzzle

object GridParser {

    fun parseGrid(puzzle: Puzzle): Grid<Int> {
        val rows: List<String> = puzzle.lines

        val grid = prepareEmptyGridOfSizeBasedOn(rows)
        populateGrid(grid, rows)

        return grid

    }

    private fun prepareEmptyGridOfSizeBasedOn(treesRows: List<String>): Grid<Int> =
        Grid(xSize = treesRows.first().length, ySize = treesRows.size) { 0 }

    private fun populateGrid(grid: Grid<Int>, rows: List<String>) {
        rows.forEachIndexed { indexY: Int, row: String ->
            row.toList().forEachIndexed { indexX: Int, height: Char ->
                val elevation: Int = calculateElevation(height)

                grid.putPoint(
                    x = indexX,
                    y = grid.ySize - 1 - indexY,
                    value = elevation
                )
            }
        }
    }

    private fun calculateElevation(height: Char) = when (height) {
        'S' -> 'a'
        'E' -> 'z'
        else -> height
    }.code
}