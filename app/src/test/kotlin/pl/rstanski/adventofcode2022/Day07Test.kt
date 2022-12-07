package pl.rstanski.adventofcode2022

import java.math.BigInteger
import kotlin.test.assertEquals
import org.junit.Test
import pl.rstanski.adventofcode2022.day07.part1.Day07Part1Solution
import pl.rstanski.adventofcode2022.day07.part2.Day07Part2Solution

class Day07Test : BaseTest() {

    private val part1Solution = Day07Part1Solution
    private val part2Solution = Day07Part2Solution

    //given
    private val puzzle = puzzleFrom("day07sample.txt")

    @Test
    fun shouldFindCorrectSolutionOfPart1ForSamplePuzzle() {
        //when
        val solution = part1Solution.solve(puzzle)
        //then
        assertEquals(expected = BigInteger("95437"), actual = solution)
    }

    @Test
    fun shouldFindCorrectSolutionOfPart2ForSamplePuzzle() {
        //when
        val solution = part2Solution.solve(puzzle)
        //then
        assertEquals(expected = BigInteger("24933642"), actual = solution)
    }
}