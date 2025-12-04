package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Var extends AInternalCodeNode {
    public String retType;
    public LinkTreePosition retPosition;

    public Var(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
        this.retPosition = new LinkTreePosition(plugin.getTypePositionOfModule(tree));
        this.retType = retType;
    }

    public Var(String name, List<String> keyWords, String retType) {
        super(name, keyWords);
        this.retType = retType;

    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = new HashMap<>();
    }

    @Override
    public void getHint(String prefix, Set<String> hints, Path pathToModule) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        code.tag = LinkTreeCodeTag.Var;
        list.add(code);

        CodeStrForColour typeC = new CodeStrForColour(LinkTreeCodeTag.Type);
        typeC.pos = this.retPosition.clone();
        list.add(typeC);
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree, this.pathToFile, this.pathToModule.subpath(0, pathToModule.getNameCount()));
        this.updateCurNode(node);
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        return List.of();
    }

    @Override
    public void setDefinitions(Map<String, AInternalCodeNode> defs) {
        if (this.codeType != CodeType.Definition) {
            this.definition = defs.getOrDefault(name, null);
        }
        else {
            this.definition = this;
            defs.put(name, this);
        }
    }
}
