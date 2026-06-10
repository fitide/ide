package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Constant extends AInternalCodeNode {
    public Constant(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        super(plugin, pathToFile, path, tree);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        return;
    }

    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        list.add(new CodeStrForColour(wholePos, LinkTreeCodeTag.Constant));
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
    public void addDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        return;
    }

    @Override
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        return;
    }

    @Override
    public AInternalCodeNode findByPos(LinkTreePosition position) {
        return null;
    }

    @Override
    protected void setTypeDump(StringBuilder builder) {
        builder.append("constant");
    }
}
