package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day05.part1.Day05Part1Solution
import pl.rstanski.adventofcode2022.day05.part2.Day05Part2Solution

class Day05Test : BaseTest() {

    private val part1Solution = Day05Part1Solution
    private val part2Solution = Day05Part2Solution

    //given
    private val puzzle = puzzleFrom("day05sample.txt")

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