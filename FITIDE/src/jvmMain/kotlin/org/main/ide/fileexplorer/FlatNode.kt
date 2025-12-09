package org.main.ide.fileexplorer

import org.ide.FileExplorerController.Node.Directory
import org.ide.FileExplorerController.Node.Node
import java.nio.file.Path

data class FlatNode(
    val node: Node,
    val path: String,
    val depth: Int
)

fun flattenTree(
    root: Directory,
    rootPath: Path,
    expanded: Set<String>
): List<FlatNode> {

    val list = mutableListOf<FlatNode>()

    fun walk(node: Node, absolutePath: Path, depth: Int) {
        val pathStr = absolutePath.toString()
        val isRoot = depth == 0

        list += FlatNode(node, pathStr, depth)

        if (node is Directory) {
            if (!isRoot && pathStr !in expanded) return

            for (i in 0 until node.dirsCnt) {
                val child = node.getDir(i)
                walk(child, absolutePath.resolve(child.name), depth + 1)
            }
            for (i in 0 until node.filesCnt) {
                val child = node.getFile(i)
                walk(child, absolutePath.resolve(child.name), depth + 1)
            }
        }
    }

    walk(root, rootPath, 0)
    return list
}
