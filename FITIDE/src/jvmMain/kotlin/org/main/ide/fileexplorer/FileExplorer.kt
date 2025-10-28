package org.main.ide.fileexplorer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import org.apache.log4j.*

import org.ide.FileExplorerController.FileExplorerController
import java.io.File
import javax.swing.JFileChooser
import kotlin.io.path.Path

class FileExplorer() {
    var currentPath: String = ""
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
            this.currentPath = selectedPath!!
            this.fileExplorerController = FileExplorerController(this.currentPath, logger)

            logger.info("Открыта директория: ${this.currentPath}")
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
            this.currentPath = selectedPath!!

            this.fileExplorerController = FileExplorerController(this.currentPath, logger)

            val selectedFile = File(this.currentPath)
            if (selectedFile.isFile) {
                this.fileExplorerController.openFile(Path(this.currentPath))
            }

            logger.info("Открыт файл: ${this.currentPath}")
        }
    }

    fun closeProject() {
        if (isControllerInitialized()) {
            this.currentPath = ""
            logger.info("Проект закрыт.")
        }
    }
}

@Composable
fun FileExplorerView(currentPath: String) {
    Box(Modifier.background(Color.Gray).fillMaxSize()) {
        if (currentPath.isEmpty()) {
            Text("Проект не открыт. Нажмите кнопку 'File' для выбора директории.")
        } else {
            Text("Открытая директория:\n$currentPath")
        }
    }
}