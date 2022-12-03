package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day04.part1.Day04Part1Solution
import pl.rstanski.adventofcode2022.day04.part2.Day04Part2Solution

class Day04Test : BaseTest() {

    private val part1Solution = Day04Part1Solution
    private val part2Solution = Day04Part2Solution

    //given
    private val puzzle = puzzleFrom("day04sample.txt")

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