package pl.rstanski.adventofcode2022.day11.common

import java.math.BigInteger
import java.math.BigInteger.ZERO


data class Monkeys(private val monkeys: List<Monkey>) {

    private val monkeysByIndex = monkeys.associateBy(Monkey::index)

    fun monkeyByIndex(index: Int): Monkey =
        monkeysByIndex.getValue(index)

    fun forEach(action: (Monkey) -> Unit) {
        for (element in monkeys) action(element)
    }

    fun sortedFromMostInspectsCount(): List<Monkey> =
        monkeys.sortedByDescending(Monkey::inspectsCount)
}

data class Monkey(
    val index: Int,
    val items: MutableList<Item>,
    val operation: Operation,
    val test: Test
) {

    var inspectsCount = 0L

    fun takeAllItems(): List<Item> {
        val returnItems = items.toList()
        inspectsCount += returnItems.size
        items.clear()

        return returnItems
    }

    fun catch(newItem: Item) {
        items.add(newItem)
    }

    fun changeWorryLevel(item: Item) {
        item.worryLevel = operation.calculate(item)
    }

    fun decideWhereToThrow(item: Item): Int =
        test.decideToWhomThrowTo(item)
}

data class Test(
    val divisibleBy: BigInteger,
    val throwToWhenTrue: Int,
    val throwToWhenFalse: Int
) {

    fun decideToWhomThrowTo(item: Item): Int {
        return when (item.worryLevel.mod(divisibleBy) == ZERO) {
            true -> throwToWhenTrue
            false -> throwToWhenFalse
        }
    }
}