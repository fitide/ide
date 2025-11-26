package org.main.ide

import androidx.compose.runtime.remember
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.uistate.UIState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FITIDE",
    ) {
        val fileExplorer = remember { FileExplorer() }
        val uiState = remember { UIState() }

        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item("Open File", onClick = { fileExplorer.openFileDialog() })
                Item("Open Directory", onClick = { fileExplorer.openDirectoryDialog() })
            }
        }
        App(fileExplorer, uiState)
    }
}
