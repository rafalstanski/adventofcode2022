package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day06.part1.Day06Part1Solution
import pl.rstanski.adventofcode2022.day06.part2.Day06Part2Solution

class Day06Test : BaseTest() {

    private val part1Solution = Day06Part1Solution
    private val part2Solution = Day06Part2Solution

    //given
    private val puzzle = puzzleFrom("day06sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 7, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 19, actual = solution)
    }
}