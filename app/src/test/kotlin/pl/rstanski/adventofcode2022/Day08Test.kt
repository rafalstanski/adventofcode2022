package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day08.part1.Day08Part1Solution
import pl.rstanski.adventofcode2022.day08.part2.Day08Part2Solution

class Day08Test : BaseTest() {

    private val part1Solution = Day08Part1Solution
    private val part2Solution = Day08Part2Solution

    //given
    private val puzzle = puzzleFrom("day08sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 0, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 0, actual = solution)
    }
}