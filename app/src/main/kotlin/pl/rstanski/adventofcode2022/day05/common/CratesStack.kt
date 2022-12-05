package pl.rstanski.adventofcode2022.day05.common

import java.util.ArrayDeque

class CratesStack {
    private val stack = ArrayDeque<Char>()

    fun putCrate(crate: Char) {
        stack.push(crate)
    }

    fun takeCrate(): Char {
        return stack.pop()
    }

    fun topCrate(): Char {
        return stack.peek()
    }
}