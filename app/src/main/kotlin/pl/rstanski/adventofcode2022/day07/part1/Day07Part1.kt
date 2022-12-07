package pl.rstanski.adventofcode2022.day07.part1

import pl.rstanski.adventofcode2022.common.Puzzle
import pl.rstanski.adventofcode2022.common.PuzzleLoader

private const val PUZZLE_FILENAME = "day07.txt"

fun main() {
    val puzzle: Puzzle = PuzzleLoader.load(PUZZLE_FILENAME)

    val result = Day07Part1Solution.solve(puzzle)



    println(result)
}

object Day07Part1Solution {

    fun solve(puzzle: Puzzle): Any {

        val instrs = puzzle.lines.map { line ->
            when {
                line.startsWith("$") -> commmand(line)
                line.startsWith("dir") -> directory(line)
                else -> file(line)
            }
        }

        val filesystem = Filesystem()


        instrs.drop(1).forEach { filesystem.apply(it) }

        println(instrs)
        filesystem.print()

        TODO()
    }

    private fun file(line: String): File {
        val parts = line.split(" ")
        return File(parts[0].toInt(), parts[1])
    }

    private fun directory(line: String): DirectoryName {
        val parts = line.split(" ")
        return DirectoryName(parts[1])
    }

    private fun commmand(line: String): Sth {
        val parts = line.split(" ")
        return when {
            parts[1] == "cd" -> CdCommand(parts[2])
            parts[1] == "ls" -> LsCommand
            else -> throw IllegalStateException("Uknown command: ${parts[1]}")
        }
    }
}

class Filesystem {

    private val structure = DirectoryNode("/", null, mutableListOf())
    private var dirPrinting: DirectoryNode? = null
    private var currentDirectory: DirectoryNode = structure

    fun print() {
        print(structure, 0)
    }

    private fun print(node: Node, indent: Int) {
        val pre = (0..indent)
            .map { "  " }
            .joinToString("")
        println(pre + node.name)
        node.children.forEach {
            print(it, indent + 1)
        }
    }

    fun apply(sth: Sth) {
        println(sth.toString() + "\t\t: " + currentDirectory.toString())
        when (sth) {
            is CdCommand -> {
                when {
                    sth.dir == ".." -> currentDirectory = currentDirectory.parent!!
                    else -> {
                        val dir = DirectoryNode(sth.dir, currentDirectory, mutableListOf())
                        structure.children.add(dir)
                        currentDirectory = dir
                    }
                }
                dirPrinting = currentDirectory
            }

            is LsCommand -> ignore()
            is DirectoryName -> {
                val dddd = structure.children.find { it.name == sth.name } as DirectoryNode?
                if (dddd == null) {
                    val dir = DirectoryNode(sth.name, currentDirectory, mutableListOf())
                    structure.children.add(dir)
                    dirPrinting = dir
                } else {
                    dirPrinting = dddd
                }
            }

            is File -> {
                if (structure.children.find { it.name == sth.name } == null) {
                    dirPrinting!!.children.add(FileNode(sth.name, dirPrinting, sth.size))
                }
            }
        }
    }

    private fun ignore() {
    }
}

sealed class Node(
    open val name: String,
    open val parent: DirectoryNode?,
    open val children: MutableList<Node>
)

class DirectoryNode(
    override val name: String,
    override val parent: DirectoryNode?,
    override val children: MutableList<Node>
) : Node(name, parent, children) {
    override fun toString(): String {
        return "DirectoryNode(name='$name', children=$children)" // parent=${parent?.name},
    }
}

class FileNode(
    override val name: String,
    override val parent: DirectoryNode?,
    val size: Int
) : Node(name, parent, mutableListOf()) {
    override fun toString(): String {
        return "FileNode(name='$name', size=$size)" // parent=${parent?.name},
    }
}


sealed class Sth

data class CdCommand(val dir: String) : Sth()

object LsCommand : Sth()

data class DirectoryName(val name: String) : Sth()

data class File(val size: Int, val name: String) : Sth()