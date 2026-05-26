package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Tools.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.ide.editor.TextFieldValueHelperKt.getMutableStateForOpenedFileInfo;

public class EditorController {
    private final HashMap<String, EditorFileInt> files;
    private String currentFile = null;
    private final MutableState<OpenedFileInfo> openedFileInfoState = getMutableStateForOpenedFileInfo(null);


    public EditorController() {
        files = new HashMap<>();
    }

    public void openFile(String fileName, List<String> fileContent) {
        var editorFile = new EditorFileWeb(fileContent);
        files.put(fileName, editorFile);
        currentFile = fileName;

        openedFileInfoState.setValue(
                new OpenedFileInfo(editorFile.getTextField())
        );
    }

    public void closeFile(String filename) {
        files.remove(filename);
    }

    public String saveFile(String fileName) {
        var file = files.get(fileName);
        file.save();
        return file.getContent();
    }

    public String getContent(String filePath) {
        return files.get(filePath).getContent();
    }

    public List<String> getOpenFiles() {
        return new ArrayList<>(files.keySet());
    }

    //нам это точно надо?
    public void updateContent(String filePath, String newContent) {
        files.get(filePath).setContent(newContent);
    }

    public void undo(String filePath) {
        files.get(filePath).undo();
    }

    public void redo(String filePath) {
        files.get(filePath).redo();
    }

    public boolean canUndo(String filePath) {
        return files.get(filePath).canUndo();
    }

    public boolean canRedo(String filePath) {
        return files.get(filePath).canRedo();
    }

    public boolean hasUnsavedChanges(String filePath) {
        return !files.get(filePath).isSaved();
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void onTextChanged(TextFieldValue newValue) {
        if (currentFile != null) {
            files.get(currentFile).onTextChanged(newValue);
        }
    }

    public OpenedFileInfo getOpenedFileInfo() {
        return openedFileInfoState.getValue();
    }

    public MutableState<OpenedFileInfo> openedFileInfoState() {
        return openedFileInfoState;
    }

    public OperationInfo getOperationType(TextFieldValue newValue) {
        if (currentFile != null) {
            return files.get(currentFile).getOperation(newValue);
        }
        return null;
    }

    public boolean insertText(String filePath, String text, CursorPosition position, boolean isMe) {
        try {
            if (files.containsKey(filePath)) {
                var file = files.get(filePath);
                file.insertText(text, position, isMe);
            }
        } catch (ChangeTextUnnavailableException e) {
            return false;
        }
        return true;
    }

    public boolean deleteText(String filePath, String textToDelete, HighlightedPosition position, boolean isMe) {
        try {
            if (files.containsKey(filePath)) {
                var file = files.get(filePath);
                file.deleteText(textToDelete, position, isMe);
            }
        } catch (ChangeTextUnnavailableException e) {
            return false;
        }
        return true;
    }

    public boolean changeText(String filePath, String textToDelete, String newText, HighlightedPosition position, boolean isMe) {
        try {
            if (files.containsKey(filePath)) {
                var file = files.get(filePath);
                file.changeText(textToDelete, newText, position, isMe);
            }
        } catch (ChangeTextUnnavailableException e) {
            return false;
        }
        return true;
    }
}
