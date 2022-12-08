package pl.rstanski.adventofcode2022.day08.common

class Forrest(val xSize: Int, val ySize: Int) {

    private val trees = MutableList(xSize) { MutableList(ySize) { 0 } }

    fun putTree(x: Int, y: Int, height: Int) {
        trees[x][y] = height
    }

    fun getTree(x: Int, y: Int): Tree =
        Tree(x, y, trees[x][y], this)

    fun allTrees(): Sequence<Tree> =
        sequence {
            (0 until xSize).forEach { x ->
                (0 until ySize).forEach { y ->
                    yield(getTree(x, y))
                }
            }
        }

    fun goLeftFrom(tree: Tree): Sequence<Tree> =
        when {
            tree.isFromBoarder() -> emptySequence()
            else -> sequence {
                (tree.x - 1 downTo 0).forEach { x ->
                    yield(getTree(x, tree.y))
                }
            }
        }

    fun goRightFrom(tree: Tree): Sequence<Tree> =
        when {
            tree.isFromBoarder() -> emptySequence()
            else -> sequence {
                (tree.x + 1 until xSize).forEach { x ->
                    yield(getTree(x, tree.y))
                }
            }
        }

    fun goTopFrom(tree: Tree): Sequence<Tree> =
        when {
            tree.isFromBoarder() -> emptySequence()
            else -> sequence {
                (tree.y + 1 until ySize).forEach { y ->
                    yield(getTree(tree.x, y))
                }
            }
        }

    fun goBottomFrom(tree: Tree): Sequence<Tree> =
        when {
            tree.isFromBoarder() -> emptySequence()
            else -> sequence {
                (tree.y - 1 downTo 0).forEach { y ->
                    yield(getTree(tree.x, y))
                }
            }
        }
}

fun Sequence<Tree>.findHigherOrEqualsThen(tree: Tree): Tree? =
    this.find { it.height >= tree.height }