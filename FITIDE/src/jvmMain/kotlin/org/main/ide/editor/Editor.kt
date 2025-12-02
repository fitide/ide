package org.main.ide.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun Editor(editorState: EditorState) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .onPreviewKeyEvent { event ->

                if (event.type == KeyEventType.KeyDown) {

                    val mod = event.isCtrlPressed || event.isMetaPressed

                    if (mod && event.key == Key.S) {
                        editorState.saveFile()
                        return@onPreviewKeyEvent true
                    }

                    if (mod && event.key == Key.Z) {
                        editorState.undo()
                        return@onPreviewKeyEvent true
                    }

                    if (mod && event.key == Key.Y) {
                        editorState.redo()
                        return@onPreviewKeyEvent true
                    }
                }

                false
            }
    ) {

        BasicTextField(
            value = editorState.textFieldValue,
            onValueChange = { newValue: TextFieldValue ->
                editorState.onTextChanged(newValue)
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 14.sp
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
