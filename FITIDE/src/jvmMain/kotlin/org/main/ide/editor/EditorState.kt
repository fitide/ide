package org.main.ide.editor

import androidx.compose.runtime.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.ide.IdeController
import org.ide.editor.Cursor
import java.nio.file.Path

class EditorState(
    private val ide: IdeController
) {

    var filePath: Path? by mutableStateOf(null)
    var textFieldValue by mutableStateOf(TextFieldValue(""))

    fun openFile(path: Path) {
        filePath = path

        val content = ide.openFile(path)

        textFieldValue = TextFieldValue(
            text = content,
            selection = TextRange(0)
        )

        syncCursor()
    }

    fun onTextChanged(newValue: TextFieldValue) {
        textFieldValue = newValue
        val fp = filePath ?: return

        ide.updateContent(fp, newValue.text)
        syncCursor()
    }

    private fun syncCursor() {
        val fp = filePath ?: return

        val text = textFieldValue.text
        val offset = textFieldValue.selection.start.coerceIn(0, text.length)

        var line = 0L
        var col = 0L

        for (i in 0 until offset) {
            if (text[i] == '\n') {
                line++
                col = 0
            } else {
                col++
            }
        }

        val cursor = Cursor().apply {
            this.line = line
            this.position = col
        }

        ide.updateCursor(fp, cursor)
    }

    fun saveFile() {
        val fp = filePath ?: return
        ide.save(fp)
    }

    fun undo() {
        val fp = filePath ?: return
        val newText = ide.undo(fp)
        textFieldValue = TextFieldValue(newText, TextRange(newText.length))
    }

    fun redo() {
        val fp = filePath ?: return
        val newText = ide.redo(fp)
        textFieldValue = TextFieldValue(newText, TextRange(newText.length))
    }
}
