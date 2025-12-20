package org.ide.LinkTreeController.Tree.ToolClasses;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.jetbrains.annotations.NotNull;

public class HintNode implements Comparable<HintNode> {
    public LinkTreeCodeTag type;
    public String name;

    public HintNode(LinkTreeCodeTag type, String name) {
        this.type = type;
        this.name = name;
    }


    @Override
    public int compareTo(@NotNull HintNode o) {
        return name.compareTo(o.name);
    }
}
