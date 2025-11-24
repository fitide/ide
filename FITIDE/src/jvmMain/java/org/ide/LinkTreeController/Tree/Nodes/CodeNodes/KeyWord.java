package org.ide.LinkTreeController.Tree.Nodes.CodeNodes;


import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.PluginController.PluginInterface.Plugin;
import org.ide.PluginController.PluginInterface.Position;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class KeyWord extends AInternalCodeNode {

    public KeyWord(Plugin plugin, Path pathToFile, Path path, ParseTree tree) {
        super(plugin, pathToFile, path, tree);
    }

    @Override
    public void setChilds() {
        childs = new HashMap<>();
    }

    @Override
    public void getHint(String prefix, List<String> hints) {
        return;
    }

    @Override
    public void getHighlightning(List<CodeStrForColour> list) {
        CodeStrForColour colour = new CodeStrForColour();
        colour.pos = this.wholePos.clone();
        colour.tag = LinkTreeCodeTag.KeyWord;
        list.add(colour);
    }
}
