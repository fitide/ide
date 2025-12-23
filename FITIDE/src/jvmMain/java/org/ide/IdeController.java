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
import org.ide.editor.EditorController;
import org.ide.editor.OpenedFileInfo;

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

public class IdeController {

    private final Logger logger = LogManager.getLogger(IdeController.class);

    private FileExplorerController fileExplorer;
    private final EditorController editorController = new EditorController();
    private PluginController pluginController;
    private LinkTreeController linkTreeController;

    private Path projectRoot;
    private File config;

    private ParseTree currentParseTree = null;
    private Plugin currentPlugin = null;

    public ParseTree getCurrentParseTree() {
        return currentParseTree;
    }

    public Plugin getCurrentPlugin() {
        return currentPlugin;
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

        if (linkTreeController != null) {
            Directory rootCopy = fileExplorer.getTreeCopy();
            linkTreeController.setFilesAndDirectoriesData(rootCopy);
        }
    }

    private void loadPluginsForProject() {
        try {
            Path confDir = projectRoot.resolve("conf");
            this.pluginController = new PluginController(projectRoot.toString());

            logger.info("Plugins loaded from: " + confDir);
        } catch (Exception e) {
            logger.error("Failed to load plugins for project: " + projectRoot, e);
        }
    }

    public File getConfig() {
        return config;
    }

    public Directory getFileTree() {
        if (fileExplorer == null) return null;
        return fileExplorer.getTreeCopy();
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
        linkTreeController.updateFileName(projectRoot.relativize(path), newName);
        var newPath = Paths.get(path.getParent().toString().toString(), newName);
        editorController.renameFile(path.toString(), newPath.toString());
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

    public OpenedFileInfo getOpenedFileInfo() {
        return editorController.getOpenedFileInfo();
    }

    public MutableState<OpenedFileInfo> openedFileInfoState() {
        return editorController.openedFileInfoState();
    }

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pending;

    public void changeCurrentFile(Path path) {
        if (path == null) return;
        editorController.changeOpenedFile(path.toString());
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) return;

        if (pending != null) pending.cancel(false);
        pending = exec.schedule(() -> analyzeAndUpdateLinkTree(path), 120, TimeUnit.MILLISECONDS);
    }

    public void onTextChanged(TextFieldValue newValue) {
        editorController.onTextChanged(newValue);

        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) return;

        Path path = Paths.get(currentFile);

        if (pending != null) pending.cancel(false);
        pending = exec.schedule(() -> analyzeAndUpdateLinkTree(path), 120, TimeUnit.MILLISECONDS);
    }

    public Path getOpenedFilePath() {
        return editorController.getOpenedFilePath();
    }

    public List<Path> getOpenedFiles() {
        return editorController.getOpenFiles();
    }

    public void closeFile(Path path) {
        editorController.closeFile(path.toString());
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