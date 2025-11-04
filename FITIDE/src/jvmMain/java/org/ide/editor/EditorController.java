package org.ide.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditorController {
    static public String defaultEncoding = "UTF-8";

    HashMap<String, EditorFile> files = new HashMap<>();
    String currentFile = null;

    public void addFile(String fileName, List<String> content) {
        var editorFile = new EditorFile();
        editorFile.content = new StringBuilder();
        for (String line : content) {
            editorFile.content.append(line);
        }
        editorFile.encoding = defaultEncoding;
        editorFile.isEdited = false;
        files.put(fileName, editorFile);
        currentFile = fileName;
    }

    public void removeFile(String fileName) {
        files.remove(fileName);
        if (currentFile.equals(fileName)) {
            currentFile = null;
        }
    }

    public void switchFile(String fileName) {
        currentFile = fileName;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public List<String> getCurrentFileContent() {
        String str = files.get(currentFile).content.toString();

        return Arrays.asList(str.split("\n"));
    }

    public void insertText(String sym, int position) {
        if (currentFile == null) {
            throw new RuntimeException("File not opened");
        }
        var file = files.get(currentFile);

        if (position >= file.content.length()) {
            throw new RuntimeException("Position out of bounds");
        }

        file.content.insert((int) position, sym);
        file.isEdited = true;
    }

    public void deleteText(int position) {
        if (currentFile == null) {
            throw new RuntimeException("File not opened");
        }
        var file = files.get(currentFile);

        if (position >= file.content.length()) {
            throw new RuntimeException("Position out of bounds");
        }

        file.content.deleteCharAt(position);
        file.isEdited = true;
    }

    public List<String> saveFile() {
        if (currentFile == null) {
            throw new RuntimeException("File not opened");
        }
        var file = files.get(currentFile);

        file.isEdited = false;

        String str = file.content.toString();
        return Arrays.asList(str.split("\n"));
    }
}
