package pl.rstanski.adventofcode2022.common

fun Char.isNotBlank(): Boolean =
    this != ' '


fun Char.digitToInt(): Int =
    this.digitToIntOrNull()!!