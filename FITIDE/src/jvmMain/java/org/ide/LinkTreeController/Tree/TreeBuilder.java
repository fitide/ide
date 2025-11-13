package org.ide.LinkTreeController.Tree;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Tag;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder {

    public static List<AInternalCodeNode> build(Plugin plugin, ParseTree tree) {
        List<AInternalCodeNode> resList = new ArrayList<>();
        int it = 0;
        while (tree.getChild(it) != null) {
            resList.add(buildOneChild(plugin, tree.getChild(it++)));
        }
        return resList;
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
