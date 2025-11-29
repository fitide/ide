package org.ide.editor;

import java.util.ArrayList;
import java.util.List;

class EditorFile {
    private final StringBuilder content;
    private boolean saved;
    private List<String> versionsList;
    private int currentVersion;

    public EditorFile(List<String> content) {
        saved = true;
        this.content = new StringBuilder();
        for (String s : content) {
            this.content.append(s);
        }
        versionsList = new ArrayList<>();
        currentVersion = 0;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String newContent) {
        if (!versionsList.isEmpty()) {
            versionsList = versionsList.subList(0, currentVersion);
        }

        versionsList.add(content.toString());
        content.setLength(0);
        content.append(newContent);
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
}
