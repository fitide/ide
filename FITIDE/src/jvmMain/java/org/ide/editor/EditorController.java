package org.ide.editor;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Tools.Pair;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.ide.editor.TextFieldValueHelperKt.getMutableStateForOpenedFileInfo;
import static org.ide.editor.TextFieldValueHelperKt.getMutableStateTextFieldValue;

public class EditorController {
    private final HashMap<String, EditorFileInt> files;
    private String currentFile = null;
    private final MutableState<OpenedFileInfo> openedFileInfoState = getMutableStateForOpenedFileInfo(null);


    public EditorController() {
        files = new HashMap<>();
    }

    public void openFile(String fileName, List<String> fileContent) {
        EditorFileInt editorFile;
        if (!hasFileOpened(fileName)) {
            editorFile  = new EditorFileWeb(fileContent);
            files.put(fileName, editorFile);
        }
        else {
            editorFile = files.get(fileName);
        }
        currentFile = fileName;

        System.out.println("File opened: " + fileName);
        openedFileInfoState.setValue(
                new OpenedFileInfo(editorFile.getTextField())
        );
    }

    public void createFileNode(String fileName, List<String> fileContent) {
        var editorFile = new EditorFileWeb(fileContent);
        files.put(fileName, editorFile);

        System.out.println("FileInfoCreated " + fileName);
    }

    public void setOpenedFileSnapshot(String fileName) {
        var editorFile = files.get(fileName);
        currentFile = fileName;

        openedFileInfoState.setValue(
                new OpenedFileInfo(editorFile.getTextField())
        );
    }

    public boolean hasFileOpened(String fileOpened) {
        return files.containsKey(fileOpened);
    }

    public TextFieldValue changeOpenedFile(String fileName) {
        currentFile = fileName;
        openedFileInfoState.setValue(
                new OpenedFileInfo(files.get(currentFile).getTextField())
        );
        return files.get(currentFile).getTextField().getValue();
    }

    public void closeFile(String filename) {
        if (currentFile == null) {
            return;
        }

        if (currentFile.equals(filename)) {
            files.remove(filename);
            if (files.size() != 0) {
                currentFile = files.keySet().iterator().next();
                openedFileInfoState.setValue(new OpenedFileInfo(files.get(currentFile).getTextField()));
            } else {
                currentFile = null;
                openedFileInfoState.setValue(new OpenedFileInfo(getMutableStateTextFieldValue("")));
            }
        } else {
            files.remove(filename);
        }


        var openedFileInfo = openedFileInfoState.getValue();
        var newOpenedFileInfo = new OpenedFileInfo(openedFileInfo.textFieldValue);
        openedFileInfoState.setValue(newOpenedFileInfo);
    }

    public String saveFile(String fileName) {
        var file = files.get(fileName);
        file.save();
        var openedFileInfo = openedFileInfoState.getValue();
        var newOpenedFileInfo = new OpenedFileInfo(openedFileInfo.textFieldValue);
        openedFileInfoState.setValue(newOpenedFileInfo);

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
            var openedFileInfo = openedFileInfoState.getValue();
            var newOpenedFileInfo = new OpenedFileInfo(openedFileInfo.textFieldValue);
            openedFileInfoState.setValue(newOpenedFileInfo);
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
                System.out.println("file content " + file.getContent());
            }
            else {
                System.out.println("file " + filePath + " not found");
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
    public void renameFile(String filename, String newName) {
        if (this.files.containsKey(filename)) {
            var file = this.files.get(filename);
            this.files.remove(filename);
            this.files.put(newName, file);

            if (Objects.equals(currentFile, filename)) {
                currentFile = newName;
            }
        }
    }
    public Path getOpenedFilePath() {
        if (currentFile != null) {
            return Path.of(currentFile);
        }

        return null;
    }
}
