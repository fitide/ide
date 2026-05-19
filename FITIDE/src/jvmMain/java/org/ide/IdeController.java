package org.ide;

import androidx.compose.runtime.MutableState;
import androidx.compose.ui.text.input.TextFieldValue;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Exceptions.UnnableToWriteInFileException;
import org.ide.FileExplorerController.FileExplorerController;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.LinkTreeController.LinkTreeController;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.PluginController.PluginController;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.WebWorker.FileSystem.FileSystemComponents.FileType;
import org.ide.WebWorker.Positions.CursorPosition;
import org.ide.WebWorker.Positions.HighlightedPosition;
import org.ide.WebWorker.WebController;
import org.ide.editor.EditorController;
import org.ide.editor.OpenedFileInfo;
import org.ide.editor.TextOperation;

import static org.ide.editor.TextFieldValueHelperKt.getMutableStateForFileTree;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class IdeController implements IdeControllerWebInt {

    private final Logger logger = LogManager.getLogger(IdeController.class);

    private FileExplorerController fileExplorer;
    private final EditorController editorController = new EditorController();
    private PluginController pluginController;
    private LinkTreeController linkTreeController;
    private WebController webController;

    private Path projectRoot;
    private File config;

    private ParseTree currentParseTree = null;
    private Plugin currentPlugin = null;

    private final MutableState<Directory> fileTreeState = getMutableStateForFileTree(null);

    public MutableState<Directory> fileTreeState() {
        return fileTreeState;
    }

    public ParseTree getCurrentParseTree() {
        return currentParseTree;
    }

    public Plugin getCurrentPlugin() {
        return currentPlugin;
    }

    public void setWebController(WebController webController) {
        this.webController = webController;
    }

    public void setLinkTreeController(LinkTreeController linkTreeController) {
        this.linkTreeController = linkTreeController;
        if (this.fileExplorer != null) {
            Directory rootCopy = this.fileExplorer.getTreeCopy();
            this.linkTreeController.setFilesAndDirectoriesData(rootCopy);
        }
    }

    public void openProject(Path root) throws Exception {
        logger.info("Opening project: " + root);
        this.projectRoot = root;
        this.fileExplorer = new FileExplorerController(root.toString(), logger);
        this.config = fileExplorer.getConfig();

        loadPluginsForProject();

        Directory rootCopy = fileExplorer.getTreeCopy();
        fileTreeState.setValue(rootCopy);
        if (linkTreeController != null) {
            linkTreeController.setFilesAndDirectoriesData(rootCopy);
        }
    }

    private void loadPluginsForProject() {
        try {
            Path confDir = projectRoot.resolve(".ide").resolve("conf");
            this.pluginController = new PluginController(projectRoot.toString());

            logger.info("Plugins loaded from: " + confDir);
        } catch (Exception e) {
            logger.error("Failed to load plugins for project: " + projectRoot, e);
        }
    }

    public Path getProjectRoot() {
        return projectRoot;
    }

    public File getConfig() {
        return config;
    }

    public Directory getFileTree() {
        if (fileExplorer == null) return null;
        return fileExplorer.getTreeCopy();
    }

    public Directory refreshTree() {
        if (fileExplorer == null) return null;
        Directory updated = fileExplorer.updateTree(projectRoot.toString());
        if (linkTreeController != null) {
            linkTreeController.setFilesAndDirectoriesData(updated);
        }
        fileTreeState.setValue(updated);
        return updated;
    }

    public void createFile(Path dir, String name) throws Exception {
        fileExplorer.createFile(dir, name);
        refreshTree();
    }

    public void createDir(Path dir, String name) throws Exception {
        fileExplorer.createDir(dir, name);
        refreshTree();
    }

    public void createFileShared(Path dir, String name) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(dir).toString();
            webController.create(rel, FileType.REGULAR, name);
        } else {
            createFile(dir, name);
        }
    }

    public void createDirShared(Path dir, String name) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(dir).toString();
            webController.create(rel, FileType.DIRECTORY, name);
        } else {
            createDir(dir, name);
        }
    }

    public void deleteFile(Path path) throws Exception {
        fileExplorer.deleteFile(path);
        refreshTree();
    }

    public void deleteDir(Path path) throws Exception {
        fileExplorer.deleteDirectory(path);
        refreshTree();
    }

    public void deleteFileShared(Path path) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(path).toString();
            webController.delete(rel, FileType.REGULAR);
        } else {
            deleteFile(path);
        }
    }

    public void deleteDirShared(Path path) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(path).toString();
            webController.delete(rel, FileType.DIRECTORY);
        } else {
            deleteDir(path);
        }
    }

    public void renameFile(Path path, String newName) throws Exception {
        fileExplorer.renameFile(path, newName);
        refreshTree();
    }

    public void renameDir(Path path, String newName) throws Exception {
        fileExplorer.renameDirectory(path, newName);
        refreshTree();
    }

    public void renameFileShared(Path path, String newName) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(path).toString();
            webController.rename(rel, newName, FileType.REGULAR);
        } else {
            renameFile(path, newName);
        }
    }

    public void renameDirShared(Path path, String newName) throws Exception {
        if (webController != null) {
            String rel = projectRoot.relativize(path).toString();
            webController.rename(rel, newName, FileType.DIRECTORY);
        } else {
            renameDir(path, newName);
        }
    }

    public void moveFile(Path from, Path toDir) throws Exception {
        fileExplorer.moveFile(from, toDir);
        refreshTree();
    }

    public void moveDir(Path from, Path toDir) throws Exception {
        fileExplorer.moveDir(from, toDir);
        refreshTree();
    }

    public void copyFile(Path from, Path toDir) throws Exception {
        fileExplorer.copyFile(from, toDir);
        refreshTree();
    }

    public void copyDir(Path from, Path toDir) throws Exception {
        fileExplorer.copyDir(from, toDir);
        refreshTree();
    }


    public String openFile(Path path) throws Exception {
        if (fileExplorer == null)
            throw new IllegalStateException("Project not opened");

        var rp = projectRoot.relativize(path);
        if (webController != null) {
            webController.updateFile(rp.toString());
        }
        List<String> list = fileExplorer.openFile(path);
        editorController.openFile(path.toString(), list);

        initializeFile(path);

        return String.join("\n", list);
    }

    private void initializeFile(Path path) {
        if (pluginController == null || linkTreeController == null) {
            return;
        }

        try {
            String ext = detectLang(path);
            if (ext.isEmpty()) {
                return;
            }

            Plugin currentPlugin = pluginController.getPluginByExtension(ext);
            if (currentPlugin == null) {
                return;
            }
            this.currentPlugin = currentPlugin;

            File file = path.toFile();
            if (!file.exists()) {
                return;
            }

            ParseTree tree = currentPlugin.getFileParseTree(file);
            this.currentParseTree = tree;

            Path relativePath = projectRoot.relativize(path);
            List<Pair<Path, ParseTree>> files = List.of(new Pair<>(relativePath, tree));
            linkTreeController.initFiles(currentPlugin, files);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateContent(Path path, String newContent) {
        editorController.updateContent(path.toString(), newContent);
    }

    public String getContent(Path path) {
        return editorController.getContent(path.toString());
    }

    public void save(Path path) throws Exception {
        String text = editorController.saveFile(path.toString());
        List<String> lines = Arrays.asList(text.split("\n"));
        fileExplorer.saveChangesToFile(path, lines);
    }

    public void save() throws Exception {
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) return;
        String text = editorController.saveFile(currentFile);
        List<String> lines = Arrays.asList(text.split("\n"));
        fileExplorer.saveChangesToFile(Paths.get(currentFile), lines);
    }

    public void redo() {
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) return;
        editorController.redo(currentFile);
    }

    public void undo() {
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) return;
        editorController.undo(currentFile);
    }

    public String redo(Path path) {
        editorController.redo(path.toString());
        return editorController.getContent(path.toString());
    }

    public String undo(Path path) {
        editorController.undo(path.toString());
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

    @Override
    public List<org.ide.WebWorker.Tools.Pair<String, FileType>> getDirData(String relativePath) throws Exception {
        List<org.ide.WebWorker.Tools.Pair<String, FileType>> resList = new ArrayList<>();
        var dir = fileExplorer.getTreeCopy().findDir(relativePath);
        for (int i = 0; i < dir.getDirsCnt(); i++) {
            var subDir = dir.getDir(i);
            resList.add(new org.ide.WebWorker.Tools.Pair<>(Paths.get(relativePath, subDir.name).toString(), FileType.DIRECTORY));
        }
        for (int i = 0; i < dir.getFilesCnt(); i++) {
            var subFile = dir.getFile(i);
            resList.add(new org.ide.WebWorker.Tools.Pair<>(Paths.get(relativePath, subFile.name).toString(), FileType.REGULAR));
        }

        return resList;
    }

    @Override
    public List<String> getFileContent(String relativePath) throws Exception {
        Path rel = Paths.get(relativePath);
        Path stripped = rel.getNameCount() > 1 ? rel.subpath(1, rel.getNameCount()) : rel;
        Path absolutePath = projectRoot.resolve(stripped);
        var fileText = openFile(absolutePath);
        return List.of(fileText.split("\n"));
    }

    @Override
    public void setDir(org.ide.WebWorker.FileSystem.FileSystemComponents.Directory dir) {
        if (projectRoot == null) {
            String name = Paths.get(dir.getRelativeDirPath()).getFileName().toString();
            Path targetDir = chooseTargetDirectory();
            if (targetDir == null) {
                logger.info("setDir cancelled: no target directory selected");
                return;
            }
            projectRoot = targetDir.resolve(name);
        }

        for (var entry : dir.getInboundsList()) {
            if (entry.getType() == FileType.DIRECTORY) {
                Path rel = Paths.get(entry.getRelativePath());
                Path stripped = rel.getNameCount() > 1 ? rel.subpath(1, rel.getNameCount()) : Paths.get("");
                if (stripped.getNameCount() > 0) projectRoot.resolve(stripped).toFile().mkdirs();
            }
        }

        if (fileExplorer == null) {
            try {
                openProject(projectRoot);
            } catch (Exception e) {
                logger.error("setDir failed", e);
            }
        }
    }

    private Path chooseTargetDirectory() {
        final Path[] result = new Path[1];
        Runnable task = () -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File selected = chooser.getSelectedFile();
                if (selected != null) result[0] = selected.toPath();
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                task.run();
            } else {
                SwingUtilities.invokeAndWait(task);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Directory selection interrupted", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            logger.error("Directory selection failed", e);
        }
        return result[0];
    }

    @Override
    public void setFile(org.ide.WebWorker.FileSystem.FileSystemComponents.File file) {
        if (projectRoot == null) return;
        Path rel = Paths.get(file.getRelativeFilePath());
        Path stripped = rel.getNameCount() > 1 ? rel.subpath(1, rel.getNameCount()) : rel;
        Path path = projectRoot.resolve(stripped);
        try {
            path.getParent().toFile().mkdirs();
            Files.writeString(path, String.join("\n", file.getContentList()));
        } catch (IOException e) {
            logger.error("setFile failed", e);
        }
        refreshTree();
    }

    @Override
    public boolean insertText(String filePath, String text, CursorPosition position) {
        return editorController.insertText(filePath, text, position);
    }

    @Override
    public boolean deleteText(String filePath, String textToDelete, HighlightedPosition position) {
        return editorController.deleteText(filePath, textToDelete, position);
    }

    @Override
    public boolean changeText(String filePath, String textToDelete, String newText, HighlightedPosition position) {
        return editorController.changeText(filePath, textToDelete, newText, position);
    }

    public OpenedFileInfo getOpenedFileInfo() {
        return editorController.getOpenedFileInfo();
    }

    public MutableState<OpenedFileInfo> openedFileInfoState() {
        return editorController.openedFileInfoState();
    }

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pending;

    public void onTextChanged(TextFieldValue newValue) {
        if (webController == null) {
            editorController.onTextChanged(newValue);

            String currentFile = editorController.getCurrentFile();
            if (currentFile == null) return;

            Path path = Paths.get(currentFile);

            if (pending != null) pending.cancel(false);
            pending = exec.schedule(() -> analyzeAndUpdateLinkTree(path), 120, TimeUnit.MILLISECONDS);
        } else {
            var operation = editorController.getOperationType(newValue);
            if (operation.operation == TextOperation.Insert && operation.text.equals("")) {
                editorController.onTextChanged(newValue);
                return;
            }

            switch (operation.operation) {
                case Insert -> webController.insertText(editorController.getCurrentFile(), operation.text,
                        CursorPosition.newBuilder().setLineNumer(operation.position.getLineStart()).setColumnNumber(operation.position.getColumnStart()).build());
                case Delete -> webController.deleteText(editorController.getCurrentFile(), operation.text, operation.position);
                case Changing -> webController.changeText(editorController.getCurrentFile(), operation.text,
                        operation.newText, operation.position);
            }
        }
    }

    public void applyConfig(List<String> config) throws UnnableToWriteInFileException, IOException {
        this.config = fileExplorer.applyConfig(config);
    }

    public List<CodeStrForColour> getSyntaxHighlightingForCurrentFile() {
        if (linkTreeController == null) {
            return Collections.emptyList();
        }
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) {
            return Collections.emptyList();
        }
        Path absolutePath = Paths.get(currentFile);
        Path relativePath = projectRoot.relativize(absolutePath);

        try {
            List<CodeStrForColour> result = linkTreeController.getSyntaxHighlightning(relativePath);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<HintNode> getHintsForCurrentFile(String prefix) {
        if (linkTreeController == null) {
            return Collections.emptyList();
        }
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) {
            return Collections.emptyList();
        }
        Path absolutePath = Paths.get(currentFile);
        Path relativePath = projectRoot.relativize(absolutePath).normalize();
        try {
            return new ArrayList<>(linkTreeController.getHintsForFile(relativePath, prefix));
        } catch (Exception e) {
            logger.error("Failed to get hints for " + relativePath, e);
            return Collections.emptyList();
        }
    }

    private void analyzeAndUpdateLinkTree(Path path) {
        if (pluginController == null || linkTreeController == null) {
            return;
        }

        try {
            String ext = detectLang(path);
            if (ext == null || ext.isEmpty()) {
                return;
            }

            Plugin plugin = pluginController.getPluginByExtension(ext);
            if (plugin == null) {
                return;
            }
            this.currentPlugin = plugin;

            String content = editorController.getContent(path.toString());
            if (content == null) {
                return;
            }

            Path shadow = getShadowFilePath(path);

            Files.createDirectories(shadow.getParent());
            Files.writeString(
                    shadow,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );

            ParseTree tree = plugin.getFileParseTree(shadow.toFile());
            this.currentParseTree = tree;

            Path relativePath = projectRoot.relativize(path);
            List<Pair<Path, ParseTree>> files = List.of(new Pair<>(relativePath, tree));
            linkTreeController.updateTree(plugin, files);

        } catch (Exception e) {
            logger.error("Failed to analyze file " + path, e);
        }
    }

    private Path getShadowFilePath(Path originalPath) {
        Path relative = projectRoot.relativize(originalPath);
        return projectRoot
                .resolve(".fitide-cache")
                .resolve(relative);
    }

    private String detectLang(Path path) {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        if (dot == -1 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1);
    }

    public String getCompileString(List<Path> pathsToFiles, String confName) throws Exception {
        StringBuilder compileStringBuilder = new StringBuilder();

        if (pathsToFiles.isEmpty()) throw new RuntimeException("No files to compile");
        var ext = detectLang(pathsToFiles.getFirst());
        var lang = pluginController.getLangNameByExtension(ext);
        var confs = pluginController.getLangsConfs(lang);
        if (confs.isEmpty()) throw new Exception("No conf to compile");
        if (confs.containsKey(confName)) {
            compileStringBuilder.append(pluginController.getCompileString(lang, confName));
        } else {
            compileStringBuilder.append(pluginController.getCompileString(lang, confs.values().iterator().next().name));
        }

        // TODO: add build controller string

        for (var path : pathsToFiles) {
            compileStringBuilder.append(path.toString()).append(" ");
        }

        return compileStringBuilder.toString();
    }
}