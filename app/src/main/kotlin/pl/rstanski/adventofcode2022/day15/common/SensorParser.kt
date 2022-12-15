package pl.rstanski.adventofcode2022.day15.common

import pl.rstanski.adventofcode2022.common.Point


data class Sensor(val location: Point, val closestBeacon: Point) {
    val distance = location.manhattanDistanceTo(closestBeacon)

    fun inRange(beacon: Point): Boolean =
        location.manhattanDistanceTo(beacon) <= distance
}

object SensorParser {

    fun parseSensor(line: String): Sensor {
        // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        val parts = line.split(":")
        val sensorLocation = parseSensorLocation(parts[0])
        val beaconLocation = parseBeaconLocation(parts[1])

        return Sensor(location = sensorLocation, closestBeacon = beaconLocation)
    }

    private fun parseBeaconLocation(line: String): Point {
//        closest beacon is at x=-2, y=15
        val parts = line.drop("closest beacon is at ".length)
            .replace(" ", "")
            .split(",")
        val x = parts[0].split('=')[1].toInt()
        val y = parts[1].split('=')[1].toInt()

        return Point(x, y)
    }

    private fun parseSensorLocation(sensorLine: String): Point {
        // Sensor at x=2, y=18
        val parts = sensorLine.drop("Sensor at ".length)
            .replace(" ", "")
            .split(",")
        val x = parts[0].split('=')[1].toInt()
        val y = parts[1].split('=')[1].toInt()

        return Point(x, y)
    }
}