package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Var extends AInternalCodeNode {
    public String retType;
    public LinkTreePosition retPosition;
    private boolean isTypeDef = false;

    public Var(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name, String retType) {
        super(plugin, pathToFile, path, tree, name);
        var pos = plugin.getTypePositionOfModule(tree);
        if (pos != null) {
            this.retPosition = new LinkTreePosition(pos);
            this.retType = retType;
        }
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
    public void getCommonHints(String prefix, Set<HintNode> hints) {
        super.getCommonHints(prefix, hints);

        if (name != null && name.startsWith(prefix)) hints.add(new HintNode(LinkTreeCodeTag.Var, name));
    }

    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        if (name != null && name.startsWith(prefix)) hints.add(new HintNode(LinkTreeCodeTag.Var, name));
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour code = new CodeStrForColour();
        code.pos = this.namePosition.clone();
        if (codeType != CodeType.Error) code.tag = LinkTreeCodeTag.Var;
        else code.tag = LinkTreeCodeTag.Error;
        list.add(code);


        if (retType != null) {
            CodeStrForColour typeColour = new CodeStrForColour();
            typeColour.pos = this.retPosition;
            if (isTypeDef) typeColour.tag = LinkTreeCodeTag.Type;
            else typeColour.tag = LinkTreeCodeTag.Error;
            list.add(typeColour);
        }
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
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        switch (codeType) {
            case Definition -> {
                defs.put(this.name, this);
                decs.put(this.name, this);
            }
            case Declaration -> {
                defs.put(this.name, this);
                decs.put(this.name, this);
            }
            case Usage -> {
                this.definition = validatePointer(defs.getOrDefault(this.name, null));
                this.declaration = validatePointer(decs.getOrDefault(this.name, null));
                if (definition == null && declaration == null) {
                    codeType = CodeType.Error;
                }
            }
        }
    }

    private AInternalCodeNode validatePointer(AInternalCodeNode node) {
        if (node instanceof Var) return node;
        return null;
    }

    @Override
    public void setTypes(Set<String> types) {
        if (types.contains(retType)) isTypeDef = true;
    }
}
