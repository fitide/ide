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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class IdeController {

    private final Logger logger = LogManager.getLogger(IdeController.class);

    private FileExplorerController fileExplorer;
    private final EditorController editorController = new EditorController();
    private PluginController pluginController;
    private LinkTreeController linkTreeController;

    private Path projectRoot;
    private File config;

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

    public Directory refreshTree() {
        if (fileExplorer == null) return null;
        Directory updated = fileExplorer.updateTree(projectRoot.toString());
        if (linkTreeController != null) {
            linkTreeController.setFilesAndDirectoriesData(updated);
        }
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

    public void deleteFile(Path path) throws Exception {
        fileExplorer.deleteFile(path);
        refreshTree();
    }

    public void deleteDir(Path path) throws Exception {
        fileExplorer.deleteDirectory(path);
        refreshTree();
    }

    public void renameFile(Path path, String newName) throws Exception {
        fileExplorer.renameFile(path, newName);
        refreshTree();
    }

    public void renameDir(Path path, String newName) throws Exception {
        fileExplorer.renameDirectory(path, newName);
        refreshTree();
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

        List<String> list = fileExplorer.openFile(path);
        editorController.openFile(path.toString(), list);

        initializeFile(path);

        return String.join("\n", list);
    }

    private void initializeFile(Path path) {
        if (pluginController == null || linkTreeController == null) {
            System.out.println("[WARN] Cannot initialize file: controllers are null");
            return;
        }

        try {
            String ext = detectLang(path);
            if (ext == null || ext.isEmpty()) {
                System.out.println("[WARN] Cannot detect extension for file: " + path);
                return;
            }

            Plugin currentPlugin = pluginController.getPluginByExtension(ext);
            if (currentPlugin == null) {
                System.out.println("[WARN] No plugin found for extension: " + ext);
                return;
            }

            File file = path.toFile();
            if (!file.exists()) {
                System.out.println("[WARN] File does not exist: " + path);
                return;
            }

            System.out.println("[DEBUG] Parsing file: " + path + " with extension: " + ext);

            ParseTree tree = currentPlugin.getFileParseTree(file);

            Path relativePath = projectRoot.relativize(path);
            System.out.println("[DEBUG] Initializing file with relative path: " + relativePath);

            List<Pair<Path, ParseTree>> files = List.of(new Pair<>(relativePath, tree));

            linkTreeController.initFiles(currentPlugin, files);

            System.out.println("[INFO] Successfully initialized file: " + relativePath);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to initialize file " + path);
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

    public void onTextChanged(TextFieldValue newValue) {
        editorController.onTextChanged(newValue);

        String currentFile = editorController.getCurrentFile();
        if (currentFile != null) {
            analyzeAndUpdateLinkTree(Paths.get(currentFile));
        }
    }

    public void applyConfig(List<String> config) throws UnnableToWriteInFileException, IOException {
        this.config = fileExplorer.applyConfig(config);
    }

    public List<CodeStrForColour> getSyntaxHighlightingForCurrentFile() {
        if (linkTreeController == null) {
            System.out.println("[WARN] LinkTreeController is null");
            return Collections.emptyList();
        }
        String currentFile = editorController.getCurrentFile();
        if (currentFile == null) {
            System.out.println("[WARN] Current file is null");
            return Collections.emptyList();
        }
        Path absolutePath = Paths.get(currentFile);
        Path relativePath = projectRoot.relativize(absolutePath);

        System.out.println("[DEBUG] Getting syntax highlighting for: " + relativePath + " (absolute: " + absolutePath + ")");

        try {
            List<CodeStrForColour> result = linkTreeController.getSyntaxHighlightning(relativePath);
            System.out.println("[DEBUG] Syntax highlighting tokens count: " + result.size());
            return result;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to get syntax highlighting for " + relativePath);
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

            Plugin currentPlugin = pluginController.getPluginByExtension(ext);
            if (currentPlugin == null) {
                return;
            }

            File file = path.toFile();
            ParseTree tree = currentPlugin.getFileParseTree(file);

            Path relativePath = projectRoot.relativize(path);
            List<Pair<Path, ParseTree>> files = List.of(new Pair<>(relativePath, tree));

            linkTreeController.updateTree(currentPlugin, files);
        } catch (Exception e) {
            logger.error("Failed to analyze file " + path, e);
        }
    }

    private String detectLang(Path path) {
        String fileName = path.getFileName().toString();
        int dot = fileName.lastIndexOf('.');
        if (dot == -1 || dot == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(dot + 1);
    }
}