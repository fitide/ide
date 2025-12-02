package org.main.ide.terminal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import applyCustomScrollBar
import applyDarkBackground
import javax.swing.SwingUtilities

@Composable
fun Terminal(controller: TerminalController) {

    SwingPanel(
        modifier = Modifier.fillMaxSize(),
        factory = {
            controller.widget
        },
        update = { widget ->
            SwingUtilities.invokeLater {
                applyCustomScrollBar(widget)
                applyDarkBackground(widget)
            }
        }
    )
}
