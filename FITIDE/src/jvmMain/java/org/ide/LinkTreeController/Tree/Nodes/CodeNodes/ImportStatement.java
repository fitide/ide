package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.List;

public class ImportStatement extends AInternalCodeNode {


    public ImportStatement(Plugin plugin, Path path, ParseTree tree, String name) {
        super(plugin, path, tree, name);
    }

    @Override
    public void getHint(String prefix, List<String> hints) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        for (KeyWord keyWord : this.keyWords) {
            LinkTreePosition pos = new LinkTreePosition();

            list.add()
        }
    }

    @Override
    public Path searchForDeclaration(Path pathToNode, String name) {
        return null;
    }

    @Override
    public Path searchForDefinition(Path pathToNode, String name) {
        return null;
    }

    @Override
    public void updateTree(Path pathToModule, ParseTree parseTree) {

    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) {
        return null;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) {
        return null;
    }

    @Override
    public void updateCurNode(AInternalCodeNode node) {

    }

    @Override
    public Path[] getPathsToSearchDeclaration(Path pathToModule) {
        return new Path[0];
    }

    @Override
    public Path[] getPathsToSearchDefinition(Path pathToModule) {
        return new Path[0];
    }

    @Override
    public Path[] getPathsToSearchDeclaration(Position position) {
        return new Path[0];
    }

    @Override
    public Path[] getPathsToSearchDefinition(Position position) {
        return new Path[0];
    }
}
