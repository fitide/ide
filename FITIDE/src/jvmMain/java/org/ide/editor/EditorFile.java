package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Tools.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.ide.editor.TextFieldValueHelperKt.*;

class EditorFile implements EditorFileInt {
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

    @Override
    public void insertText(String text, CursorPosition position, boolean isMe) {
        throw new RuntimeException("insert text on single user file");
    }

    @Override
    public void deleteText(String textToDelete, HighlightedPosition position, boolean isMe) {
        throw new RuntimeException("delete text on single user file");
    }

    @Override
    public void changeText(String textToDelete, String newText, HighlightedPosition position, boolean isMe) {
        throw new RuntimeException("change text on single user file");
    }

    public MutableState<TextFieldValue> getTextField() {
        return value;
    }

    @Override
    public OperationInfo getOperation(TextFieldValue newValue) {
        var curText = this.getContent();
        var newText = newValue.getText();
        int start = 0;
        int end1 = curText.length() - 1;
        int end2 = newText.length() - 1;
        while(curText.charAt(start) != newText.charAt(start) && start < end1 && start < end2) start++;

        while(curText.charAt(end1) != newText.charAt(end2) && end1 > start && end2 > start) {
            end1--;
            end2--;
        }

        var difLen = end1 - start - 1;
        var startPos = getPosition(curText, start + 1);
        var endPos = getPosition(curText, end1);
        var positions = HighlightedPosition.newBuilder()
                .setColumnStart(startPos.getColumnNumber()).setLineStart(startPos.getLineNumer())
                .setColumnEnd(endPos.getColumnNumber()).setLineEnd(endPos.getLineNumer()).build();

        if (difLen == 0) return new OperationInfo(TextOperation.Insert, positions, newText.substring(start, end2));
        else if (difLen == curText.length() - newText.length()) return new OperationInfo(TextOperation.Delete, positions, curText.substring(start, end1));
        else return new OperationInfo(TextOperation.Changing, positions, curText.substring(start, end1));
    }

    private CursorPosition getPosition(String value, int it) {
        int col = 0;
        int line = 0;
        int curIt = 0;
        while(curIt != it) {
            if (value.charAt(curIt) == '\n') {
                col = 0;
                line += 1;
            }
            curIt++;
        }
        return CursorPosition.newBuilder().setColumnNumber(col).setLineNumer(line).build();
    }
}
