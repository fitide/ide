package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Construction;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.Root;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkTreeControllerImpl implements LinkTreeController {
    private Map<String, List<CommonFile>> filesByExtenstion = new HashMap<>();
    private ARoot root;

    @Override
    public void setFilesAndDirectoriesData(Directory FERoot) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        root = new Root(lock);

        root.childs.putAll(setDirsOfNode(FERoot, lock, Paths.get("")));
        root.childs.putAll(setFilesOfNode(FERoot, lock, Paths.get("")));
    }

    private Map<String, CommonFileNode> setDirsOfNode(Directory node, ReentrantReadWriteLock lock, Path pathToFile) {
        Map<String, CommonFileNode> list = new HashMap<>();

        for (int i = 0; i > node.getDirsCnt(); i++) {
            list.put(node.getDir(i).name, setDir(node.getDir(i), lock, pathToFile));
        }

        return list;
    }

    private CommonFileNode setDir(Directory node, ReentrantReadWriteLock lock, Path pathToFile) {
        org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory curDir = new org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory(
                lock, Paths.get(pathToFile.toString(), node.name), node.name);

        curDir.childs.putAll(setDirsOfNode(node, lock, curDir.pathToFile));
        curDir.childs.putAll(setFilesOfNode(node, lock, curDir.pathToFile));
        return curDir;
    }

    private Map<String, CommonFileNode> setFilesOfNode(Directory node, ReentrantReadWriteLock lock, Path pathToFile) {
        Map<String, CommonFileNode> list = new HashMap<>();

        for (int i = 0; i < node.getFilesCnt(); i++) {
            FEFile file = node.getFile(i);
            list.put(file.name, setFile(file, lock, pathToFile));
        }

        return list;
    }

    private String getExtension(String name) {
        int lastP = name.lastIndexOf('.');
        return name.substring(lastP);
    }

    private CommonFileNode setFile(FEFile fileNode, ReentrantReadWriteLock lock, Path pathToFile) {
        var file =  new CommonFile(lock, Paths.get(pathToFile.toString(), fileNode.name), fileNode.name);
        String ext = getExtension(fileNode.name);
        if (!filesByExtenstion.containsKey(ext)) {
            filesByExtenstion.put(ext, new ArrayList<>());
        }
        filesByExtenstion.get(ext).add(file);
        return file;
    }

    @Override
    public void initFiles(Plugin plugin, List<Pair<Path, ParseTree>> files) {

        String ext = plugin.fileExtension();
        if (filesByExtenstion.containsKey(ext)) {
            for (var filePathAndParseTree : files) {
                var fileNode = root.getFileNode(filePathAndParseTree.a);
                if (fileNode != null && fileNode instanceof CommonFile) {
                    var file = (CommonFile) fileNode;
                    file.initCode(plugin, filePathAndParseTree.b, filePathAndParseTree.a);
                }
            }
        }

        for (var file: filesByExtenstion.get(ext)) {
            if (file.isInited) file.setDefs(root);
        }

    }

    private void setExternals(Plugin plugin) {
        var constrsInfo = plugin.getStandartConstructionsLike();
        Map<String, Construction> constMap = new HashMap<>();
        for (var constr : constrsInfo) {
            constMap.put(constr.name, )

        }
    }

    @Override
    public void getHintsForFile(Path pathToNodule) {

    }

    @Override
    public void getSyntaxHighlightning(Path pathToFile) {

    }

    @Override
    public Pair<Path, LinkTreePosition> getDefinition() {
        return null;
    }

    @Override
    public Pair<Path, LinkTreePosition> getDeclaration() {
        return null;
    }
}
