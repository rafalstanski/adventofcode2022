package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day02.part1.Day02Part1Solution
import pl.rstanski.adventofcode2022.day02.part2.Day02Part2Solution

class Day02Test : BaseTest() {

    private val day02Part1Solution = Day02Part1Solution
    private val day02Part2Solution = Day02Part2Solution
    //given
    private val puzzle = puzzleFrom("day02sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = day02Part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 15, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = day02Part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 12, actual = solution)
    }
}