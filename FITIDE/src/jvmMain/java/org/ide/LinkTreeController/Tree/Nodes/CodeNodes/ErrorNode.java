package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;

import java.nio.file.Path;
import java.util.List;

public class ErrorNode extends AInternalCodeNode {
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
