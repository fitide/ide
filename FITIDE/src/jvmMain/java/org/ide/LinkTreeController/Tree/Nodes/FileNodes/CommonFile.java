package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.IDgenerator;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;
import org.ide.LinkTreeController.Tree.Tools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

public class CommonFile extends FileNode {
    Map<String, AInternalCodeNode> codeNodes = new HashMap<>();

    public CommonFile(ReadWriteLock lock, String name) {
        super(lock, name);
    }


    @Override
    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            for (AInternalCodeNode node : codeNodes.values()) {
                if (Objects.equals(node.name, name) && !node.isDef) {
                    return Paths.get(node.pathToFile.toString(), node.id);
                }
            }
        }

        if (this.codeNodes.containsKey(Tools.getRootStr(path))) {
            return this.codeNodes.get(Tools.getRootStr(path)).searchForDeclaration(Tools.deleteRoot(path), name);
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
            if (Objects.equals(node.name, name) && !node.isDef) {
                return Paths.get(node.pathToFile.toString(), node.id);
            }
        }
        return null;
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException {
        if (!this.codeNodes.containsKey(Tools.getRootStr(path))) {
            throw new RuntimeException();
        }

        AInternalCodeNode node = codeNodes.get(Tools.getRootStr(path));

        if (path.getNameCount() == 1) {
            if (!node.isDef) {
                return codeNodes.get(Tools.getRootStr(path));
            }
        }
        else {
            return codeNodes.get(Tools.getRootStr(path)).getDeclaration(Tools.deleteRoot(path));

        }

        return null;
    }

    @Override
    public Path searchForDefinition(Path path, String name) {
        if (path.getNameCount() == 0) {
            for (AInternalCodeNode node : codeNodes.values()) {
                if (Objects.equals(node.name, name) && node.isDef) {
                    return Paths.get(node.pathToFile.toString(), node.id);
                }
            }
        }

        if (this.codeNodes.containsKey(Tools.getRootStr(path))) {
            return this.codeNodes.get(Tools.getRootStr(path)).searchForDefinition(Tools.deleteRoot(path), name);
        }

        return null;
    }

    @Override
    public void searchForDefinition(String name, ExecutorService service, Queue<Future<Path>> futures) {}

    @Override
    public Path searchForDefinitionInNode(String name) {
        for (AInternalCodeNode node : codeNodes.values()) {
            if (Objects.equals(node.name, name) && node.isDef) {
                return Paths.get(node.pathToFile.toString(), node.id);
            }
        }
        return null;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) throws NoDefinitionException {
        if (!this.codeNodes.containsKey(Tools.getRootStr(path))) {
            throw new RuntimeException();
        }

        AInternalCodeNode node = codeNodes.get(Tools.getRootStr(path));

        if (path.getNameCount() == 1) {
            if (node.isDef) {
                return codeNodes.get(Tools.getRootStr(path));
            }
        }
        else {
            return codeNodes.get(Tools.getRootStr(path)).getDefinition(Tools.deleteRoot(path));

        }

        return null;
    }

    @Override
    public void getHints(Path pathToModule, String prefix, List<String> listOfHints) {
        for (AInternalCodeNode node : this.codeNodes.values()) {
            if (node.name != null && node.name.startsWith(prefix)) {
                listOfHints.add(node.name);
                node.getHint(prefix, listOfHints);
            }
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
        List<AInternalCodeNode> list = TreeBuilder.build(plugin, tree);
        Map<String, AInternalCodeNode> newMap = new HashMap<>();
        for (AInternalCodeNode node : list) {
            boolean isFound = false;
            for (AInternalCodeNode inboundNode : this.codeNodes.values()) {
                if (Objects.equals(node.name, inboundNode.name)) {
                    inboundNode.updateCurNode(node);
                    isFound = true;
                    newMap.put(inboundNode.id, inboundNode);
                    break;
                }
            }

            if (!isFound) {
                node.setId(IDgenerator.genID());
                newMap.put(node.id, node);
            }
        }

        this.codeNodes = newMap;
    }
}
