package org.ide.LinkTreeController.Tree.ToolClasses;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;

import java.util.Objects;

public class HintNode  {
    public LinkTreeCodeTag type;
    public String name;

    public HintNode(LinkTreeCodeTag type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HintNode)) return false;
        return this.name.equals(((HintNode) obj).name);
    }
}
