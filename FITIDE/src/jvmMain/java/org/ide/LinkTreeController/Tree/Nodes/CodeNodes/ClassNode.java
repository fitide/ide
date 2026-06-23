package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ClassNode extends AInternalCodeNode {
    public ClassNode(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
    }

    public ClassNode(String name, List<String> keyWords) {
        super(name, keyWords);
    }

    @Override
    protected void setChilds(ParseTree curNode, Object state) {
        this.childs = TreeBuilder.getChilds(plugin, curNode, pathToFile, pathToModule, state);
    }

    @Override
    public void getCommonHints(String prefix, Set<HintNode> hints) {
        super.getCommonHints(prefix, hints);

        if (name != null && name.startsWith(prefix) && (codeType == CodeType.Declaration || codeType == CodeType.Definition)) hints.add(new HintNode(LinkTreeCodeTag.Class, name));
    }

    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        if (pathToModule.getNameCount() == 0) {
            return;
        }


        if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
            for (AInternalCodeNode node : childs.values()) {
                node.getCommonHints(prefix, hints);
            }

            childs.get(PathTools.getRootStr(pathToModule)).getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
        }
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour classColor = new CodeStrForColour();
        classColor.pos = this.namePosition;
        if (codeType != CodeType.Error) classColor.tag = LinkTreeCodeTag.Class;
        else classColor.tag = LinkTreeCodeTag.Error;


        list.add(classColor);
    }

    @Override
    public Path searchForDeclaration(Path pathToNode, String name) {
        if (pathToNode.getNameCount() == 0) {
            for (AInternalCodeNode node : childs.values()) {
                if (node.name != null && node.name.equals(name) && node.codeType == CodeType.Declaration) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }
        }
        else {
            if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
                return childs.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }
        }

        return null;
    }

    @Override
    public Path searchForDefinition(Path pathToNode, String name) {
        if (pathToNode.getNameCount() == 0) {
            for (AInternalCodeNode node : childs.values()) {
                if (node.name != null && node.name.equals(name) && node.codeType == CodeType.Definition) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }

        }
        else {
            if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
                return childs.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }

        }

        return null;
    }

    @Override
    protected void updateTree(ParseTree tree, Object state) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree, pathToFile, PathTools.deleteLast(pathToModule), state);
        this.updateCurNode(node);
    }

    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        if (namePosition.compareTo(position) == 0) return List.of();
        if (this.wholePos.compareTo(position) == 0) {
            List<Path> res = new ArrayList<>();

            for (AInternalCodeNode node : childs.values()) {
                if (node.wholePos.compareTo(position) == 0) {
                    res = node.getPathsToSearchDeclaration(position);
                    break;
                }
            }

            res.add(this.pathToModule);
            return res;
        }

        return null;
    }

    @Override
    public void addDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        switch (codeType) {
            case Definition -> {
                defs.put(this.name, this);
                decs.put(this.name, this);
            }
            case Declaration -> {
                decs.put(this.name, this);
            }
            default -> {}
        }


        for (var node : childs.values()) node.addDefinitionsAndDeclarations(defs, decs);
    }

    @Override
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        var ddefs = new HashMap<>(defs);
        var ddecs = new HashMap<>(decs);

        switch (codeType) {
            case Definition -> {
                for (var node : this.childs.values()) {
                    node.setDefinitionsAndDeclarations(defs, decs);
                }
            }
            case Declaration -> {
                this.definition = setDefDec(decs);
            }
            case Usage -> {
                this.definition = setDefDec(defs);
                this.declaration = setDefDec(decs);
                if (this.definition == null && this.declaration == null) {
                    codeType = CodeType.Error;
                }
            }
            default -> {}
        }

        defs = ddefs;
        decs = ddecs;
    }

    private AInternalCodeNode setDefDec(Map<String, AInternalCodeNode> map) {
        var node = map.getOrDefault(this.name, null);
        if (node != null && node instanceof ClassNode) {
            return node;
        }
        return null;
    }

    @Override
    public AInternalCodeNode findByPos(LinkTreePosition position) {
        if (contains(namePosition, position)) return this;

        for (var node : childs.values()) {
            if (contains(node.wholePos, position)) {
                return node.findByPos(position);
            }
        }

        return null;
    }

    @Override
    protected void setTypeDump(StringBuilder builder) {
        builder.append("class");
    }
}
