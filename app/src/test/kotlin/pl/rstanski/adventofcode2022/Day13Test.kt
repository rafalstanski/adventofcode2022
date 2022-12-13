package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day13.part1.Day13Part1Solution
import pl.rstanski.adventofcode2022.day13.part2.Day13Part2Solution

class Day13Test : BaseTest() {

    private val part1Solution = Day13Part1Solution
    private val part2Solution = Day13Part2Solution

    //given
    private val puzzle = puzzleFrom("day13sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 13, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 0, actual = solution)
    }
}