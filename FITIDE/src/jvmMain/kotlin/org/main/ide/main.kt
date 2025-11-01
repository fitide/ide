package org.main.ide

import androidx.compose.runtime.remember
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.main.ide.fileexplorer.FileExplorer

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FITIDE",
    ) {
        val fileExplorer = remember { FileExplorer() }

        MenuBar {
            Menu("File", mnemonic = 'F') {
                Item("Open File", onClick = { fileExplorer.openFileDialog() })
                Item("Open Directory", onClick = { fileExplorer.openDirectoryDialog() })
            }
        }
        App(fileExplorer)
    }
}
