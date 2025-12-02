package org.main.ide

import androidx.compose.runtime.remember
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.ide.IdeController
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.uistate.UIState
import org.main.ide.editor.EditorView

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FITIDE"
    ) {
        val ideController = remember { IdeController() }

        val fileExplorer = remember { FileExplorer(ideController) }
        val uiState = remember { UIState() }

        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item("Open Directory", onClick = { fileExplorer.openDirectoryDialog() })
            }
        }

        App(
            ideController = ideController,
            fileExplorer = fileExplorer,
            uiState = uiState,
        )
    }
}
