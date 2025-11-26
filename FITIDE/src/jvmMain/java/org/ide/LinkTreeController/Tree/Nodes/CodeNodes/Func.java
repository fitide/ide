package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.Tools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Objects;

public class Func extends AInternalCodeNode {
    public Map<String, AInternalCodeNode> args;
    public AInternalCodeNode retType;
    public LinkTreePosition bodyPosition;
    public LinkTreePosition argsPosition;

    public Func(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
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
    }


    @Override
    public void getHint(String prefix, List<String> hints, Path pathToModule) {
        if (pathToModule.getNameCount() == 0) {
            return;
        }

        if (Objects.equals(retType.id, Tools.getRootStr(pathToModule))) {
            retType.getCommonHints(prefix, hints);
            retType.getHint(prefix, hints, Tools.deleteRoot(pathToModule));
        }

        if (args.containsKey(Tools.getRootStr(pathToModule))) {
            var node = args.get(Tools.getRootStr(pathToModule));
            node.getCommonHints(prefix, hints);
            node.getHint(prefix, hints, Tools.deleteRoot(pathToModule));
        }

        for (AInternalCodeNode node : args.values()) {
            node.getCommonHints(prefix, hints);
        }

        for (AInternalCodeNode node : childs.values()) {
            node.getCommonHints(prefix, hints);
        }

        if (childs.containsKey(Tools.getRootStr(pathToModule))) {
            childs.get(Tools.getRootStr(pathToModule)).getHint(prefix, hints, Tools.deleteRoot(pathToModule));
        }
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);
        CodeStrForColour funcColour = new CodeStrForColour();
        funcColour.pos = this.namePosition;
        for (AInternalCodeNode arg : args.values()) {
            arg.getHighlightning(list);
        }
        retType.getHighlightning(list);
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
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree);
        this.updateCurNode(node);
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
    public void updateCurNode(AInternalCodeNode node) {
        super.updateCurNode(node);
        if (!(node instanceof Func)) throw new RuntimeException("copy CodeNode for different types");

        this.args = ((Func) node).args;
        this.retType = ((Func) node).retType;
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
