package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;

import java.util.ArrayList;
import java.util.List;

import static org.ide.editor.TextFieldValueHelperKt.*;

class EditorFile {
    private MutableState<TextFieldValue> value = null;
    private boolean saved;
    private List<TextFieldValue> versionsList;
    private int currentVersion;

    public EditorFile(List<String> contentLines) {
        saved = true;
        StringBuilder s = new StringBuilder();
        for (String line : contentLines) {
            s.append(line).append('\n');
        }
        String initial = s.toString();
        this.value = getMutableStateTextFieldValue(initial);

        versionsList = new ArrayList<>();
        versionsList.add(this.value.getValue());
        currentVersion = 0;
    }

    public String getContent() {
        return this.value.getValue().getText();
    }

    public void setContent(String newContent) {
        value.setValue(getTextFieldValue(newContent));
        versionsList = new ArrayList<>();
        versionsList.add(value.getValue());
        currentVersion = 0;
        saved = false;
    }

    public void save() {
        saved = true;
    }

    public boolean isSaved() {
        return saved;
    }

    public boolean canRedo() {
        return currentVersion < versionsList.size() - 1;
    }

    public boolean canUndo() {
        return currentVersion > 0;
    }

    public void undo() {
        if (currentVersion > 0) {
            currentVersion--;
            value.setValue(versionsList.get(currentVersion));
        }
    }

    public void redo() {
        if (currentVersion < versionsList.size() - 1) {
            currentVersion++;
            value.setValue(versionsList.get(currentVersion));
        }
    }

    public void onTextChanged(TextFieldValue newValue) {
        if (currentVersion < versionsList.size() - 1) {
            versionsList = new ArrayList<>(versionsList.subList(0, currentVersion + 1));
        }

        versionsList.add(newValue);
        currentVersion = versionsList.size() - 1;

        saved = false;
        value.setValue(newValue);
    }

    public MutableState<TextFieldValue> getTextField() {
        return value;
    }
}
