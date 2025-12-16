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
import org.ide.PluginController.PluginInterface.ExternalType;
import org.ide.PluginController.PluginInterface.ExternalVar;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Func extends AInternalCodeNode {
    public Map<String, AInternalCodeNode> args = new HashMap<>();
    public String retType = null;
    public boolean isTypeDef = false;
    public LinkTreePosition retTypePosition;
    public LinkTreePosition bodyPosition;
    public LinkTreePosition argsPosition;

    public Func(Plugin plugin, Path pathToFile, Path path, ParseTree tree, String name) {
        super(plugin, pathToFile, path, tree, name);
        if (codeType == CodeType.Error) return;

        List<ParseTree> argsInTree = plugin.getArgsOfFunc(tree);
        for (ParseTree parseTree : argsInTree) {
            AInternalCodeNode arg = (TreeBuilder.buildOneChild(plugin, parseTree, pathToFile, pathToModule));
            args.put(arg.id, arg);
        }
        Position pos;
        if ((pos = plugin.getPositionOfModuleBody(tree)) != null) this.bodyPosition = new LinkTreePosition(pos);
        if ((pos = plugin.getPositionOfArgsOfFunc(tree)) != null) this.argsPosition = new LinkTreePosition(pos);
        if ((pos = plugin.getTypePositionOfModule(tree)) != null) this.retTypePosition = new LinkTreePosition(pos);
    }

    public Func(String name, List<String> keyWords, List<ExternalVar> externalArgs, ExternalType externalType) {
        super(name, keyWords);
        for (var arg : externalArgs) {
            this.args.put(arg.Name, new Var(arg.Name, List.of(), arg.Type.name));
        }
        this.retType = externalType.name;
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        this.childs = TreeBuilder.getChilds(plugin, curNode, pathToFile, pathToModule);
    }

    @Override
    public void getCommonHints(String prefix, Set<HintNode> hints) {
        super.getCommonHints(prefix, hints);

        if (name != null && name.startsWith(prefix)) hints.add(new HintNode(LinkTreeCodeTag.Func, name));
    }


    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        if (pathToModule.getNameCount() == 0) {
            return;
        }


        if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
            if (args.containsKey(PathTools.getRootStr(pathToModule))) {
                var node = args.get(PathTools.getRootStr(pathToModule));
                node.getCommonHints(prefix, hints);
                node.getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
            }

            for (AInternalCodeNode node : args.values()) {
                node.getCommonHints(prefix, hints);
            }

            for (AInternalCodeNode node : childs.values()) {
                node.getCommonHints(prefix, hints);
            }

            childs.get(PathTools.getRootStr(pathToModule)).getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
        }
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        super.getHighlightning(list);

        CodeStrForColour funcColour = new CodeStrForColour();
        funcColour.pos = this.namePosition;
        if (codeType != CodeType.Error) funcColour.tag = LinkTreeCodeTag.Func;
        else funcColour.tag = LinkTreeCodeTag.Error;


        if (retType != null) {
            CodeStrForColour typeColour = new CodeStrForColour();
            typeColour.pos = this.retTypePosition;
            if (isTypeDef) typeColour.tag = LinkTreeCodeTag.Type;
            else typeColour.tag = LinkTreeCodeTag.Error;
            list.add(typeColour);
        }

        list.add(funcColour);
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
            if (args.containsKey(PathTools.getRootStr(pathToModule))) {
                return args.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }

            if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
                return childs.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
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
            if (args.containsKey(PathTools.getRootStr(pathToModule))) {
                return args.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }

            if (childs.containsKey(PathTools.getRootStr(pathToModule))) {
                return childs.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }
        }

        return null;
    }

    @Override
    protected void updateTree(ParseTree tree) {
        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree, pathToFile, PathTools.deleteLast(pathToModule));
        this.updateCurNode(node);
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) {
        AInternalCodeNode node = super.getDeclaration(path);
        if (node == null && args.containsKey(PathTools.getRootStr(path))) {
            return args.get(PathTools.getRootStr(path)).getDeclaration(PathTools.deleteRoot(path));
        }

        return node;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) {
        AInternalCodeNode node = super.getDeclaration(path);
        if (node == null && args.containsKey(PathTools.getRootStr(path))) {
            return args.get(PathTools.getRootStr(path)).getDeclaration(PathTools.deleteRoot(path));
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

    @Override
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        switch (codeType) {
            case Definition -> {
                defs.put(this.name, this);
                decs.put(this.name, this);
                for (var arg : args.values()) {
                    decs.put(arg.name, arg);
                }
                for (var node : this.childs.values()) {
                    node.setDefinitionsAndDeclarations(defs, decs);
                }
            }
            case Declaration -> {
                this.definition = setDefDec(decs);
                decs.put(this.name, this);
            }
            case Usage -> {
                this.definition = setDefDec(defs);
                this.declaration = setDefDec(decs);
                if (this.definition == null && this.declaration == null) {
                    codeType = CodeType.Error;
                }
            }
        }
    }

    private AInternalCodeNode setDefDec(Map<String, AInternalCodeNode> map) {
        var node = map.getOrDefault(this.name, null);
        if (node != null && node instanceof Func && checkArgs(node)) {
            return node;
        }
        return null;
    }

    //TODO: implement
    private boolean checkArgs(AInternalCodeNode de) { return true;}

    @Override
    public void setTypes(Set<String> types) {
        if (types.contains(retType)) isTypeDef = true;

        super.setTypes(types);

        for (var arg : args.values()) {
            arg.setTypes(types);
        }
    }

}
