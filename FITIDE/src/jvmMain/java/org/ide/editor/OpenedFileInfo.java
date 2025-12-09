package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;

//TODO:: возможно, это излишне
public class OpenedFileInfo {
    public MutableState<TextFieldValue> textFieldValue;

    public OpenedFileInfo() {
    }

    public OpenedFileInfo(MutableState<TextFieldValue> textFieldValue) {
        this.textFieldValue = textFieldValue;
    }
}