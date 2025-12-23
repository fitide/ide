package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.ide.editor.TextFieldValueHelperKt.getMutableStateForOpenedFileInfo;

public class EditorController {
    private final HashMap<String, EditorFile> files;
    private String currentFile = null;
    private final MutableState<OpenedFileInfo> openedFileInfoState = getMutableStateForOpenedFileInfo(null);


    public EditorController() {
        files = new HashMap<>();
    }

    public void openFile(String fileName, List<String> fileContent) {
        var editorFile = new EditorFile(fileContent);
        files.put(fileName, editorFile);
        currentFile = fileName;

        openedFileInfoState.setValue(
                new OpenedFileInfo(editorFile.getTextField())
        );
    }

    public TextFieldValue changeOpenedFile(String fileName) {
        currentFile = fileName;
        openedFileInfoState.setValue(
                new OpenedFileInfo(files.get(currentFile).getTextField())
        );
        return files.get(currentFile).getTextField().getValue();
    }

    public void closeFile(String filename) {
        if (currentFile.equals(filename)) {
            if (files.size() != 0) {
                currentFile = files.keySet().iterator().next();
            } else {
                currentFile = null;
            }
        }

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

    public List<Path> getOpenFiles() {
        var paths = new ArrayList<Path>();
        for (var file : files.keySet()) {
            paths.add(Path.of(file));
        }
        return paths;
    }

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

    public Path getOpenedFilePath() {
        if (currentFile != null) {
            return Path.of(currentFile);
        }

        return null;
    }
}
