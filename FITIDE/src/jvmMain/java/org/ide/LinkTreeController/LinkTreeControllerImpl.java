package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;
import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Construction;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Func;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Var;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.Root;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

        for (int i = 0; i < node.getDirsCnt(); i++) {
            list.put(node.getDir(i).name, setDir(node.getDir(i), lock, pathToFile));
        }

        return list;
    }

    private CommonFileNode setDir(Directory node, ReentrantReadWriteLock lock, Path pathToFile) {
        org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory curDir = new org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory(
                lock, Paths.get(pathToFile.toString(), node.name), node.name);

        Path newPath = Paths.get(curDir.pathToFile.toString(), node.name);
        curDir.childs.putAll(setDirsOfNode(node, lock, newPath));
        curDir.childs.putAll(setFilesOfNode(node, lock, newPath));
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
        if (lastP != -1) return name.substring(lastP);
        else return "";
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

        initFilesCode(plugin, files);

        setExternals(plugin);

        for (var file: filesByExtenstion.get(ext)) {
            if (file.isInited) file.setLinks(root);
        }
    }

    private void initFilesCode(Plugin plugin, List<Pair<Path, ParseTree>> files) {
        for (var filePathAndParseTree : files) {
            var fileNode = root.getFileNode(filePathAndParseTree.a);
            if (fileNode != null && fileNode instanceof CommonFile) {
                var file = (CommonFile) fileNode;
                file.initCode(plugin, filePathAndParseTree.b, filePathAndParseTree.a);
            }
        }
    }

    private void setExternals(Plugin plugin) {
        setStandartTypes(plugin);
        setConstrs(plugin);
        setFuncs(plugin);
        setVars(plugin);
        setExternalFiles(plugin);
    }

    private void setConstrs(Plugin plugin) {
        var constrsInfo = plugin.getStandartConstructionsLike();
        Map<String, Construction> constMap = new HashMap<>();
        for (var constr : constrsInfo) {
            constMap.put(constr.name, new Construction(constr.name, constr.keyWords));
        }
        root.setExternalConstrs(constMap);
    }

    private void setFuncs(Plugin plugin) {
        var funcInfo = plugin.getStandartFuncs();
        Map<String, Func> funcs = new HashMap<>();
        for (var externalFunc : funcInfo) {
            funcs.put(externalFunc.name, new Func(externalFunc.name, List.of(), externalFunc.args, externalFunc.type));
        }
        root.setExternalFunctions(funcs);
    }

    private void setVars(Plugin plugin) {
        var varInfo = plugin.getStandartVars();
        Map<String, Var> vars = new HashMap<>();
        for (var externalVar : varInfo) {
            vars.put(externalVar.Name, new Var(externalVar.Name, List.of(), externalVar.Type.name));
        }
        root.setExternalVars(vars);
    }

    private void setExternalFiles(Plugin plugin) {
        var filesInfo = plugin.getStandartFiles();
        Map<String, CommonFile> files = new HashMap<>();
        for (var file : filesInfo) {
            files.put(file.name, new CommonFile(file));
        }
        root.setExternalFiles(files);
    }

    private void setStandartTypes(Plugin plugin) {
        var extTypes = plugin.getStandartTypes();
        var types = new HashSet<String>();
        for (var type : extTypes) {
            types.add(type.name);
        }
        root.setStandartTypes(types);
    }

    @Override
    public Set<HintNode> getHintsForFile(Path pathToNodule, String prefix) {
        try {
            return root.getHints(pathToNodule, prefix);
        } catch (BadPathException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CodeStrForColour> getSyntaxHighlightning(Path pathToFile) {
        return root.getSyntaxHughlighting(pathToFile);
    }

    @Override
    public void updateTree(Plugin plugin, List<Pair<Path, ParseTree>> files) {
        initFiles(plugin, files);
    }

    @Override
    public ARoot getTree() {
        return root;
    }
}
