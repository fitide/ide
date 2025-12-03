package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;


import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyWord extends AInternalCodeNode {

    public KeyWord(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
        keyWords = List.of();
    }

    @Override
    public void setChilds(ParseTree curNode) {
        childs = new HashMap<>();
    }

    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        CodeStrForColour colour = new CodeStrForColour();
        colour.pos = this.wholePos.clone();
        colour.tag = LinkTreeCodeTag.KeyWord;
        list.add(colour);
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree, pathToFile, PathTools.deleteLast(pathToModule));
        this.updateCurNode(node);
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        return List.of();
    }

    @Override
    public void setDefinitions(Map<String, AInternalCodeNode> defs) {
        return;
    }
}
