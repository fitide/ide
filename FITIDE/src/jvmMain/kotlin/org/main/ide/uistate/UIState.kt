package org.main.ide.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class UIState {
    var isTerminalVisible by mutableStateOf(true)
        private set

    fun toggleTerminal() {
        isTerminalVisible = !isTerminalVisible
    }
}