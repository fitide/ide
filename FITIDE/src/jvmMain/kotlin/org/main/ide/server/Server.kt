package org.main.ide.server

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ide.IdeController
import org.ide.WebWorker.WebController
import org.main.ide.uistate.UIColors.AccentBlue
import org.main.ide.uistate.UIColors.AccentGreen
import org.main.ide.uistate.UIColors.Background
import org.main.ide.uistate.UIColors.BorderFocused
import org.main.ide.uistate.UIColors.BorderUnfocused
import org.main.ide.uistate.UIColors.ButtonBg
import org.main.ide.uistate.UIColors.ButtonText
import org.main.ide.uistate.UIColors.Panel
import org.main.ide.uistate.UIColors.TextFieldContainer
import org.main.ide.uistate.UIColors.TextPrimary
import org.main.ide.uistate.UIColors.TextSecondary
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

private enum class CodeWithMeMode {
    IDLE,
    JOIN,
    HOST
}

@Composable
fun CodeWithMeDialog(ideController: IdeController, existingLink: String? = null, hasProject: Boolean = true, onCreated: (WebController) -> Unit, onDismiss: () -> Unit) {
    DialogWindow(
        onCloseRequest = onDismiss,
        title = "Code With Me",
        state = rememberDialogState(size = DpSize(560.dp, 420.dp)),
        resizable = false
    ) {
        CodeWithMeContent(ideController = ideController, existingLink = existingLink, hasProject = hasProject, onCreated = onCreated, onDismiss = onDismiss)
    }
}

@Composable
private fun CodeWithMeContent(ideController: IdeController, existingLink: String? = null, hasProject: Boolean = true, onCreated: (WebController) -> Unit, onDismiss: () -> Unit) {
    var mode by remember { mutableStateOf(if (existingLink != null) CodeWithMeMode.HOST else CodeWithMeMode.IDLE) }
    var joinUrl by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var hostLink by remember { mutableStateOf(existingLink ?: "") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            Text(
                text = "Code With Me",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    letterSpacing = 0.3.sp
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Collaborate in real time with your team.",
                style = TextStyle(
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderUnfocused)
            )

            Spacer(Modifier.height(20.dp))

            when (mode) {
                CodeWithMeMode.IDLE -> {
                    IdleView(
                        onHostClick = { mode = CodeWithMeMode.HOST },
                        onJoinClick = { mode = CodeWithMeMode.JOIN }
                    )
                }

                CodeWithMeMode.HOST -> {
                    HostView(
                        link = hostLink,
                        alreadyStarted = existingLink != null,
                        hasProject = hasProject,
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onStart = { enteredName ->
                            name = enteredName
                            isLoading = true
                            errorMessage = null
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val host = WebController.getLocalIp()
                                    val wc = WebController(ideController, enteredName, host)
                                    ideController.setWebController(wc)
                                    withContext(Dispatchers.Main) {
                                        hostLink = wc.codeToConnect
                                        isLoading = false
                                        onCreated(wc)
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        errorMessage = e.message ?: "Failed to start server"
                                    }
                                }
                            }
                        },
                        onBack = { mode = CodeWithMeMode.IDLE; errorMessage = null }
                    )
                }

                CodeWithMeMode.JOIN -> {
                    JoinView(
                        name = name,
                        onNameChange = { name = it },
                        url = joinUrl,
                        onUrlChange = { joinUrl = it },
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onConnect = {
                            isLoading = true
                            errorMessage = null
                            scope.launch(Dispatchers.IO) {
                                try {
                                    val host = WebController.getLocalIp()
                                    val wc = WebController(ideController, name, host, joinUrl)
                                    ideController.setWebController(wc)
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        onCreated(wc)
                                        onDismiss()
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        isLoading = false
                                        errorMessage = e.message ?: "Failed to connect"
                                    }
                                }
                            }
                        },
                        onBack = { mode = CodeWithMeMode.IDLE; errorMessage = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun IdleView(
    onHostClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionCard(Modifier.weight(1f),
            title = "Create Server",
            description = "Start a session\nand share the link",
            accentColor = AccentGreen,
            onClick = onHostClick
        )

        ActionCard(
            modifier = Modifier.weight(1f),
            title = "Join Server",
            description = "Enter a link to\njoin someone's session",
            accentColor = AccentBlue,
            onClick = onJoinClick
        )
    }
}

@Composable
private fun ActionCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Panel)
            .border(
                width = 1.dp,
                color = BorderUnfocused,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(accentColor)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )

            Text(
                text = description,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            )
        }
    }
}

