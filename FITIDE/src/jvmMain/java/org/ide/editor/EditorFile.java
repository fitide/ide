package org.ide.editor;

import java.util.ArrayList;
import java.util.List;

class EditorFile {
    private final StringBuilder content;
    private boolean saved;
    private List<String> versionsList;
    private int currentVersion;
    private Cursor cursor;

    public EditorFile(List<String> contentLines) {
        saved = true;
        this.content = new StringBuilder();
        for (String s : contentLines) {
            this.content.append(s).append('\n');
        }
        versionsList = new ArrayList<>();
        currentVersion = 0;
        cursor = new Cursor();
        cursor.line = 0;
        cursor.position = 0;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String newContent) {
        if (!versionsList.isEmpty()) {
            versionsList = new ArrayList<>(versionsList.subList(0, currentVersion + 1));
        }

        versionsList.add(content.toString());
        currentVersion = versionsList.size() - 1;

        content.setLength(0);
        content.append(newContent);
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
            content.setLength(0);
            content.append(versionsList.get(currentVersion));
        }
    }

    public void redo() {
        if (currentVersion < versionsList.size() - 1) {
            currentVersion++;
            content.setLength(0);
            content.append(versionsList.get(currentVersion));
        }
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}
