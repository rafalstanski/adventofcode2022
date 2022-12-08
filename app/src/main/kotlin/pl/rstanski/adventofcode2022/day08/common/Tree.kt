package pl.rstanski.adventofcode2022.day08.common

data class Tree(
    val x: Int,
    val y: Int,
    val height: Int,
    private val forest: Forrest
) {
    fun isFromBoarder(): Boolean =
        x == 0 || y == 0 || x == forest.xSize - 1 || y == forest.ySize
}