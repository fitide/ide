package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Var extends AInternalCodeNode {

    public Var(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name){
        super(plugin, pathToFile, path, tree, name);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = new HashMap<>();
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        code.tag = LinkTreeCodeTag.Var;
        list.add(code);
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree);
        this.updateCurNode(node);
    }

    @Override
    protected List<Path> getPaths(Position position) {
        return List.of();
    }

}
