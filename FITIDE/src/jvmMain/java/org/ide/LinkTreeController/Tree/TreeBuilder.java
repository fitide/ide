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

        Path nullPath = Paths.get("");
        for (int it = 0; it < tree.getChildCount(); it++) {
            AInternalCodeNode node = buildOneChild(plugin, tree.getChild(it), pathToFIle, nullPath);
            if (node != null) resList.put(node.id, node);
        }
        return resList;
    }

    public static Map<String, AInternalCodeNode> getChilds(Plugin plugin, ParseTree parseTree, Path pathToFile, Path pathToNode) {
        Map<String, AInternalCodeNode> res = new HashMap<>();
        List<ParseTree> childs = plugin.getBodeOfModule(parseTree);
        for (ParseTree tree : childs) {
            AInternalCodeNode node = buildOneChild(plugin, tree, pathToFile, pathToNode);
            if (node != null) res.put(node.id, node);
        }
        return res;
    }

    public static AInternalCodeNode buildOneChild(Plugin plugin, ParseTree parseTree,  Path pathToFile, Path pathToParent) {
        Tag[] tags = plugin.getTagsOfNode(parseTree);
        AInternalCodeNode resNode;
        for (Tag tag : tags) {
            switch (tag) {
                case Var -> {
                    return new Var(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree), plugin.getType(parseTree));
                }
                case ImportStatement -> {
                    return new ImportStatement(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree));
                }
                case Construction -> {
                    return new Construction(plugin, pathToFile, pathToParent, parseTree);
                }
                case KeyWord -> {
                    return new KeyWord(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree));
                }
                case Func -> {
                    return new Func(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree));
                }
                case ErrorNode -> {
                    String name;
                    if ((name = plugin.getNameOfNode(parseTree)) != null){
                        return new ErrorNode(plugin, pathToFile, pathToParent, parseTree, name);
                    }
                    else {
                        return new ErrorNode(plugin, pathToFile, pathToParent, parseTree);
                    }
                }
            }
        }

        return null;
    }
}
