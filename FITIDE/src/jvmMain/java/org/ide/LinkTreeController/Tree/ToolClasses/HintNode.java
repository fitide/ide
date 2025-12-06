package org.ide.LinkTreeController.Tree.ToolClasses;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;

public class HintNode {
    public LinkTreeCodeTag type;
    public String name;

    public HintNode(LinkTreeCodeTag type, String name) {
        this.type = type;
        this.name = name;
    }
}
