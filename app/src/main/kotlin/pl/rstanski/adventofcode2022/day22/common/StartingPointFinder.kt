package pl.rstanski.adventofcode2022.day22.common

import pl.rstanski.adventofcode2022.common.Point

object StartingPointFinder {
    fun findStartingPoint(mapOfTheBoard: List<String>): Point {
        val column = mapOfTheBoard.first()
            .mapIndexed { index, char -> index to char }
            .find { it.second == '.' }!!
            .first
        return Point(column, 0)
    }
}