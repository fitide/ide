package org.main.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

import org.main.ide.editor.Editor
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.terminal.Terminal

@Composable
@Preview
fun App() {
    MaterialTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {

                Box(modifier = Modifier.weight(0.25f).fillMaxHeight()) {
                    FileExplorer()
                }

                VerticalDivider(color = Color.DarkGray)

                Box(modifier = Modifier.weight(0.75f).fillMaxHeight()) {
                    Editor()
                }
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.DarkGray)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
            ) {
                Terminal()
            }
        }
    }
}

@Composable
fun VerticalDivider(color: Color, thickness: Dp = 5.dp) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}