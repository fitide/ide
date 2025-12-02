package org.main.ide.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.main.ide.terminal.TerminalController

class UIState {

    val terminalController = TerminalController(System.getProperty("user.home"))
    var isTerminalVisible by mutableStateOf(true)
        private set

    var isFileExplorerVisible by mutableStateOf(true)
        private set

    fun toggleTerminal() {
        isTerminalVisible = !isTerminalVisible
    }

    fun toggleFileExplorer() {
        isFileExplorerVisible = !isFileExplorerVisible
    }
}