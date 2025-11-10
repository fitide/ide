package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import java.nio.file.Path;

public abstract class AInternalCodeNodeEnd {
    public Path pathToFile;
    public int column;
    public int row;


    AInternalCodeNodeEnd(Path path, int row, int column) {
        this.column = column;
        this.row = row;
        this.pathToFile = path;
    }


    public void setPathToFile(Path pathToFile) {
        this.pathToFile = pathToFile;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

}
