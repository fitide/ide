package org.ide.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorController {
    private final HashMap<String, EditorFile> files;

    public EditorController() {
        files = new HashMap<>();
    }

    public void openFile(String fileName, List<String> fileContent) {
        files.put(fileName, new EditorFile(fileContent));
    }

    public void closeFile(String filePath) {
        files.remove(filePath);
    }

    public String saveFile(String filePath) {
        var file = files.get(filePath);
        file.save();
        return file.getContent();
    }

    public String getContent(String filePath) {
        return files.get(filePath).getContent();
    }

    public List<String> getOpenFiles() {
        return new ArrayList<>(files.keySet());
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

}
