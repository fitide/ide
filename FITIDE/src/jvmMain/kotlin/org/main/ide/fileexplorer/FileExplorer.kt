package org.main.ide.fileexplorer

import androidx.compose.runtime.*
import org.ide.IdeController
import org.ide.FileExplorerController.Node.Directory
import java.nio.file.Path
import javax.swing.JFileChooser

class FileExplorer(
    private val ide: IdeController
) {

    var currentProject: Path? by mutableStateOf(null)
        private set

    var fileTree: Directory? by mutableStateOf(null)
        private set

    var selected by mutableStateOf(setOf<String>())
    var lastClicked: String? by mutableStateOf(null)
    var expandedDirs by mutableStateOf(setOf<String>())

    data class ClipboardItem(val path: Path, val isDirectory: Boolean, val cut: Boolean)
    var clipboard: List<ClipboardItem> by mutableStateOf(emptyList())


    fun openDirectoryDialog() {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return

        val dir = chooser.selectedFile.toPath()
        openProject(dir)
    }

    fun openProject(dir: Path) {
        currentProject = dir
        ide.openProject(dir)
        fileTree = ide.getFileTree()

        selected = emptySet()
        lastClicked = null
        expandedDirs = emptySet()
    }

    fun refresh() {
        fileTree = ide.refreshTree()
    }

    fun createFile(dir: Path, name: String) {
        ide.createFile(dir, name)
        refresh()
    }

    fun createDirectory(dir: Path, name: String) {
        ide.createDir(dir, name)
        refresh()
    }

    fun delete(path: Path, isDirectory: Boolean) {
        if (isDirectory) ide.deleteDir(path)
        else ide.deleteFile(path)
        refresh()
    }

    fun rename(path: Path, newName: String, isDirectory: Boolean) {
        if (isDirectory) ide.renameDir(path, newName)
        else ide.renameFile(path, newName)
        refresh()
    }

    fun move(from: Path, toDir: Path, isDirectory: Boolean) {
        if (isDirectory) ide.moveDir(from, toDir)
        else ide.moveFile(from, toDir)
        refresh()
    }

    fun copy(from: Path, toDir: Path, isDirectory: Boolean) {
        if (isDirectory) ide.copyDir(from, toDir)
        else ide.copyFile(from, toDir)
        refresh()
    }

    fun cutSelected() {
        clipboard = selected.map { ClipboardItem(Path.of(it), isDir(it), cut = true) }
    }

    fun copySelected() {
        clipboard = selected.map { ClipboardItem(Path.of(it), isDir(it), cut = false) }
    }

    fun paste(targetDir: Path) {
        clipboard.forEach {
            if (it.cut) move(it.path, targetDir, it.isDirectory)
            else copy(it.path, targetDir, it.isDirectory)
        }
        clipboard = emptyList()
        refresh()
    }

    fun pasteToSelection() {
        val first = selected.firstOrNull() ?: return
        val firstPath = Path.of(first)

        val targetDir: Path = if (isDir(first)) {
            firstPath
        } else {
            firstPath.parent ?: return
        }

        paste(targetDir)
    }

    fun click(path: String, ctrl: Boolean, shift: Boolean) {
        val current = selected

        val newSelection: Set<String> = when {
            shift -> {
                setOf(path)
            }

            ctrl -> {
                if (current.contains(path)) {
                    current - path
                } else {
                    current + path
                }
            }

            else -> {
                setOf(path)
            }
        }

        selected = newSelection
        lastClicked = path
    }

    fun deleteSelected() {
        val snapshot = selected.toList()
        snapshot.forEach { p ->
            delete(Path.of(p), isDir(p))
        }
        selected = emptySet()
    }

    fun toggleExpand(path: String) {
        expandedDirs = if (expandedDirs.contains(path)) {
            expandedDirs - path
        } else {
            expandedDirs + path
        }
    }

    private fun isDir(path: String): Boolean {
        val p = Path.of(path)
        val tree = fileTree ?: return false
        return findNode(tree, p)?.first == true
    }

    private fun findNode(dir: Directory, path: Path): Pair<Boolean, String>? {
        val current = path.fileName?.toString() ?: return null
        if (dir.name == current) return true to dir.name

        for (i in 0 until dir.dirsCnt) {
            val child = dir.getDir(i)
            findNode(child, path)?.let { return it }
        }

        for (i in 0 until dir.filesCnt) {
            val child = dir.getFile(i)
            if (child.name == current) return false to child.name
        }

        return null
    }
}
