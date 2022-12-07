package pl.rstanski.adventofcode2022.day07.common

import java.math.BigInteger
import pl.rstanski.adventofcode2022.common.sum

sealed class Node(
    open val name: String,
    open val parent: DirectoryNode?,
)

class FileNode(
    override val name: String,
    val size: Int,
    override val parent: DirectoryNode
) : Node(name, parent)

class DirectoryNode(
    override val name: String,
    override val parent: DirectoryNode? = null,
    private val children: MutableMap<String, Node> = mutableMapOf()
) : Node(name, parent) {

    fun createFileIfNotExists(fileName: String, size: Int): FileNode {
        val existingNode = children[fileName]

        return if (existingNode != null) {
            require(existingNode is FileNode)
            existingNode
        } else {
            val fileNode = FileNode(fileName, size, this)
            children.putIfAbsent(fileName, fileNode)

            fileNode
        }
    }

    fun createDirectoryIfNotExists(directoryName: String): DirectoryNode {
        val existingNode = children[directoryName]

        return if (existingNode != null) {
            require(existingNode is DirectoryNode)
            existingNode
        } else {
            val directoryNode = DirectoryNode(directoryName, this)
            children.putIfAbsent(directoryName, directoryNode)

            directoryNode
        }
    }

    fun createIfNotExistsAndGoTo(subDirectoryName: String): DirectoryNode {
        return when (subDirectoryName) {
            ".." -> goToParent()
            else -> createDirectoryIfNotExists(subDirectoryName)
        }
    }

    private fun goToParent(): DirectoryNode {
        return parent ?: throw IllegalStateException("No parent for $name")
    }

    fun size(): BigInteger =
        children.values.map { child ->
            when (child) {
                is FileNode -> child.size.toBigInteger()
                is DirectoryNode -> child.size()
            }
        }.sum()

    fun findAllDirectoriesRecursively(): List<DirectoryNode> {
        val myDirectories = children.values.filterIsInstance<DirectoryNode>()
        val childrenDirectories = myDirectories
            .map(DirectoryNode::findAllDirectoriesRecursively)
            .flatten()

        return myDirectories + childrenDirectories
    }
}