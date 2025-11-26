package org.main.ide.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ide.fitide.generated.resources.*

import org.apache.logging.log4j.*
import org.ide.FileExplorerController.FileExplorerController
import org.ide.FileExplorerController.Node.Directory
import org.ide.FileExplorerController.Node.FEFile
import org.ide.FileExplorerController.Node.Node
import java.io.File
import java.util.Locale.getDefault
import javax.swing.JFileChooser

import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.MaterialTheme
import javax.swing.JOptionPane
import java.nio.file.Paths

data class FlatNode(
    val node: Node,
    val depth: Int,
    val isExpanded: Boolean?,
    val path: String
)

class FileExplorer() {
    var currentPath: String by mutableStateOf("")
        private set
    var fileTree: Directory? by mutableStateOf(null)
        private set

    var expandedState: Map<String, Boolean> by mutableStateOf(emptyMap())

    private lateinit var fileExplorerController: FileExplorerController

    private val logger: Logger = LogManager.getLogger(FileExplorer::class.java)

    init {
        logger.info("FileExplorer создан. Ожидание выбора проекта.")
    }

    private fun isControllerInitialized(): Boolean {
        return ::fileExplorerController.isInitialized
    }

    val flatFileList: List<FlatNode>
        @Composable get() {
            val root = fileTree ?: return emptyList()
            val list = mutableListOf<FlatNode>()
            buildFlatList(root, 0, list, currentPath)
            return list
        }

    private fun buildFlatList(currentNode: Node, depth: Int, list: MutableList<FlatNode>, currentAbsPath: String) {
        val isDir = currentNode is Directory
        val isExpanded = expandedState[currentAbsPath] ?: (depth == 0)

        list.add(FlatNode(currentNode, depth, if (isDir) isExpanded else null, currentAbsPath))

        if (isDir && isExpanded) {

            for (i in 0 until currentNode.dirsCnt) {
                val child = currentNode.getDir(i)
                buildFlatList(child, depth + 1, list, "$currentAbsPath${File.separator}${child.name}")
            }
            for (i in 0 until currentNode.filesCnt) {
                val child = currentNode.getFile(i)
                buildFlatList(child, depth + 1, list, "$currentAbsPath${File.separator}${child.name}")
            }
        }
    }

    fun toggleExpansion(node: Node, nodePath: String) {
        if (node !is Directory) return
        expandedState = expandedState.toMutableMap().apply {
            val isRoot = nodePath == currentPath
            this[nodePath] = !(this[nodePath] ?: isRoot)
        }
    }

    fun openDirectoryDialog() {
        var selectedPath: String? = null
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        chooser.dialogTitle = "Выберите директорию проекта"
        val result = chooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPath = chooser.selectedFile.absolutePath
        }

        if (selectedPath != null) {
            this.currentPath = selectedPath
            try {
                this.fileExplorerController = FileExplorerController(this.currentPath, logger)
                this.fileTree = this.fileExplorerController.treeCopy
                this.expandedState = mapOf(this.currentPath to true)
                logger.info("Открыта директория: ${this.currentPath}")
                logger.info("Дерево файлов построено: ${this.fileTree?.name}")
            } catch (e: Exception) {
                logger.error("Не удалось инициализировать контроллер: ${e.message}")
                this.currentPath = ""
                this.fileTree = null
            }
        }
    }

    fun openFileDialog() {
        var selectedPath: String? = null
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.FILES_ONLY
        chooser.dialogTitle = "Выберите файл"
        val result = chooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedPath = chooser.selectedFile.absolutePath
        }


        if (selectedPath != null) {
            val selectedFile = File(selectedPath)
            val parentDir = selectedFile.parent

            if (parentDir != null) {
                this.currentPath = parentDir
                try {
                    this.fileExplorerController = FileExplorerController(this.currentPath, logger)
                    this.fileTree = this.fileExplorerController.treeCopy
                    this.expandedState = mapOf(this.currentPath to true)
                    logger.info("Открыт файл: $selectedPath")
                    logger.info("Дерево файлов построено: ${this.fileTree?.name}")
                } catch (e: Exception) {
                    logger.error("Не удалось инициализировать контроллер: ${e.message}")
                    this.currentPath = ""
                    this.fileTree = null
                }
            }
        }
    }

    fun closeProject() {
        if (isControllerInitialized()) {
            this.currentPath = ""
            this.fileTree = null
            this.expandedState = emptyMap()
            logger.info("Проект закрыт.")
        }
    }

    private fun refreshFileTree() {
        if (isControllerInitialized()) {
            this.fileTree = fileExplorerController.treeCopy
            logger.info("Дерево файлов обновлено.")
        }
    }

    fun renameNode(nodePath: String, isDirectory: Boolean) {
        if (!isControllerInitialized()) return

        val newName = JOptionPane.showInputDialog(null, "Введите новое имя:")
        if (newName.isNullOrBlank()) {
            logger.info("Переименование отменено.")
            return
        }

        try {
            val path = Paths.get(nodePath)
            if (isDirectory) {
                fileExplorerController.renameDirectory(path, newName)
            } else {
                fileExplorerController.renameFile(path, newName)
            }
            refreshFileTree()
            logger.info("Переименован $nodePath в $newName")
        } catch (e: Exception) {
            logger.error("Ошибка переименования $nodePath: ${e.message}")
            JOptionPane.showMessageDialog(null, "Ошибка переименования: ${e.message}", "Ошибка", JOptionPane.ERROR_MESSAGE)
        }
    }

    fun createFileInNode(dirPath: String) {
        if (!isControllerInitialized()) return

        val fileName = JOptionPane.showInputDialog(null, "Введите имя нового файла:")
        if (fileName.isNullOrBlank()) {
            logger.info("Создание файла отменено.")
            return
        }

        try {
            val path = Paths.get(dirPath)
            fileExplorerController.createFile(path, fileName)
            expandedState = expandedState.toMutableMap().apply {
                this[dirPath] = true
            }
            refreshFileTree()
            logger.info("Создан файл $fileName в $dirPath")
        } catch (e: Exception) {
            logger.error("Ошибка создания файла в $dirPath: ${e.message}")
            JOptionPane.showMessageDialog(null, "Ошибка создания файла: ${e.message}", "Ошибка", JOptionPane.ERROR_MESSAGE)
        }
    }

    fun deleteNode(nodePath: String, isDirectory: Boolean) {
        if (!isControllerInitialized()) return

        val confirmation = JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите удалить этот элемент?", "Подтвердите удаление", JOptionPane.YES_NO_OPTION)
        if (confirmation != JOptionPane.YES_OPTION) {
            logger.info("Удаление отменено.")
            return
        }

        try {
            val path = Paths.get(nodePath)
            if (isDirectory) {
                fileExplorerController.deleteDirectory(path)
            } else {
                fileExplorerController.deleteFile(path)
            }
            refreshFileTree()
            logger.info("Удален элемент $nodePath")
        } catch (e: Exception) {
            logger.error("Ошибка удаления $nodePath: ${e.message}")
            JOptionPane.showMessageDialog(null, "Ошибка удаления: ${e.message}", "Ошибка", JOptionPane.ERROR_MESSAGE)
        }
    }
}

