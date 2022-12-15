package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day14.part1.Day14Part1Solution
import pl.rstanski.adventofcode2022.day14.part2.Day14Part2Solution

class Day14Test : BaseTest() {

    private val part1Solution = Day14Part1Solution
    private val part2Solution = Day14Part2Solution

    //given
    private val puzzle = puzzleFrom("day14sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 24, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 93, actual = solution)
    }
}