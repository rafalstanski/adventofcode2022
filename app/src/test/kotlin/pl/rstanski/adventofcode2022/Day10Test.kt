package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day10.part1.Day10Part1Solution
import pl.rstanski.adventofcode2022.day10.part2.Day10Part2Solution

class Day10Test : BaseTest() {

    private val part1Solution = Day10Part1Solution
    private val part2Solution = Day10Part2Solution

    //given
    private val puzzle = puzzleFrom("day10sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 13140, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 0, actual = solution)
    }
}