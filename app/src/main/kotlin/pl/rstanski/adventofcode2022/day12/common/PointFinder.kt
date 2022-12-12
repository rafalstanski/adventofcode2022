package pl.rstanski.adventofcode2022.day12.common

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle

object PointFinder {

    fun findPoint(puzzle: Puzzle, point: Char): Point {
        puzzle.lines.forEachIndexed { indexY: Int, row: String ->
            row.toList().forEachIndexed { indexX: Int, height: Char ->
                if (height == point) {
                    return Point(
                        x = indexX,
                        y = puzzle.lines.size - 1 - indexY,
                    )
                }
            }
        }
        throw IllegalStateException("Unable to find point $point")
    }
}