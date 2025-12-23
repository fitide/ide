package org.main.ide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import org.ide.IdeController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.main.ide.buttonbar.ButtonBarHorizontal
import org.main.ide.buttonbar.ButtonBarVertical
import org.main.ide.config.Config
import org.main.ide.config.ConfigView
import org.main.ide.config.parseConfigsJson
import org.main.ide.editor.EditorView
import org.main.ide.editor.tabs.EditorTabs
import org.main.ide.fileexplorer.FileExplorer
import org.main.ide.fileexplorer.FileExplorerView
import org.main.ide.terminal.Terminal
import org.main.ide.uistate.UIColors.Background
import org.main.ide.uistate.UIColors.ButtonBg
import org.main.ide.uistate.UIColors.ButtonText
import org.main.ide.uistate.UIColors.DividerColor
import org.main.ide.uistate.UIColors.EditorBg
import org.main.ide.uistate.UIColors.Panel
import org.main.ide.uistate.UIColors.PanelElevated
import org.main.ide.uistate.UIColors.TextPrimary
import org.main.ide.uistate.UIColors.TextSecondary
import org.main.ide.uistate.UIState
import java.io.File

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
fun App(
    ideController: IdeController,
    fileExplorer: FileExplorer,
    uiState: UIState,
) {
    var isConfigOpen by remember { mutableStateOf(false) }
    var showNoProjectDialog by remember { mutableStateOf(false) }
    var configs by remember { mutableStateOf(listOf(Config())) }
    var selectedConfigIndex by remember { mutableStateOf(0) }

    LaunchedEffect(isConfigOpen) {
        if (isConfigOpen) {
            val file: File? = ideController.config
            if (file != null && file.exists() && file.length() > 0) {
                runCatching {
                    val json = file.readText()
                    val parsed = parseConfigsJson(json)
                    configs = parsed
                    selectedConfigIndex = 0.coerceAtMost(parsed.lastIndex)
                }.onFailure {
                    configs = listOf(Config())
                    selectedConfigIndex = 0
                }
            } else {
                configs = listOf(Config())
                selectedConfigIndex = 0
            }
        }
    }

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
                        .background(Background)
                        .padding(horizontal = 12.dp)
                ) {
                    ButtonBarHorizontal(
                        onConfigClick = {
                            if (fileExplorer.currentProject == null) {
                                showNoProjectDialog = true
                            } else {
                                isConfigOpen = true
                            }
                        }
                    )
                }

                ThinDivider(vertical = false)

                Row(Modifier.fillMaxSize()) {
                    Box(
                        Modifier
                            .width(56.dp)
                            .fillMaxHeight()
                            .background(Background)
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
                                        ideController = ideController,
                                        fileExplorer = fileExplorer
                                    )
                                }
                            }

                            RoundedPanel(
                                modifier = Modifier
                                    .weight(if (uiState.isFileExplorerVisible) 0.78f else 1f)
                                    .fillMaxHeight(),
                                bg = EditorBg
                            ) {
                                Column(Modifier.fillMaxSize()) {
                                    EditorTabs(
                                        ide = ideController,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    ThinDivider(vertical = false)

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        EditorView(ideController)
                                    }
                                }
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

            if (isConfigOpen) {
                DialogWindow(
                    onCloseRequest = { isConfigOpen = false },
                    title = "CDM-8 Build / Run Configuration",
                    state = rememberDialogState(
                        size = DpSize(900.dp, 700.dp)
                    ),
                    resizable = false
                ) {
                    ConfigView(
                        configs = configs,
                        selectedIndex = selectedConfigIndex,
                        onSelectedChange = { newIndex ->
                            selectedConfigIndex = newIndex
                        },
                        ideController = ideController,
                        onCancel = {
                            isConfigOpen = false
                        }
                    )
                }
            }

            if (showNoProjectDialog) {
                AlertDialog(
                    onDismissRequest = { showNoProjectDialog = false },
                    containerColor = PanelElevated,
                    titleContentColor = TextPrimary,
                    textContentColor = TextSecondary,
                    shape = RoundedCornerShape(10.dp),
                    title = {
                        Text(
                            text = "No project opened",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                    },
                    text = {
                        Text(
                            text = "Open a directory first to use Run/Build configurations.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showNoProjectDialog = false },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = ButtonBg,
                                contentColor = ButtonText
                            )
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {}
                )
            }
        }
    }
}
