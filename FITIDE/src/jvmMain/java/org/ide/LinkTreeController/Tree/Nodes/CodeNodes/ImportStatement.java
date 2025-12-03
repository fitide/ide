package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportStatement extends AInternalCodeNode {


    public ImportStatement(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = new HashMap<>();
    }

    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        code.tag = LinkTreeCodeTag.importStatement;
        list.add(code);
    }

    @Override
    public void updateTree(ParseTree parseTree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, parseTree, pathToFile, PathTools.deleteLast(pathToModule));
        this.updateCurNode(node);
    }



    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        List<Path> paths = new ArrayList<>();
        paths.add(Paths.get(pathToFile.toString(), this.name));
        paths.add(Paths.get(this.name));
        return paths;
    }

    @Override
    public void setDefinitions(Map<String, AInternalCodeNode> defs) {
        return;
    }

}
