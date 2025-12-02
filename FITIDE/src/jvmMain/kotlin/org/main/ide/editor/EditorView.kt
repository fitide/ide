package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.ide.IdeController

@Composable
fun EditorView(ideController: IdeController) {

    val openedFileInfo = ideController.openedFileInfoState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    val mod = event.isCtrlPressed || event.isMetaPressed

                    if (mod && event.key == Key.S) {
                        ideController.save()
                        return@onPreviewKeyEvent true
                    }

                    if (mod && event.key == Key.Z) {
                        ideController.undo()
                        return@onPreviewKeyEvent true
                    }

                    if (mod && event.key == Key.Y) {
                        ideController.redo()
                        return@onPreviewKeyEvent true
                    }
                }
                false
            }
    ) {

        if (openedFileInfo != null) {
            BasicTextField(
                value = openedFileInfo.textFieldValue.value,
                onValueChange = {
                    ideController.onTextChanged(it)
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp
                ),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text("No file opened", color = Color.White)
        }
    }
}