@Composable
private fun HostView(
    link: String,
    alreadyStarted: Boolean = false,
    hasProject: Boolean = true,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onStart: (name: String) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val started = alreadyStarted || link.isNotEmpty()

    LaunchedEffect(Unit) { if (!started) focusRequester.requestFocus() }

    if (!started) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Your name",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            )

            BasicTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                cursorBrush = SolidColor(BorderFocused),
                textStyle = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = TextPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .clip(RoundedCornerShape(7.dp))
                    .background(TextFieldContainer)
                    .border(1.dp, if (name.isNotEmpty()) BorderFocused else BorderUnfocused, RoundedCornerShape(7.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                decorationBox = { inner ->
                    if (name.isEmpty()) {
                        Text("Name", style = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = TextSecondary.copy(alpha = 0.5f)))
                    }
                    inner()
                }
            )

            if (!hasProject) {
                Text(
                    text = "Open a project first to create a server.",
                    style = TextStyle(fontSize = 12.sp, color = Color(0xFFE57373))
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    style = TextStyle(fontSize = 12.sp, color = Color(0xFFE57373))
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                BackButton(onClick = onBack)
                Spacer(Modifier.weight(1f))
                if (isLoading) {
                    Text(
                        text = "Starting...",
                        style = TextStyle(fontSize = 13.sp, color = TextSecondary)
                    )
                } else {
                    val canStart = name.isNotBlank() && hasProject
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(7.dp))
                            .background(if (canStart) AccentGreen.copy(alpha = 0.85f) else ButtonBg)
                            .clickable(enabled = canStart) { onStart(name) }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Start",
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (canStart) Color.White else TextSecondary
                            )
                        )
                    }
                }
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Your session link",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp))
                    .background(TextFieldContainer)
                    .border(1.dp, BorderUnfocused, RoundedCornerShape(7.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = link,
                    style = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = TextSecondary),
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(ButtonBg)
                        .clickable { copyToClipboard(link) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "Copy", style = TextStyle(fontSize = 12.sp, color = ButtonText))
                }
            }

            Spacer(Modifier.height(4.dp))
            BackButton(onClick = onBack)
        }
    }
}

@Composable
private fun JoinView(
    name: String,
    onNameChange: (String) -> Unit,
    url: String,
    onUrlChange: (String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onConnect: () -> Unit,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        BasicTextField(
            value = name,
            onValueChange = onNameChange,
            singleLine = true,
            cursorBrush = SolidColor(BorderFocused),
            textStyle = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = TextPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .clip(RoundedCornerShape(7.dp))
                .background(TextFieldContainer)
                .border(1.dp, if (name.isNotEmpty()) BorderFocused else BorderUnfocused, RoundedCornerShape(7.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            decorationBox = { inner ->
                if (name.isEmpty()) {
                    Text("Name", style = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace, color = TextSecondary.copy(alpha = 0.5f)))
                }
                inner()
            }
        )

        Text(
            text = "Paste session link",
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        )

        BasicTextField(
            value = url,
            onValueChange = onUrlChange,
            singleLine = true,
            cursorBrush = SolidColor(BorderFocused),
            textStyle = TextStyle(
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .clip(RoundedCornerShape(7.dp))
                .background(TextFieldContainer)
                .border(
                    width = 1.dp,
                    color = if (url.isNotEmpty()) {
                        BorderFocused
                    } else {
                        BorderUnfocused
                    },
                    shape = RoundedCornerShape(7.dp)
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),

            decorationBox = { inner ->

                if (url.isEmpty()) {
                    Text(
                        text = "URL",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextSecondary.copy(alpha = 0.5f)
                        )
                    )
                }

                inner()
            }
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = TextStyle(fontSize = 12.sp, color = Color(0xFFE57373))
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            BackButton(onClick = onBack)

            Spacer(Modifier.weight(1f))

            if (isLoading) {
                Text(
                    text = "Connecting...",
                    style = TextStyle(fontSize = 13.sp, color = TextSecondary)
                )
            } else {
                val canConnect = url.isNotBlank() && name.isNotBlank()
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(if (canConnect) AccentBlue.copy(alpha = 0.85f) else ButtonBg)
                        .clickable(enabled = canConnect, onClick = onConnect)
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Присоединиться",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (canConnect) Color.White else TextSecondary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(7.dp))
            .background(ButtonBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = "← Назад",
            style = TextStyle(
                fontSize = 13.sp,
                color = TextSecondary
            )
        )
    }
}

private fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(text)

    clipboard.setContents(selection, selection)
}