package pl.rstanski.adventofcode2022

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

open class BaseTest {

    protected fun puzzleFrom(fileName: String): Puzzle =
        PuzzleLoader.load(fileName)
}