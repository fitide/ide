package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.List;

public class ErrorNode extends AInternalCodeNode {
    public ErrorNode(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
    }

    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
    }

    @Override
    protected void updateTree(ParseTree tree) {
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        return List.of();
    }
}
