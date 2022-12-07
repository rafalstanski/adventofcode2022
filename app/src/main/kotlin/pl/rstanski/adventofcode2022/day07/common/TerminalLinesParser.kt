package pl.rstanski.adventofcode2022.day07.common

object TerminalLinesParser {

    fun parseTerminalLines(lines: List<String>): List<TerminalLine> =
        lines.map(::interpretLine)

    private fun interpretLine(line: String): TerminalLine =
        when {
            line.startsWith("$") -> interpretAsCommand(line)
            line.startsWith("dir") -> interpretAsDirectoryInfo(line)
            else -> interpretAsFileInfo(line)
        }

    private fun interpretAsCommand(line: String): TerminalLine {
        val parts: List<String> = line.split(" ")
        return when {
            parts[1] == "cd" -> CdCommand(parts[2])
            parts[1] == "ls" -> LsCommand
            else -> throw IllegalStateException("Unknown command: ${parts[1]}")
        }
    }

    private fun interpretAsDirectoryInfo(line: String): DirectoryInfo {
        val parts: List<String> = line.split(" ")
        return DirectoryInfo(parts[1])
    }

    private fun interpretAsFileInfo(line: String): FileInfo {
        val parts: List<String> = line.split(" ")
        return FileInfo(parts[1], parts[0].toInt())
    }
}