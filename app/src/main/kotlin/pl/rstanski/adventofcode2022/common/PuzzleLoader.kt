package pl.rstanski.adventofcode2022.common

import java.net.URL

object PuzzleLoader {

    private const val LINE_SEPARATOR = "\n"

    fun load(fileName: String): Puzzle {
        val resource = loadResourceFromClasspath(fileName)
        return requireNotNull(resource)
            .let(::readLines)
            .let(::Puzzle)
    }

    private fun readLines(resource: URL): List<String> =
        resource.readText()
            .split(LINE_SEPARATOR)

    private fun loadResourceFromClasspath(fileName: String): URL? =
        Thread.currentThread().contextClassLoader.getResource(fileName)
}

data class Puzzle(val lines: List<String>)