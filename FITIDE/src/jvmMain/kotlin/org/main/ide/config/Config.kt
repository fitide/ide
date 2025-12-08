package org.main.ide.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.main.ide.uistate.UIColors.Background
import org.main.ide.uistate.UIColors.DividerColor
import org.main.ide.uistate.UIColors.Panel
import org.main.ide.uistate.UIColors.PanelElevated
import org.main.ide.uistate.UIColors.TextPrimary
import org.main.ide.uistate.UIColors.BorderFocused
import org.main.ide.uistate.UIColors.BorderUnfocused
import org.main.ide.uistate.UIColors.ButtonBg
import org.main.ide.uistate.UIColors.ButtonText
import org.main.ide.uistate.UIColors.TextFieldContainer
import org.main.ide.uistate.UIColors.TextSecondary


class Config {
    var name: String = ""
    var pathToInterpreter: String = ""
    var main: String = ""
    var outputFile: String = ""
    var args: String = ""
}

@Composable
fun ConfigView(config: Config) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10.dp))
                .background(Panel)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f, fill = true)
                        .background(PanelElevated)
                        .padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = config.name,
                        onValueChange = { config.name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
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
                    )

                    OutlinedTextField(
                        value = config.pathToInterpreter,
                        onValueChange = { config.pathToInterpreter = it },
                        label = { Text("Path to interpreter") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
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
                    )

                    OutlinedTextField(
                        value = config.main,
                        onValueChange = { config.main = it },
                        label = { Text("Main file") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
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
                    )

                    OutlinedTextField(
                        value = config.outputFile,
                        onValueChange = { config.outputFile = it },
                        label = { Text("Output") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
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
                    )

                    OutlinedTextField(
                        value = config.args,
                        onValueChange = { config.args = it },
                        label = { Text("Args") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        colors = OutlinedTextFieldDefaults.colors(
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
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 12.dp),
                    thickness = 2.dp,
                    color = DividerColor
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { /* TODO: cancel/close */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { /* TODO: apply */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Apply")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { /* TODO: apply & build & run */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonBg,
                            contentColor = ButtonText
                        )
                    ) {
                        Text("Run ▶\uFE0E")
                    }
                }
            }
        }
    }
}
