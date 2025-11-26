package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.Tools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Construction extends AInternalCodeNode {
    public Map<String, AInternalCodeNode> args = new HashMap<>();
    public LinkTreePosition bodyPosition;
    public LinkTreePosition argsPosition;

    public Construction(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        super(plugin, pathToFile, path, tree);
        List<ParseTree> argsInTree = plugin.getArgsOfFunc(tree);
        for (ParseTree parseTree : argsInTree) {
            AInternalCodeNode arg = (TreeBuilder.buildOneChild(plugin, parseTree));
            args.put(arg.id, arg);
        }
        this.bodyPosition = new LinkTreePosition(plugin.getPositionOfModuleBody(tree));
        this.argsPosition = new LinkTreePosition(plugin.getPositionOfArgsOfFunc(tree));
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = TreeBuilder.getChilds(plugin, curNode);
        List<ParseTree> argsTrees = plugin.getArgsOfFunc(curNode);
        for (ParseTree tree : argsTrees) {
            AInternalCodeNode arg = TreeBuilder.buildOneChild(plugin, tree);
            args.put(arg.id, arg);
        }
    }


    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
        if (pathToModule.getNameCount() == 0) {
            return;
        }

        if (this.childs.containsKey(Tools.getRootStr(pathToModule))) {
            this.childs.get(Tools.getRootStr(pathToModule)).getHint(prefix, hints, Tools.deleteRoot(pathToModule));
            return;
        }

        if (this.args.containsKey(Tools.getRootStr(pathToModule))) {
            this.args.get(Tools.getRootStr(pathToModule)).getHint(prefix, hints, Tools.deleteRoot(pathToModule));
            return;
        }

    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);
        for (AInternalCodeNode arg : args.values()) {
            arg.getHighlightning(list);
        }
    }

    @Override
    public Path searchForDeclaration(Path pathToNode, String name) {
        if (pathToNode.getNameCount() == 0) {
            for (AInternalCodeNode node : args.values()) {
                if (node.name != null && node.name.equals(name) &&
                        (node.codeType == CodeType.Declaration || node.codeType == CodeType.Definition)) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }

            for (AInternalCodeNode node : childs.values()) {
                if (node.name != null && node.name.equals(name) &&
                        (node.codeType == CodeType.Declaration || node.codeType == CodeType.Definition)) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }
        }
        else {
            if (args.containsKey(Tools.getRootStr(pathToModule))) {
                return args.get(Tools.getRootStr(pathToModule)).searchForDeclaration(Tools.deleteRoot(pathToModule), name);
            }

            if (childs.containsKey(Tools.getRootStr(pathToModule))) {
                return childs.get(Tools.getRootStr(pathToModule)).searchForDeclaration(Tools.deleteRoot(pathToModule), name);
            }
        }

        return null;
    }

    @Override
    public Path searchForDefinition(Path pathToNode, String name) {
        if (pathToNode.getNameCount() == 0) {
            for (AInternalCodeNode node : args.values()) {
                if (node.name != null && node.name.equals(name) && node.codeType == CodeType.Declaration) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }

            for (AInternalCodeNode node : childs.values()) {
                if (node.name != null && node.name.equals(name) && node.codeType == CodeType.Definition) {
                    return Paths.get(pathToNode.toString(), node.id);
                }
            }
        }
        else {
            if (args.containsKey(Tools.getRootStr(pathToModule))) {
                return args.get(Tools.getRootStr(pathToModule)).searchForDeclaration(Tools.deleteRoot(pathToModule), name);
            }

            if (childs.containsKey(Tools.getRootStr(pathToModule))) {
                return childs.get(Tools.getRootStr(pathToModule)).searchForDeclaration(Tools.deleteRoot(pathToModule), name);
            }
        }

        return null;
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) {
        AInternalCodeNode node = super.getDeclaration(path);
        if (node == null && args.containsKey(Tools.getRootStr(path))) {
            return args.get(Tools.getRootStr(path)).getDeclaration(Tools.deleteRoot(path));
        }

        return node;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) {
        AInternalCodeNode node = super.getDeclaration(path);
        if (node == null && args.containsKey(Tools.getRootStr(path))) {
            return args.get(Tools.getRootStr(path)).getDeclaration(Tools.deleteRoot(path));
        }

        return node;
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree);
        if (node instanceof Construction) {
            this.updateCurNode(node);
            this.args = ((Construction) node).args;
        }
        else {
            throw new RuntimeException("wrong update");
        }
    }


    @Override
    protected List<Path> getPaths(LinkTreePosition position) {
        if (namePosition.compareTo(position) == 0) return List.of();
        if (bodyPosition.compareTo(position) == 0) {
            List<Path> res = new ArrayList<>();

            for (AInternalCodeNode node : childs.values()) {
                if (node.wholePos.compareTo(position) == 0) res = node.getPathsToSearchDeclaration(position);
            }

            res.add(this.pathToModule);
            return res;
        }

        if (argsPosition.compareTo(position) == 0) {
            List<Path> res = new ArrayList<>();

            for (AInternalCodeNode node : args.values()) {
                if (node.wholePos.compareTo(position) == 0) res = node.getPathsToSearchDeclaration(position);
            }

            res.add(this.pathToModule);
            return res;
        }

        return null;
    }
}
