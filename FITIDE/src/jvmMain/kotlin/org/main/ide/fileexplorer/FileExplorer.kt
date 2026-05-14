package org.main.ide.fileexplorer

import androidx.compose.runtime.*
import org.ide.IdeController
import org.ide.FileExplorerController.Node.Directory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JFileChooser

class FileExplorer(
    private val ideController: IdeController
) {

    var currentProject: Path? by mutableStateOf(null)
        private set

    var fileTree: Directory? by mutableStateOf(null)
        private set

    var selected by mutableStateOf(setOf<String>())
    var lastClicked: String? by mutableStateOf(null)
    var expandedDirs by mutableStateOf(setOf<String>())
    var selectionAnchor: String? by mutableStateOf(null)

    data class ClipboardItem(val path: Path, val isDirectory: Boolean, val cut: Boolean)
    var clipboard: List<ClipboardItem> by mutableStateOf(emptyList())


    fun openDirectoryDialog() {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return

        val dir = Paths.get(chooser.selectedFile.toPath().toString())
        openProject(dir)
    }

    fun openProject(dir: Path) {
        currentProject = dir
        ideController.openProject(dir)
        fileTree = ideController.getFileTree()

        selected = emptySet()
        lastClicked = null
        expandedDirs = emptySet()
        selectionAnchor = null
    }

    fun refresh() {
        fileTree = ideController.refreshFileTree()
    }
    fun createFile(dir: Path, name: String) {
        try {
            ideController.createFile(dir, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        refresh()
    }

    fun createDirectory(dir: Path, name: String) {
        try {
            ideController.createDir(dir, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        refresh()
    }

    fun delete(path: Path, isDirectory: Boolean) {
        try {
            if (isDirectory) ideController.deleteDir(path)
            else ideController.deleteFile(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        refresh()
    }

    fun rename(path: Path, newName: String, isDirectory: Boolean) {

        if (newName.isBlank()) {
            return
        }

        val currentName = path.fileName?.toString() ?: ""
        if (newName == currentName) {
            return
        }

        val targetPath = path.parent?.resolve(newName)

        if (targetPath != null && Files.exists(targetPath)) {
            return
        }

        try {
            if (isDirectory) {
                ideController.renameDir(path, newName)
            } else {
                ideController.renameFile(path, newName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            refresh()
        }
    }

    fun move(from: Path, toDir: Path, isDirectory: Boolean) {
        try {
            if (isDirectory) ideController.moveDir(from, toDir)
            else ideController.moveFile(from, toDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        refresh()
    }

    fun copy(from: Path, toDir: Path, isDirectory: Boolean) {
        try {
            if (isDirectory) ideController.copyDir(from, toDir)
            else ideController.copyFile(from, toDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            if (it.isDirectory && targetDir.startsWith(it.path)) {
                return@forEach
            }

            if (!it.cut && it.isDirectory) {
                val targetChild = targetDir.resolve(it.path.fileName)
                if (Files.exists(targetChild)) {
                    return@forEach
                }
            }

            if (it.cut) {
                move(it.path, targetDir, it.isDirectory)
            } else {
                copy(it.path, targetDir, it.isDirectory)
            }
        }
        clipboard = emptyList()
        refresh()
    }

    fun pasteToSelection() {
        if (clipboard.isEmpty()) return

        if (selected.isEmpty()) {
            val root = currentProject ?: return
            paste(root)
            return
        }

        val selectedList = selected.toList()

        val targetDir: Path = when {
            selectedList.size == 1 -> {
                val only = selectedList.first()
                val onlyPath = Path.of(only)
                if (isDir(only)) {
                    onlyPath
                } else {
                    onlyPath.parent ?: onlyPath
                }
            }

            else -> {
                val selectedPaths = selectedList.map { Path.of(it) }
                val parents = selectedPaths.mapNotNull { it.parent }.toSet()

                when {
                    parents.size == 1 -> parents.first()

                    else -> {
                        val anchorStr = lastClicked ?: selectedList.first()
                        val anchorPath = Path.of(anchorStr)
                        anchorPath.parent ?: anchorPath
                    }
                }
            }
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
        selectionAnchor = path
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
