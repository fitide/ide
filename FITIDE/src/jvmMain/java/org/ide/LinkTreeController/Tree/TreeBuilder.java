package org.ide.LinkTreeController.Tree;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.*;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Tag;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {

    public static Map<String, AInternalCodeNode> build(Plugin plugin, ParseTree tree, Path pathToFIle) {
        Map<String, AInternalCodeNode> resList = new HashMap<>();
        var state = plugin.newStateObject();

        Path nullPath = Paths.get("");
        for (var child : plugin.getChildsOfNode(tree, state)) {
            AInternalCodeNode node = buildOneChild(plugin, child, pathToFIle, nullPath, state);
            if (node != null) resList.put(node.id, node);
        }
        return resList;
    }

    public static Map<String, AInternalCodeNode> getChilds(Plugin plugin, ParseTree parseTree, Path pathToFile, Path pathToNode, Object state) {
        Map<String, AInternalCodeNode> res = new HashMap<>();
        List<ParseTree> childs = plugin.getChildsOfNode(parseTree, state);
        for (ParseTree tree : childs) {
            AInternalCodeNode node = buildOneChild(plugin, tree, pathToFile, pathToNode, state);
            if (node != null) res.put(node.id, node);
        }
        return res;
    }

    public static AInternalCodeNode buildOneChild(Plugin plugin, ParseTree parseTree,  Path pathToFile, Path pathToParent, Object state) {
        Tag[] tags = plugin.getTagsOfNode(parseTree);
        AInternalCodeNode resNode;
        for (Tag tag : tags) {
            switch (tag) {
                case Var -> {
                    return new Var(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree, state), plugin.getType(parseTree));
                }
                case ImportStatement -> {
                    return new ImportStatement(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree, state));
                }
                case Construction -> {
                    return new Construction(plugin, pathToFile, pathToParent, parseTree, state);
                }
                case KeyWord -> {
                    return new KeyWord(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree, state));
                }
                case Func -> {
                    return new Func(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree, state), state);
                }
                case Class -> {
                    return new ClassNode(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree, state));
                }
                case ErrorNode -> {
                    String name;
                    if ((name = plugin.getNameOfNode(parseTree, state)) != null){
                        return new ErrorNode(plugin, pathToFile, pathToParent, parseTree, name);
                    }
                    else {
                        return new ErrorNode(plugin, pathToFile, pathToParent, parseTree, state);
                    }
                }
                case Expression -> {
                    return new Expression(plugin, pathToFile, pathToParent, parseTree, state);
                }
                case Constant -> {
                    return new Constant(plugin, pathToFile, pathToParent, parseTree, state);
                }
            }
        }

        return null;
    }
}
