package org.main.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.main.ide.buttonbar.ButtonBarHorizontal
import org.main.ide.buttonbar.ButtonBarVertical
import org.main.ide.editor.Editor
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.fileexplorer.FileExplorerView
import org.main.ide.terminal.Terminal

@Composable
@Preview
fun App(fileExplorer: FileExplorer) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Верхняя панель
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.DarkGray)
                ) {
                    ButtonBarHorizontal()
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color.Black
                )

                // Основная область
                Row(modifier = Modifier.fillMaxSize()) {
                    // Левая панель
                    Box(
                        modifier = Modifier
                            .width(60.dp)  // уменьшено
                            .fillMaxHeight()
                            .background(Color.Gray)
                    ) {
                        ButtonBarVertical()
                    }

                    VerticalDivider(color = Color.DarkGray)

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.7f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.2f)  // уменьшено
                                    .fillMaxHeight()
                            ) {
                                FileExplorerView(fileExplorer.currentPath)
                            }

                            VerticalDivider(color = Color.DarkGray)

                            Box(
                                modifier = Modifier
                                    .weight(0.8f)
                                    .fillMaxHeight()
                            ) {
                                Editor()
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 2.dp,
                            color = Color.DarkGray
                        )

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
