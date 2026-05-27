package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.*;

public class Expression extends AInternalCodeNode {


    public Expression(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        super(plugin, pathToFile, path, tree);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = TreeBuilder.getChilds(plugin, curNode, pathToFile, pathToModule);
    }

    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
            for (var child : childs.values()) {
                child.getCommonHints(prefix, hints);
            }

            childs.get(PathTools.getRootStr(pathToModule)).getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
        }
    }

    @Override
    protected void updateTree(ParseTree tree) {
        var newNode = TreeBuilder.buildOneChild(plugin, tree, pathToFile, PathTools.deleteLast(pathToModule));
        if (newNode instanceof Expression) {
            this.updateCurNode(newNode);
        }
        else {
            throw new RuntimeException("Wrong updating on expression node");
        }
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        if (namePosition.compareTo(position) == 0) return List.of();
        if (wholePos.compareTo(position) == 0) {
            List<Path> res = new ArrayList<>();

            for (AInternalCodeNode node : childs.values()) {
                if (node.wholePos.compareTo(position) == 0) res = node.getPathsToSearchDeclaration(position);
            }

            res.add(this.pathToModule);
            return res;
        }

        return List.of();
    }

    @Override
    public void addDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        for (var child : childs.values()) {
            child.addDefinitionsAndDeclarations(defs, decs);
        }
    }

    @Override
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        for (var child : childs.values()) {
            child.setDefinitionsAndDeclarations(defs, decs);
        }
    }

    @Override
    public AInternalCodeNode findByPos(LinkTreePosition position) {
        for (var node : childs.values()) {
            if (contains(node.wholePos, position)) {
                return node.findByPos(position);
            }
        }

        return null;
    }
}
