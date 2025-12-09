package org.ide.LinkTreeController.Tree.ToolClasses;


import org.ide.PluginController.PluginInterface.Position;
import org.jetbrains.annotations.NotNull;

public class LinkTreePosition extends Position implements Comparable<LinkTreePosition>, Cloneable {
    public int rowS, colS, rowE, colE;

    public LinkTreePosition() {};

    public LinkTreePosition(Position pos) {
        this.rowS = pos.rowS;
        this.colS = pos.colS;
        this.rowE = pos.rowE;
        this.colE = pos.colE;
    }

    @Override
    public int compareTo(@NotNull LinkTreePosition pos) {
        if (rowE < pos.rowE) return 1;
        if (rowS > pos.rowE) return -1;
        boolean isStartInside = rowS < pos.rowS || (rowS == pos.rowS && colS <= pos.colS);
        boolean isEndInside = rowE > pos.rowE || colE >= pos.colE;
        if (isEndInside && isStartInside) return 0;
        if (rowS < pos.rowS) return 1;
        else return -1;
    }

    @Override
    public LinkTreePosition clone() {
        LinkTreePosition clone = new LinkTreePosition();
        clone.rowS = this.rowS;
        clone.colS = this.colS;
        clone.rowE = this.rowE;
        clone.colE = this.colE;
        return clone;
    }
}
