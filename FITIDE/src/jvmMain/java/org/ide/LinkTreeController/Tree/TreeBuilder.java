package org.ide.LinkTreeController.Tree;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeBuilder {

    public static Map<String, AInternalCodeNode> build(Plugin plugin, ParseTree tree) {
        Map<String, AInternalCodeNode> resList = new HashMap<>();
        int it = 0;
        while (tree.getChild(it) != null) {
            AInternalCodeNode node = buildOneChild(plugin, tree.getChild(it++));
            resList.put(node.id, node);
        }
        return resList;
    }

    public static Map<String, AInternalCodeNode> getChilds(Plugin plugin, ParseTree parseTree) {
        Map<String, AInternalCodeNode> res = new HashMap<>();
        List<ParseTree> childs = plugin.getBodeOfModule(parseTree);
        for (ParseTree tree : childs) {
            AInternalCodeNode node = buildOneChild(plugin, tree);
            res.put(node.id, node);
        }
        return res;
    }

    public static AInternalCodeNode buildOneChild(Plugin plugin, ParseTree parseTree) {
        Tag[] tags = plugin.getTagsOfNode(parseTree);
        AInternalCodeNode resNode;
        for (Tag tag : tags) {
            switch (tag) {
                case Var -> {
                }
            }
        }
    }
}
