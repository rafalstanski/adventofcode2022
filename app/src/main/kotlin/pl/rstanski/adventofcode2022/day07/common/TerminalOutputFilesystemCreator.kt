package pl.rstanski.adventofcode2022.day07.common

object TerminalOutputFilesystemCreator {

    fun recreateFromTerminalOutput(terminalLines: List<TerminalLine>): DirectoryNode {
        val root = DirectoryNode("/")

        var currentDirectory = root
        val linesIterator = terminalLines.iterator()
        while (linesIterator.hasNext()) {
            when (val terminalLine = linesIterator.next()) {
                is LsCommand -> {}
                is CdCommand -> currentDirectory = currentDirectory.createIfNotExistsAndGoTo(terminalLine.directoryName)
                is FileInfo -> recreateFromFileInfo(currentDirectory, terminalLine)
                is DirectoryInfo -> recreateFromDirectoryInfo(currentDirectory, terminalLine)
            }
        }

        return root
    }

    private fun recreateFromFileInfo(currentDirectory: DirectoryNode, fileInfo: FileInfo) {
        currentDirectory.createFileIfNotExists(fileInfo.name, fileInfo.size)
    }

    private fun recreateFromDirectoryInfo(currentDirectory: DirectoryNode, directoryInfo: DirectoryInfo) {
        currentDirectory.createDirectoryIfNotExists(directoryInfo.name)
    }
}