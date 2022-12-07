package pl.rstanski.adventofcode2022.day07.common

sealed class TerminalLine

sealed class Command : TerminalLine()
data class CdCommand(val directoryName: String) : Command()
object LsCommand : Command()

sealed class CommandResultLine : TerminalLine()
data class DirectoryInfo(val name: String) : CommandResultLine()
data class FileInfo(val name: String, val size: Int) : CommandResultLine()