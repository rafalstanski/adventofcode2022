package pl.rstanski.adventofcode2022.day19.common

import java.time.Instant

fun minedGeodeUsing(blueprints: List<Blueprint>, mineSessionFactory: (Blueprint) -> MineSession): List<Pair<Int, Int>> {
    val minedGeodeByBlueprint = blueprints.map { blueprint ->
        println("${Instant.now()} start mining with blueprint: $blueprint")
        val minedGeode = mineSessionFactory(blueprint).mine()
        println("${Instant.now()} Mined geode: $minedGeode")

        blueprint.index to minedGeode
    }

    minedGeodeByBlueprint.forEach { println("mined geode (blueprint = ${it.first}) - ${it.second}") }

    return minedGeodeByBlueprint
}