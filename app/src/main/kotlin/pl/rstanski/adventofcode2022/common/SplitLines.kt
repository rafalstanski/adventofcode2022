package pl.rstanski.adventofcode2022.common

object SplitLines {

    fun split(lines: List<String>, separatorRule: (String) -> Boolean = String::isBlank): List<List<String>> {
        val linesIterator = lines.iterator()

        val groups = mutableListOf<List<String>>()
        val currentGroup = mutableListOf<String>()

        while (linesIterator.hasNext()) {
            val line = linesIterator.next()

            if (separatorRule(line)) {
                groups.add(currentGroup.toList())
                currentGroup.clear()
            } else {
                currentGroup.add(line)
            }
        }
        groups.add(currentGroup.toList())
        currentGroup.clear()

        return groups
    }
}

fun List<String>.splitByEmptyLine(): List<List<String>> =
    SplitLines.split(this, String::isBlank)

fun List<String>.splitBy(separatorRule: (String) -> Boolean = String::isBlank): List<List<String>> =
    SplitLines.split(this, separatorRule)