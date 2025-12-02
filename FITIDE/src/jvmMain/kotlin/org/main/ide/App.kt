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
import org.ide.IdeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.main.ide.buttonbar.ButtonBarHorizontal
import org.main.ide.buttonbar.ButtonBarVertical
import org.main.ide.editor.EditorView
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.fileexplorer.FileExplorerView
import org.main.ide.terminal.Terminal
import org.main.ide.uistate.UIState

@Composable
@Preview
fun App(
    ideController: IdeController,
    fileExplorer: FileExplorer,
    uiState: UIState
) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2B2B2B))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFF3C3F41))
                ) {
                    ButtonBarHorizontal()
                }

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Black
                )

                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .fillMaxHeight()
                            .background(Color(0xFF3C3F41))
                    ) {
                        ButtonBarVertical(uiState = uiState)
                    }

                    VerticalDivider(color = Color.Black)

                    Column(modifier = Modifier.fillMaxSize()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(if (uiState.isTerminalVisible) 0.7f else 1f)
                        ) {

                            Box(
                                modifier = Modifier
                                    .weight(0.22f)
                                    .fillMaxHeight()
                            ) {
                                FileExplorerView(
                                    ideController = ideController,
                                    fileExplorer = fileExplorer
                                )
                            }

                            VerticalDivider(color = Color.Black)

                            Box(
                                modifier = Modifier
                                    .weight(0.78f)
                                    .fillMaxHeight()
                            ) {
                                EditorView(ideController)
                            }
                        }

                        if (uiState.isTerminalVisible) {

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.Black
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.3f)
                            ) {
                                val workingDir = fileExplorer.currentProject?.toString()
                                    ?: System.getProperty("user.home")

                                Terminal(workingDirectory = workingDir)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalDivider(color: Color, thickness: Dp = 1.dp) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}
