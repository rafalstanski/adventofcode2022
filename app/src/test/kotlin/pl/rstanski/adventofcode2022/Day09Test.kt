package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day09.part1.Day09Part1Solution
import pl.rstanski.adventofcode2022.day09.part2.Day09Part2Solution

class Day09Test : BaseTest() {

    private val part1Solution = Day09Part1Solution
    private val part2Solution = Day09Part2Solution

    //given
    private val puzzle = puzzleFrom("day09sample.txt")

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
        assertEquals(expected = 1, actual = solution)
    }
}