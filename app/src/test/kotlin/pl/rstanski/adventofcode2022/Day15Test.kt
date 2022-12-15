package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day15.part1.Day15Part1Solution
import pl.rstanski.adventofcode2022.day15.part2.Day15Part2Solution

class Day15Test : BaseTest() {

    private val part1Solution = Day15Part1Solution
    private val part2Solution = Day15Part2Solution

    //given
    private val puzzle = puzzleFrom("day15sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle, 10)
        //then
        assertEquals(expected = 26, actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle, 20)
        //then
        assertEquals(expected = 56000011, actual = solution)
    }
}