package pl.rstanski.adventofcode2022

import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day01.part1.Day01Part1Solution
import pl.rstanski.adventofcode2022.day01.part2.Day01Part2Solution
import pl.rstanski.adventofcode2022.day02.part1.Day02Part1Solution
import pl.rstanski.adventofcode2022.day02.part2.Day02Part2Solution

class Day01Test : BaseTest() {

    private val part1Solution = Day01Part1Solution
    private val part2Solution = Day01Part2Solution
    //given
    private val puzzle = puzzleFrom("day01sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = 24000.toBigInteger(), actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = 45000.toBigInteger(), actual = solution)
    }
}