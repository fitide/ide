package org.main.ide.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import org.apache.logging.log4j.*
import org.ide.FileExplorerController.FileExplorerController
import org.ide.FileExplorerController.Node.Directory
import org.ide.FileExplorerController.Node.FEFile
import org.ide.FileExplorerController.Node.Node
import java.io.File
import javax.swing.JFileChooser

class FileExplorer() {
    var currentPath: String by mutableStateOf("")
        private set
    var fileTree: Directory? by mutableStateOf(null)
        private set

    private lateinit var fileExplorerController: FileExplorerController

    private val logger: Logger = LogManager.getLogger(FileExplorer::class.java)

    init {
        logger.info("FileExplorer создан. Ожидание выбора проекта.")
    }

    private fun isControllerInitialized(): Boolean {
        return ::fileExplorerController.isInitialized
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
                    logger.info("Открыт файл: $selectedPath")
                    logger.info("Дерево файлов построено: ${this.fileTree?.name}")

                    // TODO: Отправить 'selectedFile' в Editor
                    //this.fileExplorerController.openFile(Path(selectedPath))
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
            logger.info("Проект закрыт.")
        }
    }
}

@Composable
fun FileTreeNode(node: Node) {
    if (node is Directory) {
        var isExpanded by remember { mutableStateOf(true) }

        Column {
            Row(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 2.dp)
            ) {
                Icon(
                    Icons.Filled.Folder,
                    contentDescription = "Directory",
                    modifier = Modifier.padding(end = 4.dp).size(20.dp),
                    tint = Color(0xFFFFA000)
                )
                Text(node.name, fontWeight = FontWeight.Bold)
            }

            if (isExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    for (i in 0 until node.getFilesCnt()) {
                        FileTreeNode(node = node.getFile(i))
                    }
                    for (i in 0 until node.getDirsCnt()) {
                        FileTreeNode(node = node.getDir(i))
                    }
                }
            }
        }
    } else if (node is FEFile) {
        Row(
            modifier = Modifier
                .clickable { /* TODO: Open file in Editor */ }
                .padding(vertical = 2.dp)
        ) {
            Icon(
                Icons.Filled.InsertDriveFile,
                contentDescription = "File",
                modifier = Modifier.padding(end = 4.dp).size(20.dp)
            )
            Text(node.name)
        }
    }
}


@Composable
fun FileExplorerView(fileExplorer: FileExplorer) {
    val currentPath = fileExplorer.currentPath
    val fileTree = fileExplorer.fileTree

    Box(
        Modifier
            .background(Color.Gray.copy(alpha = 0.8f))
            .fillMaxSize()
            .padding(4.dp)
    ) {
        if (currentPath.isEmpty()) {
            Text("Проект не открыт. Нажмите 'File' для выбора директории.", color = Color.White)
        } else if (fileTree != null) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    FileTreeNode(node = fileTree)
                }
            }
        } else {
            Text("Загрузка дерева... $currentPath", color = Color.White)
        }
    }
}