package pl.rstanski.adventofcode2022.day05.common

data class MoveInstruction(
    val cratesCountToMove: Int,
    val fromStackNumber: Int,
    val toStackNumber: Int
)