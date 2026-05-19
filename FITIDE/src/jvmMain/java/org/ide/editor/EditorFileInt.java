package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Tools.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.ide.editor.TextFieldValueHelperKt.getMutableStateTextFieldValue;
import static org.ide.editor.TextFieldValueHelperKt.getTextFieldValue;

public interface EditorFileInt {

    public String getContent();

    public void setContent(String newContent);

    public void save();

    public boolean isSaved();

    public boolean canRedo();

    public boolean canUndo();

    public void undo();

    public void redo();

    public void onTextChanged(TextFieldValue newValue);

    public void insertText(String text, CursorPosition position) throws ChangeTextUnnavailableException;

    public void deleteText(String textToDelete, HighlightedPosition position) throws ChangeTextUnnavailableException;

    public void changeText(String textToDelete, String newText, HighlightedPosition position) throws ChangeTextUnnavailableException;

    public MutableState<TextFieldValue> getTextField();

    public OperationInfo getOperation(TextFieldValue newValue);
}
