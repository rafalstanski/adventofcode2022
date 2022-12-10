package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day11.part1.Day11Part1Solution
import pl.rstanski.adventofcode2022.day11.part2.Day11Part2Solution

class Day11Test : BaseTest() {

    private val part1Solution = Day11Part1Solution
    private val part2Solution = Day11Part2Solution

    //given
    private val puzzle = puzzleFrom("day11sample.txt")

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