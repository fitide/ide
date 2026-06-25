package org.ide.editor

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.ide.FileExplorerController.Node.Directory
import org.ide.WebWorker.Positions.CursorPosition

//TODO: огромный костыль со странным именем!!!
fun getMutableStateTextFieldValue(str : String): MutableState<TextFieldValue> {
    return mutableStateOf(TextFieldValue(str))
}

fun getTextFieldValue(str : String): TextFieldValue {
    return TextFieldValue(str)
}

fun getTextFieldValue(str : String, cursor: Int): TextFieldValue {
    return TextFieldValue(str, selection = TextRange(cursor, cursor))
}

fun getMutableStateTextFieldValueFromNewValue(value : TextFieldValue) : MutableState<TextFieldValue> {
    return mutableStateOf(value)
}

fun getMutableStateForOpenedFileInfo(value: OpenedFileInfo?) : MutableState<OpenedFileInfo?> {
    return mutableStateOf(value)
}

fun getMutableStateForFileTree(value: Directory?) : MutableState<Directory?> {
    return mutableStateOf(value)
}

fun getSelection(value: TextFieldValue) : Int {
    return value.selection.start
}