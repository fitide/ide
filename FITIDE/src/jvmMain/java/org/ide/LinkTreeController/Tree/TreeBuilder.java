package org.ide.LinkTreeController.Tree;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Construction;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.ImportStatement;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Var;
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
        int it = 0;
        Path nullPath = Paths.get("");
        while (tree.getChild(it) != null) {
            AInternalCodeNode node = buildOneChild(plugin, tree.getChild(it++), pathToFIle, nullPath);
            resList.put(node.id, node);
        }
        return resList;
    }

    public static Map<String, AInternalCodeNode> getChilds(Plugin plugin, ParseTree parseTree, Path pathToFile, Path pathToNode) {
        Map<String, AInternalCodeNode> res = new HashMap<>();
        List<ParseTree> childs = plugin.getBodeOfModule(parseTree);
        for (ParseTree tree : childs) {
            AInternalCodeNode node = buildOneChild(plugin, tree, pathToFile, pathToNode);
            res.put(node.id, node);
        }
        return res;
    }

    public static AInternalCodeNode buildOneChild(Plugin plugin, ParseTree parseTree,  Path pathToFile, Path pathToParent) {
        Tag[] tags = plugin.getTagsOfNode(parseTree);
        AInternalCodeNode resNode;
        for (Tag tag : tags) {
            switch (tag) {
                case Var -> {
                    return new Var(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree));
                }
                case ImportStatement -> {
                    return new ImportStatement(plugin, pathToFile, pathToParent, parseTree, plugin.getNameOfNode(parseTree));
                }
                case Construction -> {
                    return new Construction(plugin, pathToFile, pathToParent, parseTree);
                }
            }
        }
    }
}
