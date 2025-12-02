package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;
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
    public Map<String, AInternalCodeNode> defInFIle = new HashMap<>();

    public CommonFile(ReadWriteLock lock, String name) {
        super(lock, name);
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
    public void getHints(Path pathToModule, String prefix, List<String> listOfHints) {
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
        codeNodes = TreeBuilder.build(plugin, tree);
    }
}
