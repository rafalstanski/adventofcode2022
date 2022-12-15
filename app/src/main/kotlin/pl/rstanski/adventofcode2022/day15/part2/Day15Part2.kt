package pl.rstanski.adventofcode2022.day15.part2

import pl.rstanski.adventofcode2022.common.Point
import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader
import pl.rstanski.adventofcode2022.day15.common.Sensor
import pl.rstanski.adventofcode2022.day15.common.SensorParser

private const val PUZZLE_FILENAME = "day15.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day15Part2Solution.solve(puzzle, 4000000)

    println(result)
}

object Day15Part2Solution {

    fun solve(puzzle: Puzzle, limit: Int): Any {
        val sensors = puzzle.lines.map(SensorParser::parseSensor)
        val distressBeacon = DistressBeaconFinder.find(sensors, limit)

        return distressBeacon.tuningFrequency()
    }

    private fun Point.tuningFrequency(): Long =
        this.x.toLong() * 4000000L + this.y.toLong()
}

object DistressBeaconFinder {

    fun find(sensors: List<Sensor>, limit: Int): Point {

        sensors.forEach { sensor ->
            val potentialBeacons = setOf(
                checkBoarder(sensor, sensors, limit, 1, 1),
                checkBoarder(sensor, sensors, limit, 1, -1),
                checkBoarder(sensor, sensors, limit, -1, -1),
                checkBoarder(sensor, sensors, limit, -1, 1)
            )
            if (potentialBeacons.filterNotNull().isNotEmpty()) {
                return potentialBeacons.filterNotNull().first()
            }
        }

        throw IllegalStateException("Unable to find distress beacon")
    }
    private fun checkBoarder(sensor: Sensor, sensors: List<Sensor>, limit: Int, leftRight: Int, topBottom: Int): Point? {
        val xRange = (0..sensor.distance + 1)

        xRange.forEach { x ->
            val y = sensor.distance + 1 - x
            val beacon = sensor.location + Point(x * leftRight, y * topBottom)

            if (beacon.x in (0..limit) && beacon.y in (0..limit)) {
                if (sensors.none { otherSensor -> otherSensor.inRange(beacon) }) {
                    return beacon
                }
            }
        }
        return null
    }
}
