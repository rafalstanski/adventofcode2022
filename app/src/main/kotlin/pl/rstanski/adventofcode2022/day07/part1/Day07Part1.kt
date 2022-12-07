package pl.rstanski.adventofcode2022.day07.part1

import java.math.BigInteger
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

        println(filesystem.find())

        return filesystem.find()
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

    var total: BigInteger = BigInteger.ZERO

    fun find(): BigInteger {
        val list = mutableListOf<DirectoryNode>()
        structure.find(list)

        val free = BigInteger("70000000") - structure.size()
        val need = BigInteger("30000000") - free

        println("need: " + need)

        val allDirectories = structure.getAllDirectories()
        val find = allDirectories.sortedBy { it.size() }.first { it.size() >= need }

//        val sum = list.sumOf { it.size() }
//        println(sum)

        return find.size()
    }

    fun apply(sth: Sth) {
        println(sth.toString() + "\t\t: " + currentDirectory.toString())
        when (sth) {
            is CdCommand -> {
                when {
                    sth.dir == ".." -> currentDirectory = currentDirectory.parent!!
                    else -> {
                        if (currentDirectory.children.find { it.name == sth.dir } != null) {
                            currentDirectory = currentDirectory.children.find { it.name == sth.dir } as DirectoryNode
                        } else {
                            val dir = DirectoryNode(sth.dir, currentDirectory, mutableListOf())
                            structure.children.add(dir)
                            currentDirectory = dir
                        }
                    }
                }
            }

            is LsCommand -> ignore()
            is DirectoryName -> {
                if (currentDirectory.children.find { it.name == sth.name } == null) {
                    currentDirectory.children.add(DirectoryNode(sth.name, currentDirectory, mutableListOf()))
                }
            }

            is File -> {
                if (currentDirectory.children.find { it.name == sth.name } == null) {
                    currentDirectory.children.add(FileNode(sth.name, currentDirectory, sth.size))
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

    fun size(): BigInteger {
        return children.map { child ->
            when(child) {
                is FileNode -> child.size.toBigInteger()
                is DirectoryNode -> child.size()
            }
        }.sumOf { it }
    }

    fun getAllDirectories(): List<DirectoryNode> {
        val nodes = children.filterIsInstance<DirectoryNode>()
        return listOf(this) + nodes.map { it.getAllDirectories() }.flatMap { it }
    }

    fun find(list:MutableList<DirectoryNode>) {
        val limit = BigInteger("100000")
        if (size() <= limit) {
            list.add(this)
        }

        children.map { child ->
            if(child is DirectoryNode) {
                child.find(list)
            }
        }
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