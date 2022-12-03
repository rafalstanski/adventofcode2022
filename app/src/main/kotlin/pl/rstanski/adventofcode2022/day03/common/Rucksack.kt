package pl.rstanski.adventofcode2022.day03.common

data class Rucksack(val items: List<Char>) {
    init {
        require(items.size.mod(2) == 0)
    }

    private val firstCompartment = items.subList(0, items.size / 2)
    private val secondCompartment = items.subList(items.size / 2, items.size)

    fun commonItemsInCompartments(): Set<Char> =
        firstCompartment intersect secondCompartment.toSet()
}