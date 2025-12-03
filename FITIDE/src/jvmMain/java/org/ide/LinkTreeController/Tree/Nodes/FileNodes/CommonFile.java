package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.*;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Func;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.ImportStatement;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Var;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

public class CommonFile extends FileNode {
    public Map<String, AInternalCodeNode> codeNodes = new HashMap<>();
    public Map<String, AInternalCodeNode> defInFile = new HashMap<>();
    public Map<String, AInternalCodeNode> decInFile = new HashMap<>();
    public boolean isInited = false;


    public CommonFile(ReadWriteLock lock, Path pathToFile, String name) {
        super(lock, pathToFile, name);
    }

    @Override
    public List<Path> getPathsToSearchDeclaration(Path pathToModule) {
        if (pathToModule.getNameCount() == 0) return null;

        if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
            return this.codeNodes.get(PathTools.getRootStr(pathToModule)).getPathsToSearchDeclaration(PathTools.deleteRoot(pathToModule));
        }

        return null;
    }

    @Override
    public List<Path> getPathsToSearchDeclaration(Path pathToFile, LinkTreePosition linkTreePosition) {
        for (AInternalCodeNode node : this.codeNodes.values()) {
            if (node.wholePos.compareTo(linkTreePosition) == 0) {
                node.getPathsToSearchDeclaration(linkTreePosition);
            }
        }

        return null;
    }

    @Override
    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            for (AInternalCodeNode node : codeNodes.values()) {
                if (Objects.equals(node.name, name) && node.codeType == CodeType.Declaration) {
                    return Paths.get(node.pathToModule.toString(), node.id);
                }
            }
        }

        if (this.codeNodes.containsKey(PathTools.getRootStr(path))) {
            return this.codeNodes.get(PathTools.getRootStr(path)).searchForDeclaration(PathTools.deleteRoot(path), name);
        }

        return null;
    }

    @Override
    public void searchForDeclaration(String name, ExecutorService service, Queue<Future<Path>> futures) {
        return;
    }

    @Override
    public Path searchForDeclarationInNode(String name) {
        for (AInternalCodeNode node : codeNodes.values()) {
            if (Objects.equals(node.name, name) && node.codeType == CodeType.Declaration) {
                return Paths.get(node.pathToModule.toString(), node.id);
            }
        }
        return null;
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException {
        if (!this.codeNodes.containsKey(PathTools.getRootStr(path))) {
            throw new RuntimeException();
        }

        AInternalCodeNode node = codeNodes.get(PathTools.getRootStr(path));

        if (path.getNameCount() == 1) {
            if (node.codeType == CodeType.Declaration) {
                return codeNodes.get(PathTools.getRootStr(path));
            }
        }
        else {
            return codeNodes.get(PathTools.getRootStr(path)).getDeclaration(PathTools.deleteRoot(path));

        }

        return null;
    }

    @Override
    public List<Path> getPathsToSearchDefinition(Path pathToModule) {
        if (pathToModule.getNameCount() == 0) return null;

        if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
            return this.codeNodes.get(PathTools.getRootStr(pathToModule)).getPathsToSearchDefinition(PathTools.deleteRoot(pathToModule));
        }

        return null;
    }

    @Override
    public List<Path> getPathsToSearchDefinition(Path pathToFile, LinkTreePosition linkTreePosition) {
        for (AInternalCodeNode node : this.codeNodes.values()) {
            if (node.wholePos.compareTo(linkTreePosition) == 0) {
                node.getPathsToSearchDefinition(linkTreePosition);
            }
        }

        return null;
    }

    @Override
    public Path searchForDefinition(Path path, String name) {
        if (path.getNameCount() == 0) {
            for (AInternalCodeNode node : codeNodes.values()) {
                if (Objects.equals(node.name, name) && node.codeType == CodeType.Definition) {
                    return Paths.get(node.pathToModule.toString(), node.id);
                }
            }
        }

        if (this.codeNodes.containsKey(PathTools.getRootStr(path))) {
            return this.codeNodes.get(PathTools.getRootStr(path)).searchForDefinition(PathTools.deleteRoot(path), name);
        }

        return null;
    }

    @Override
    public void searchForDefinition(String name, ExecutorService service, Queue<Future<Path>> futures) {}

    @Override
    public Path searchForDefinitionInNode(String name) {
        for (AInternalCodeNode node : codeNodes.values()) {
            if (Objects.equals(node.name, name) && node.codeType == CodeType.Definition) {
                return Paths.get(node.pathToModule.toString(), node.id);
            }
        }
        return null;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) throws NoDefinitionException {
        if (!this.codeNodes.containsKey(PathTools.getRootStr(path))) {
            throw new RuntimeException();
        }

        AInternalCodeNode node = codeNodes.get(PathTools.getRootStr(path));

        if (path.getNameCount() == 1) {
            if (node.codeType == CodeType.Definition) {
                return codeNodes.get(PathTools.getRootStr(path));
            }
        }
        else {
            return codeNodes.get(PathTools.getRootStr(path)).getDefinition(PathTools.deleteRoot(path));

        }

        return null;
    }

    @Override
    public void getHints(Path pathToModule, String prefix, Set<String> listOfHints) {
        for (AInternalCodeNode node : this.codeNodes.values()) {
            node.getCommonHints(prefix, listOfHints);
        }
        if (this.codeNodes.containsKey(PathTools.getRootStr(pathToModule))) {
            this.codeNodes.get(PathTools.getRootStr(pathToModule)).getHint(prefix, listOfHints, PathTools.deleteRoot(pathToModule));
        }
    }

    @Override
    public List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile) {
        List<CodeStrForColour> res = new ArrayList<>();
        for (AInternalCodeNode node : this.codeNodes.values()) {
            node.getHighlightning(res);
        }
        return res;
    }

    @Override
    public void addDir(Path pathToDir, String name) {
        throw new RuntimeException("Create dir in file");
    }

    @Override
    public void updateCode(Plugin plugin, Path pathToModule, ParseTree tree) {
        codeNodes = TreeBuilder.build(plugin, tree, pathToFile);
    }



    public void setPlugin(Plugin plugin, ParseTree tree) {
        codeNodes = TreeBuilder.build(plugin, tree, pathToFile);
        var decsDefs = getDecsAndDefs();
        for (AInternalCodeNode dec : decsDefs.a) {
            this.decInFile.put(dec.name, dec);
        }
        for (AInternalCodeNode def : decsDefs.b) {
            this.decInFile.put(def.name, def);
            this.defInFile.put(def.name, def);
        }
    }

    private Pair<List<AInternalCodeNode>, List<AInternalCodeNode>> getDecsAndDefs() {
        Pair<List<AInternalCodeNode>, List<AInternalCodeNode>> decsDefs = new Pair<>(new ArrayList<>(), new ArrayList<>());
        for (AInternalCodeNode codeNode : codeNodes.values()) {
            if (codeNode instanceof Func || codeNode instanceof Var) {
                switch(codeNode.codeType) {
                    case Declaration -> decsDefs.a.add(codeNode);
                    case Definition -> decsDefs.b.add(codeNode);
                }
            }
        }
        return decsDefs;
    }

    public void initCode(Plugin plugin, ParseTree tree, Path pathToFile) {
        codeNodes = TreeBuilder.build(plugin, tree, pathToFile);
        isInited = true;
    }

    public void setDefs(ARoot root) {
        List<CommonFile> files = new ArrayList<>();
        files.add(this);
        for (AInternalCodeNode node : codeNodes.values()) {
            if (node instanceof ImportStatement) {
                ImportStatement importStatement = (ImportStatement) node;
                var paths = importStatement.getPathsToSearchDefinition(this.pathToFile);
                for (var path : paths) {
                    CommonFileNode commonFileNode = root.getFileNode(path);
                    if (commonFileNode != null && commonFileNode instanceof CommonFile)
                        files.add((CommonFile) commonFileNode);
                }
            }
        }

        Map<String, AInternalCodeNode> defs = new HashMap();
        for (var file : files) {
            defs.putAll(file.defInFile);
        }

        for (var node : this.codeNodes.values()) {
            if (node.codeType == CodeType.Definition) {
                defs.put(node.name, node);
            }
        }

        for (var node : this.codeNodes.values()) {
            node.setDefinitions(defs);
        }
    }
}
