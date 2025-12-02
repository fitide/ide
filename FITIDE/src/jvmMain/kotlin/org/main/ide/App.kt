package org.main.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.main.ide.buttonbar.ButtonBarHorizontal
import org.main.ide.buttonbar.ButtonBarVertical
import org.main.ide.editor.Editor
import org.main.ide.editor.EditorState
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.fileexplorer.FileExplorerView
import org.main.ide.terminal.Terminal
import org.main.ide.uistate.UIState

private val Background    = Color(0xFF2B2D30)
private val Panel         = Color(0xFF323438)
private val PanelElevated = Color(0xFF3A3C40)
private val EditorBg      = Color(0xFF282A2D)
private val DividerColor  = Color(0xFF45474B)

@Composable
private fun RoundedPanel(
    modifier: Modifier = Modifier,
    bg: Color = Panel,
    radius: Int = 10,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius.dp))
            .background(bg)
    ) { content() }
}

@Composable
private fun ThinDivider(
    vertical: Boolean = true,
    color: Color = DividerColor,
    thickness: Int = 2
) {
    if (vertical) {
        Box(
            Modifier
                .fillMaxHeight()
                .width(thickness.dp)
                .background(color)
        )
    } else {
        Box(
            Modifier
                .fillMaxWidth()
                .height(thickness.dp)
                .background(color)
        )
    }
}

@Composable
@Preview
fun App(
    fileExplorer: FileExplorer,
    uiState: UIState,
    editorState: EditorState
) {
    MaterialTheme {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFF2B2D30))
                        .padding(horizontal = 12.dp)
                ) {
                    ButtonBarHorizontal()
                }

                ThinDivider(vertical = false)

                Row(Modifier.fillMaxSize()) {
                    Box(
                        Modifier
                            .width(56.dp)
                            .fillMaxHeight()
                            .background(Color(0xFF2B2D30))
                    ) {
                        ButtonBarVertical(uiState)
                    }

                    ThinDivider(vertical = true)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(if (uiState.isTerminalVisible) 0.68f else 1f),
                        ) {

                            if (uiState.isFileExplorerVisible) {
                                RoundedPanel(
                                    modifier = Modifier
                                        .weight(0.22f)
                                        .fillMaxHeight()
                                        .padding(end = 8.dp),
                                    bg = Panel
                                ) {
                                    FileExplorerView(
                                        fileExplorer = fileExplorer,
                                        editorState = editorState
                                    )
                                }
                            }

                            RoundedPanel(
                                modifier = Modifier
                                    .weight(if (uiState.isFileExplorerVisible) 0.78f else 1f)
                                    .fillMaxHeight(),
                                bg = EditorBg
                            ) {
                                Editor(editorState)
                            }
                        }

                        if (uiState.isTerminalVisible) {

                            Spacer(Modifier.height(8.dp))

                            RoundedPanel(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.32f),
                                bg = PanelElevated
                            ) {
                                Terminal(controller = uiState.terminalController)
                            }
                        }
                    }
                }
            }
        }
    }
}