@Composable
fun FileIconForExtension(file: FEFile): Painter {
    val name = file.name
    val extension = name.substringAfterLast('.', "").lowercase(getDefault())

    val resource: DrawableResource = when (extension) {
        "kt" -> Res.drawable.kotlin
        "java" -> Res.drawable.java
        "go" -> Res.drawable.go
        "py" -> Res.drawable.python
        "c" -> Res.drawable.c
        "cpp" -> Res.drawable.cpp
        "clj" -> Res.drawable.clojure
        "asm" -> Res.drawable.assembly
        "txt" -> Res.drawable.txt
        "md" -> Res.drawable.markdown
        "json" -> Res.drawable.json
        else -> Res.drawable.file
    }

    return painterResource(resource)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlatFileNode(flatNode: FlatNode, fileExplorer: FileExplorer) {
    val node = flatNode.node
    val depth = flatNode.depth
    val isDirectory = node is Directory
    val extensionIcon = if (node is FEFile) FileIconForExtension(node) else null

    val horizontalPadding = (depth * 16).dp
    val startIndent = if (isDirectory) 0.dp else 24.dp

    val menuItems = if (isDirectory) {
        listOf(
            ContextMenuItem("Переименовать") { fileExplorer.renameNode(flatNode.path, isDirectory = true) },
            ContextMenuItem("Создать файл") { fileExplorer.createFileInNode(flatNode.path) },
            ContextMenuItem("Удалить") { fileExplorer.deleteNode(flatNode.path, isDirectory = true) }
        )
    } else {
        listOf(
            ContextMenuItem("Переименовать") { fileExplorer.renameNode(flatNode.path, isDirectory = false) },
            ContextMenuItem("Удалить") { fileExplorer.deleteNode(flatNode.path, isDirectory = false) }
        )
    }

    val customContextMenuColors = MaterialTheme.colorScheme.copy(
        surface = Color(0xA9A9A9),
        onSurface = Color.White
    )

    MaterialTheme(customContextMenuColors) {
        ContextMenuArea(items = { menuItems }) {
            Row(
                modifier = Modifier
                    .clickable {
                        if (isDirectory) {
                            fileExplorer.toggleExpansion(node, flatNode.path)
                        }
                    }
                    .padding(vertical = 2.dp)
                    .padding(start = horizontalPadding)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isDirectory) {
                    Icon(
                        imageVector = if (flatNode.isExpanded == true) Icons.Default.KeyboardArrowDown else Icons.Default.ChevronRight,
                        contentDescription = if (flatNode.isExpanded == true) "Collapse" else "Expand",
                        modifier = Modifier.size(20.dp).padding(end = 4.dp),
                        tint = Color.White
                    )

                    Icon(
                        painter = painterResource(Res.drawable.folder),
                        contentDescription = "Directory",
                        modifier = Modifier.padding(end = 4.dp).size(20.dp),
                        tint = Color.Unspecified
                    )
                    Text(node.name, fontWeight = FontWeight.Bold, color = Color.White)
                }
                else if (node is FEFile && extensionIcon != null) {
                    Spacer(Modifier.width(startIndent))

                    Icon(
                        painter = extensionIcon,
                        contentDescription = "File icon for ${node.name}",
                        modifier = Modifier.padding(end = 4.dp).size(20.dp),
                        tint = Color.Unspecified
                    )

                    Text(node.name, color = Color.White)
                }
            }
        }
    }
}


@Composable
fun FileExplorerView(fileExplorer: FileExplorer) {
    val currentPath = fileExplorer.currentPath
    val fileTree = fileExplorer.fileTree
    val flatList = fileExplorer.flatFileList

    Box(
        Modifier
            .background(Color.Gray)
            .fillMaxSize()
            .padding(4.dp)
    ) {
        if (currentPath.isEmpty()) {
            Text("Проект не открыт. Нажмите 'File' для выбора директории.", color = Color.Black)
        } else if (fileTree != null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(flatList) { flatNode ->
                    FlatFileNode(flatNode = flatNode, fileExplorer = fileExplorer)
                }
            }
        } else {
            Text("Загрузка дерева... $currentPath", color = Color.LightGray)
        }
    }
}