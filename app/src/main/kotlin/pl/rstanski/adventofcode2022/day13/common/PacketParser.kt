package pl.rstanski.adventofcode2022.day13.common

object PacketParser {

    fun parsePacket(line: String): List<Any> {
        val iterator = line.drop(1).dropLast(1)
            .iterator()
        return parsePacket(iterator)
    }

    private fun parsePacket(iterator: CharIterator): List<Any> {
        val values: MutableList<Any> = mutableListOf()
        val digits: MutableList<Char> = mutableListOf()

        while (iterator.hasNext()) {
            val char = iterator.next()

            if (char.isDigit()) digits.add(char)

            if (char == '[') values.add(parsePacket(iterator))

            if (char == ']') {
                if (digits.isNotEmpty()) {
                    val number = digits.joinAsInt()
                    values.add(number)
                }
                return values
            }

            if (char == ',') {
                if (digits.isNotEmpty()) {
                    val number = digits.joinAsInt()
                    digits.clear()
                    values.add(number)
                }
            }
        }

        if (digits.isNotEmpty()) {
            val number = digits.joinAsInt()
            values.add(number)
        }

        return values
    }
}

private fun List<Char>.joinAsInt(): Int =
    this.joinToString("").toInt()