package org.ide.LinkTreeController.Tree.ToolClasses;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;

public class CodeStrForColour {
    public LinkTreePosition pos;
    public LinkTreeCodeTag tag;

    public CodeStrForColour() {
    }

    public CodeStrForColour(LinkTreeCodeTag tag) {
        this.tag = tag;
    }

    public CodeStrForColour(LinkTreePosition pos, LinkTreeCodeTag tag) {
        this.pos = pos;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return tag.toString() + "\tPos: " + pos.rowS + ":" + pos.colS + " " + pos.rowE + ":" + pos.colE;
    }

}
