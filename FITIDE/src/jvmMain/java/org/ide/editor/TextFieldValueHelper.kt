package org.ide.editor

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import org.ide.FileExplorerController.Node.Directory

//TODO: огромный костыль со странным именем!!!
fun getMutableStateTextFieldValue(str : String): MutableState<TextFieldValue> {
    return mutableStateOf(TextFieldValue(str))
}

fun getTextFieldValue(str : String): TextFieldValue {
    return TextFieldValue(str)
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