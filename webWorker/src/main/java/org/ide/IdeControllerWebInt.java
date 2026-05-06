package org.ide;

import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.Tools.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    public List<Pair<String, FileType>> getDirData(String relativePath);

    public List<String> getFileContent(String relativePath);
}
