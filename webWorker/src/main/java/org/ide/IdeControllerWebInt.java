package org.ide;

import org.ide.WebWorker.FileSystem.FileSystemComponents.Directory;
import org.ide.WebWorker.FileSystem.FileSystemComponents.File;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.Tools.Pair;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public interface IdeControllerWebInt {

    public void createFile(Path dir, String name) throws Exception;

    public void createDir(Path dir, String name) throws Exception;

    public void deleteFile(Path path) throws Exception;

    public void deleteDir(Path path) throws Exception;

    public void renameFile(Path path, String newName) throws Exception;

    public void renameDir(Path path, String newName) throws Exception;

    public void moveFile(Path from, Path toDir) throws Exception;

    public void moveDir(Path from, Path toDir) throws Exception;

    public void copyFile(Path from, Path toDir) throws Exception;

    public void copyDir(Path from, Path toDir) throws Exception;


    public void updateContent(Path path, String newContent);

    public String getContent(Path path);

    public boolean canUndo(Path path);

    public boolean canRedo(Path path);

    public boolean hasUnsavedChanges(Path path);

    public Path getProjectRoot();

    public List<Pair<String, FileType>> getDirData(String relativePath) throws Exception;

    public List<String> getFileContent(String relativePath) throws FileNotFoundException, Exception;

    void setDir(Directory dir);

    void setFile(File file);

    public boolean insertText(String filePath, String text, CursorPosition position, String host);

    public boolean deleteText(String filePath, String textToDelete, HighlightedPosition position, String host);

    public boolean changeText(String filePath, String textToDelete, String newText, HighlightedPosition position, String host);
}
