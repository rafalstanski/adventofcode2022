package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day12.part1.Day12Part1Solution
import pl.rstanski.adventofcode2022.day12.part2.Day12Part2Solution

class Day12Test : BaseTest() {

    private val part1Solution = Day12Part1Solution
    private val part2Solution = Day12Part2Solution

    //given
    private val puzzle = puzzleFrom("day12sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 31, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 29, actual = solution)
    }
}