package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day03.part1.Day03Part1Solution
import pl.rstanski.adventofcode2022.day03.part2.Day03Part2Solution

class Day03Test : BaseTest() {

    private val part1Solution = Day03Part1Solution
    private val part2Solution = Day03Part2Solution

    //given
    private val puzzle = puzzleFrom("day03sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 157, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 70, actual = solution)
    }
}