package org.ide.LinkTreeController;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.FileExplorerController.Node.Directory;
import org.ide.FileExplorerController.Node.FEFile;
import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Construction;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Func;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Var;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.Root;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
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

        initFilesCode(plugin, files);

        setExternals(plugin);

        setFilesLinks(files);
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

    private void setFilesLinks(List<Pair<Path, ParseTree>> files) {
        for (var filePathAndParseTree : files) {
            var fileNode = root.getFileNode(filePathAndParseTree.a);
            if (fileNode != null && fileNode instanceof CommonFile) {
                var file = (CommonFile) fileNode;
                file.setLinks(root);
            }
        }
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
        String ext = plugin.fileExtension();
        initFilesCode(plugin, files);
        setFilesLinks(files);
    }

    @Override
    public ARoot getTree() {
        return root;
    }

    @Override
    public void updateFilePath(Path pathToDirWithFileToChange, Path pathToDirToInsert, String fileName) {
        root.updateFilePath(pathToDirWithFileToChange, pathToDirToInsert, fileName);
    }

    @Override
    public void updateFileName(Path pathToFile, String newName) {
        root.updateFileName(pathToFile, newName);
    }

    @Override
    public AInternalCodeNode goToDefinition(Path pathToFile, int row, int col) {
        CommonFileNode n = root.getFileNode(pathToFile);
        if (!(n instanceof CommonFile file)) return null;
        if (!file.isInited) return null;
        if (file.codeNodes == null || file.codeNodes.isEmpty()) return null;

        AInternalCodeNode usage = findUsage(file, row, col);
        if (usage == null) return null;

        if (usage.definition == null) {
            System.out.println("Found usage, but definition is null. usage=" + usage.name
                    + " type=" + usage.codeType
                    + " wholePos=" + usage.wholePos.rowS + ":" + usage.wholePos.colS + "-"
                    + usage.wholePos.rowE + ":" + usage.wholePos.colE);
            return null;
        }
        return usage.definition;

    }

    private AInternalCodeNode findUsage(CommonFile file, int row, int col) {
        AInternalCodeNode u = findUsageAt(file, row, col);
        if (u != null) return u;

        if (col > 0) {
            u = findUsageAt(file, row, col - 1);
            if (u != null) return u;
        }

        u = findUsageAt(file, row + 1, col);
        if (u != null) return u;

        if (col > 0) {
            u = findUsageAt(file, row + 1, col - 1);
            if (u != null) return u;
        }

        return null;
    }

    private AInternalCodeNode findUsageAt(CommonFile file, int row, int col) {
        if (row < 0 || col < 0) return null;

        LinkTreePosition cursor = new LinkTreePosition();
        cursor.rowS = row; cursor.rowE = row;
        cursor.colS = col; cursor.colE = col;

        AInternalCodeNode usage = findNodeAt(file.codeNodes.values(), cursor, true);
        if (usage == null) usage = findNodeAt(file.codeNodes.values(), cursor, false);
        return usage;
    }


    private AInternalCodeNode findNodeAt(Collection<AInternalCodeNode> roots,
                                         LinkTreePosition cursor,
                                         boolean preferNamePosition) {
        AInternalCodeNode best = null;
        for (AInternalCodeNode r : roots) {
            AInternalCodeNode cand = findNodeAtRec(r, cursor, preferNamePosition);
            if (cand == null) continue;
            if (best == null || isNarrower(cand, best)) best = cand;
        }
        return best;
    }

    private AInternalCodeNode findNodeAtRec(AInternalCodeNode node,
                                            LinkTreePosition cursor,
                                            boolean preferNamePosition) {
        if (!contains(node.wholePos, cursor)) return null;

        AInternalCodeNode bestChild = null;
        for (AInternalCodeNode ch : node.childs.values()) {
            AInternalCodeNode cand = findNodeAtRec(ch, cursor, preferNamePosition);
            if (cand == null) continue;
            if (bestChild == null || isNarrower(cand, bestChild)) bestChild = cand;
        }
        if (bestChild != null) return bestChild;

        if (preferNamePosition) {
            return contains(node.namePosition, cursor) ? node : null;
        }
        return node;
    }

    private boolean contains(LinkTreePosition range, LinkTreePosition cursor) {
        int r = cursor.rowS;
        int c = cursor.colS;

        boolean afterStart = (r > range.rowS) || (r == range.rowS && c >= range.colS);
        boolean beforeEnd  = (r < range.rowE) || (r == range.rowE && c <= range.colE);

        return afterStart && beforeEnd;
    }

    private boolean isNarrower(AInternalCodeNode a, AInternalCodeNode b) {
        long aSize = span(a.wholePos);
        long bSize = span(b.wholePos);
        return aSize < bSize;
    }

    private long span(LinkTreePosition p) {
        long s = ((long) p.rowS) * 1_000_000L + p.colS;
        long e = ((long) p.rowE) * 1_000_000L + p.colE;
        return Math.max(0, e - s);
    }
}
