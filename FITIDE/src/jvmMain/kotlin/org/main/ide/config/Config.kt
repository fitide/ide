package org.main.ide.config

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ide.IdeController
import org.main.ide.uistate.UIColors.Background
import org.main.ide.uistate.UIColors.BorderFocused
import org.main.ide.uistate.UIColors.BorderUnfocused
import org.main.ide.uistate.UIColors.ButtonBg
import org.main.ide.uistate.UIColors.ButtonText
import org.main.ide.uistate.UIColors.DividerColor
import org.main.ide.uistate.UIColors.Panel
import org.main.ide.uistate.UIColors.PanelElevated
import org.main.ide.uistate.UIColors.TextFieldContainer
import org.main.ide.uistate.UIColors.TextPrimary
import org.main.ide.uistate.UIColors.TextSecondary
import javax.swing.JFileChooser

class Config {
    var name by mutableStateOf("")
    var pathToInterpreter by mutableStateOf("")
    var main by mutableStateOf("")
    var outputFile by mutableStateOf("")
    var args by mutableStateOf("")
}

@Serializable
private data class ConfigDto(
    val name: String = "",
    val pathToInterpreter: String = "",
    val main: String = "",
    val outputFile: String = "",
    val args: String = "",
)

private fun ConfigDto.toState(): Config = Config().also { cfg ->
    cfg.name = name
    cfg.pathToInterpreter = pathToInterpreter
    cfg.main = main
    cfg.outputFile = outputFile
    cfg.args = args
}

private fun Config.toDto(): ConfigDto = ConfigDto(
    name = name,
    pathToInterpreter = pathToInterpreter,
    main = main,
    outputFile = outputFile,
    args = args,
)

private val jsonFormat = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun parseConfigsJson(json: String): List<Config> {
    val trimmed = json.trim()
    if (trimmed.isEmpty() || trimmed == "{}" || trimmed == "[]") {
        return listOf(Config())
    }

    return if (trimmed.startsWith("[")) {
        jsonFormat.decodeFromString<List<ConfigDto>>(trimmed)
            .map { it.toState() }
            .ifEmpty { listOf(Config()) }
    } else {
        listOf(
            jsonFormat.decodeFromString<ConfigDto>(trimmed).toState()
        )
    }
}

private val textFieldColors
    @Composable get() = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BorderFocused,
        unfocusedBorderColor = BorderUnfocused,
        focusedLabelColor = TextPrimary,
        unfocusedLabelColor = TextSecondary,
        cursorColor = TextPrimary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        disabledTextColor = TextSecondary,
        disabledLabelColor = TextSecondary,
        disabledBorderColor = BorderUnfocused,
        focusedContainerColor = TextFieldContainer,
        unfocusedContainerColor = TextFieldContainer,
        disabledContainerColor = TextFieldContainer
    )

@Composable
fun ConfigView(
    configs: List<Config>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    ideController: IdeController,
    onCancel: () -> Unit,
) {
    var localConfigs by remember(configs) { mutableStateOf(configs.toMutableList()) }
    var localSelectedIndex by remember(selectedIndex, configs) {
        mutableStateOf(selectedIndex.coerceIn(0, localConfigs.lastIndex))
    }

    val selectedConfig = localConfigs.getOrNull(localSelectedIndex) ?: return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Panel)
        ) {
            Column(
                modifier = Modifier
                    .width(220.dp)
                    .fillMaxHeight()
                    .background(PanelElevated)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Configurations",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                localConfigs.forEachIndexed { index, cfg ->
                    val isSelected = index == localSelectedIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isSelected) TextFieldContainer
                                else PanelElevated
                            )
                            .clickable {
                                localSelectedIndex = index
                                onSelectedChange(index)
                            }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (cfg.name.isNotBlank()) cfg.name else "Unnamed",
                            color = if (isSelected) TextPrimary else TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val newConfig = Config()
                            localConfigs = (localConfigs + newConfig).toMutableList()
                            localSelectedIndex = localConfigs.lastIndex
                            onSelectedChange(localSelectedIndex)

                            persistConfigs(localConfigs, ideController)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("+")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (localConfigs.size <= 1) {
                                val cfg = localConfigs.first()
                                cfg.name = ""
                                cfg.pathToInterpreter = ""
                                cfg.main = ""
                                cfg.outputFile = ""
                                cfg.args = ""
                            } else {
                                val idx = localSelectedIndex.coerceIn(0, localConfigs.lastIndex)
                                localConfigs = localConfigs.toMutableList().also { it.removeAt(idx) }
                                localSelectedIndex =
                                    (idx - 1).coerceAtLeast(0).coerceAtMost(localConfigs.lastIndex)
                                onSelectedChange(localSelectedIndex)
                            }

                            persistConfigs(localConfigs, ideController)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("-")
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(DividerColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Run/Build Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .background(PanelElevated, RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    LabeledField(label = "Name") {
                        OutlinedTextField(
                            value = selectedConfig.name,
                            onValueChange = { selectedConfig.name = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                            colors = textFieldColors
                        )
                    }

                    LabeledField(label = "Interpreter") {
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedConfig.pathToInterpreter,
                                onValueChange = { selectedConfig.pathToInterpreter = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                                colors = textFieldColors
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val chooser = JFileChooser()
                                    chooser.fileSelectionMode = JFileChooser.FILES_ONLY
                                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                        selectedConfig.pathToInterpreter = chooser.selectedFile.absolutePath
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButtonBg,
                                    contentColor = ButtonText
                                )
                            ) {
                                Text("...")
                            }
                        }
                    }

                    LabeledField(label = "Main file") {
                        Row(Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedConfig.main,
                                onValueChange = { selectedConfig.main = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                                colors = textFieldColors
                            )
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val chooser = JFileChooser()
                                    chooser.fileSelectionMode = JFileChooser.FILES_ONLY
                                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                        selectedConfig.main = chooser.selectedFile.absolutePath
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ButtonBg,
                                    contentColor = ButtonText
                                )
                            ) {
                                Text("...")
                            }
                        }
                    }

                    LabeledField(label = "Output") {
                        OutlinedTextField(
                            value = selectedConfig.outputFile,
                            onValueChange = { selectedConfig.outputFile = it },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                            colors = textFieldColors
                        )
                    }

                    LabeledField(label = "Program arguments") {
                        OutlinedTextField(
                            value = selectedConfig.args,
                            onValueChange = { selectedConfig.args = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 60.dp),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                            colors = textFieldColors
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = DividerColor
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 2.dp,
                    color = DividerColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onCancel() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            persistConfigs(localConfigs, ideController)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Apply")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            // TODO: build & run с selectedConfig
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Run ▶︎")
                    }
                }
            }
        }
    }
}

private fun persistConfigs(configs: List<Config>, ideController: IdeController) {
    val dtoList = configs.map { it.toDto() }
    val json = jsonFormat.encodeToString(dtoList)
    ideController.applyConfig(listOf(json))
}

@Composable
private fun LabeledField(
    label: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = label,
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        content()
    }
}