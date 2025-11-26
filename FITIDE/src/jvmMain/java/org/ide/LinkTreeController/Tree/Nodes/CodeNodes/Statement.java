package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.List;

public class Statement extends AInternalCodeNode {

    public Statement(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        super(plugin, pathToFile, path, tree);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = TreeBuilder.getChilds(plugin, curNode);
    }

    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
        return;
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree);
        if (node instanceof Statement) {
            this.updateCurNode(node);
        }
        else {
            throw new RuntimeException("wrong update");
        }
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        return List.of();
    }
}
