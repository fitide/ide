package org.ide;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.ide.FileExplorerController.FileExplorerController;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.editor.Cursor;
import org.ide.editor.EditorController;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class IdeController {

    private final Logger logger = LogManager.getLogger(IdeController.class);

    private FileExplorerController fileExplorer;
    private final EditorController editorController = new EditorController();

    private Path projectRoot;

    public void openProject(Path root) throws Exception {
        logger.info("Opening project: " + root);
        this.projectRoot = root;
        this.fileExplorer = new FileExplorerController(root.toString(), logger);
    }

    public Directory getFileTree() {
        if (fileExplorer == null) return null;
        return fileExplorer.getTreeCopy();
    }

    public Directory refreshTree() {
        if (fileExplorer == null) return null;
        return fileExplorer.updateTree(projectRoot.toString());
    }

    public void createFile(Path dir, String name) throws Exception {
        fileExplorer.createFile(dir, name);
    }

    public void createDir(Path dir, String name) throws Exception {
        fileExplorer.createDir(dir, name);
    }

    public void deleteFile(Path path) throws Exception {
        fileExplorer.deleteFile(path);
    }

    public void deleteDir(Path path) throws Exception {
        fileExplorer.deleteDirectory(path);
    }

    public void renameFile(Path path, String newName) throws Exception {
        fileExplorer.renameFile(path, newName);
    }

    public void renameDir(Path path, String newName) throws Exception {
        fileExplorer.renameDirectory(path, newName);
    }

    public void moveFile(Path from, Path toDir) throws Exception {
        fileExplorer.moveFile(from, toDir);
    }

    public void moveDir(Path from, Path toDir) throws Exception {
        fileExplorer.moveDir(from, toDir);
    }

    public void copyFile(Path from, Path toDir) throws Exception {
        fileExplorer.copyFile(from, toDir);
    }

    public void copyDir(Path from, Path toDir) throws Exception {
        fileExplorer.copyDir(from, toDir);
    }


    public String openFile(Path path) throws Exception {
        if (fileExplorer == null)
            throw new IllegalStateException("Project not opened");

        List<String> list = fileExplorer.openFile(path);;
        editorController.openFile(path.toString(), list);

        return String.join("\n", list);
    }

    public void updateContent(Path path, String newContent) {
        editorController.updateContent(path.toString(), newContent);
    }

    public String getContent(Path path) {
        return editorController.getContent(path.toString());
    }

    public void updateCursor(Path path, Cursor cursor) {
        editorController.updateCursor(path.toString(), cursor);
    }

    public Cursor getCursor(Path path) {
        return editorController.getCursor(path.toString());
    }

    public void save(Path path) throws Exception {
        String text = editorController.saveFile(path.toString());
        List<String> lines = Arrays.asList(text.split("\n"));
        fileExplorer.saveChangesToFile(path, lines);
    }

    public String undo(Path path) {
        editorController.undo(path.toString());
        return editorController.getContent(path.toString());
    }

    public String redo(Path path) {
        editorController.redo(path.toString());
        return editorController.getContent(path.toString());
    }

    public boolean canUndo(Path path) {
        return editorController.canUndo(path.toString());
    }

    public boolean canRedo(Path path) {
        return editorController.canRedo(path.toString());
    }

    public boolean hasUnsavedChanges(Path path) {
        return editorController.hasUnsavedChanges(path.toString());
    }
}
