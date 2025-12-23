package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeType;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.LinkTreeController.Tree.TreeBuilder;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Construction extends AInternalCodeNode {
    public Map<String, AInternalCodeNode> args = new HashMap<>();
    public LinkTreePosition bodyPosition;
    public LinkTreePosition argsPosition;
    public List<Pair<Map<String, AInternalCodeNode>, LinkTreePosition>> bodies;

    public Construction(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        //TODO: override
        super(plugin, pathToFile, path, tree);
        if (codeType == CodeType.Error) return;

        List<ParseTree> argsInTree = plugin.getConstructionArgs(tree);
        for (ParseTree parseTree : argsInTree) {
            AInternalCodeNode arg = (TreeBuilder.buildOneChild(plugin, parseTree, pathToFile, this.pathToModule));
            args.put(arg.id, arg);
        }

        this.bodyPosition = new LinkTreePosition(plugin.getPositionOfModuleBody(tree));
        this.argsPosition = new LinkTreePosition(plugin.getPositionOfArgsOfFunc(tree));

    }

    public Construction(String name, List<String> keyWords) {
        super(name, keyWords);
    }

    @Override
    protected void setChilds(ParseTree curNode) {
        //TODO: override

        this.childs = TreeBuilder.getChilds(plugin, curNode, pathToFile, pathToModule);
        List<ParseTree> argsTrees = plugin.getArgsOfFunc(curNode);
        for (ParseTree tree : argsTrees) {
            AInternalCodeNode arg = TreeBuilder.buildOneChild(plugin, tree, pathToFile, pathToModule);
            args.put(arg.id, arg);
        }

        bodies = List.of(new Pair<>(childs, bodyPosition));


    }


    @Override
    public void getHint(String prefix, Set<HintNode> hints, Path pathToModule) {
        if (pathToModule.getNameCount() == 0) {
            return;
        }

        if (this.childs.containsKey(PathTools.getRootStr(pathToModule))) {
            this.childs.get(PathTools.getRootStr(pathToModule)).getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
            return;
        }

        if (this.args.containsKey(PathTools.getRootStr(pathToModule))) {
            this.args.get(PathTools.getRootStr(pathToModule)).getHint(prefix, hints, PathTools.deleteRoot(pathToModule));
            return;
        }

    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        for (AInternalCodeNode arg : args.values()) {
            arg.getHighlightning(list);
        }

        for (var body : bodies) {
            for (var state : body.a.values()) {
                state.getHighlightning(list);
            }
        }

        for (var keyword : this.keyWords) {
            keyword.getHighlightning(list);
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
            for (var body : bodies) {
                for (var  node : body.a.values()) {
                    if (node.name != null && node.name.equals(name) &&
                            (node.codeType == CodeType.Declaration || node.codeType == CodeType.Definition)) {
                        return Paths.get(pathToNode.toString(), node.id);
                    }
                }
            }
        }
        else {
            if (args.containsKey(PathTools.getRootStr(pathToModule))) {
                return args.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
            }

            for (var body : bodies) {
                if (body.a.containsKey(PathTools.getRootStr(pathToModule))) {
                    return childs.get(PathTools.getRootStr(pathToModule)).searchForDeclaration(PathTools.deleteRoot(pathToModule), name);
                }
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

            for (var body : bodies) {
                for (var  node : body.a.values()) {
                    if (node.name != null && node.name.equals(name) && node.codeType == CodeType.Definition) {
                        return Paths.get(pathToNode.toString(), node.id);
                    }
                }
            }
        }
        else {
            if (args.containsKey(PathTools.getRootStr(pathToModule))) {
                return args.get(PathTools.getRootStr(pathToModule)).searchForDefinition(PathTools.deleteRoot(pathToModule), name);
            }

            for (var body : bodies) {
                if (body.a.containsKey(PathTools.getRootStr(pathToModule))) {
                    return childs.get(PathTools.getRootStr(pathToModule)).searchForDefinition(PathTools.deleteRoot(pathToModule), name);
                }
            }
        }

        return null;
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
    protected void updateTree(ParseTree tree) {
        //TODO: override

        AInternalCodeNode node = TreeBuilder.buildOneChild(plugin, tree, pathToFile, PathTools.deleteLast(pathToModule));
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
        for (var body : bodies) {
            if (body.b.compareTo(position) == 0) {
                List<Path> res = new ArrayList<>();

                for (AInternalCodeNode node : body.a.values()) {
                    if (node.wholePos.compareTo(position) == 0) res = node.getPathsToSearchDeclaration(position);
                }


                res.add(this.pathToModule);
                return res;
            }
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
    public void addDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        return;
    }

    @Override
    public void setDefinitionsAndDeclarations(Map<String, AInternalCodeNode> defs, Map<String, AInternalCodeNode> decs) {
        for (var arg : this.args.values()) {
            arg.addDefinitionsAndDeclarations(defs, decs);
        }

        for (var arg : args.values()) {
            arg.setDefinitionsAndDeclarations(defs, decs);
        }

        for (var body : bodies) {
            Map<String, AInternalCodeNode> tempdefs = new HashMap<>(defs);
            var tempDecs = new HashMap<>(decs);

            for (var node : body.a.values()) {
                node.setDefinitionsAndDeclarations(defs, decs);
            }

            for (var node : body.a.values()) {
                node.setDefinitionsAndDeclarations(defs, decs);
            }

            defs = tempdefs;
            decs = tempDecs;
        }
    }

    @Override
    public void setTypes(Set<String> types) {
        for (var arg : args.values()) {
            arg.setTypes(types);
        }
        for (var body : bodies) {
            for (var node : body.a.values()) {
                node.setTypes(types);
            }
        }
    }
}
