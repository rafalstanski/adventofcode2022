package pl.rstanski.adventofcode2022.day13.common

import pl.rstanski.adventofcode2022.day13.common.ORDER.NONE
import pl.rstanski.adventofcode2022.day13.common.ORDER.NOT_RIGHT_ORDER
import pl.rstanski.adventofcode2022.day13.common.ORDER.RIGHT_ORDER

object PacketComparator {

    fun compare(leftList: List<Any>, rightList: List<Any>): ORDER {
        val leftIterator = leftList.iterator()
        val rightIterator = rightList.iterator()

        while (leftIterator.hasNext() && rightIterator.hasNext()) {
            val left = leftIterator.next()
            val right = rightIterator.next()

            if (left is Int && right is Int) {
                if (left < right) {
                    return RIGHT_ORDER
                }
                if (left > right) {
                    return NOT_RIGHT_ORDER
                }
            }

            if (left is List<*> && right is List<*>) {
                val innerCompare = compare(left as List<Any>, right as List<Any>)
                if (innerCompare != NONE) return innerCompare
            }

            if (left is List<*> && right is Int) {
                val innerCompare = compare(left as List<Any>, listOf<Any>(right))
                if (innerCompare != NONE) return innerCompare
            }

            if (left is Int && right is List<*>) {
                val innerCompare = compare(listOf<Any>(left), right as List<Any>)
                if (innerCompare != NONE) return innerCompare
            }
        }

        if ((!leftIterator.hasNext()) && rightIterator.hasNext()) {
            return RIGHT_ORDER
        }

        if (leftIterator.hasNext() && !rightIterator.hasNext()) {
            return NOT_RIGHT_ORDER
        }

        return NONE
    }
}

enum class ORDER {
    RIGHT_ORDER, NONE, NOT_RIGHT_ORDER
}