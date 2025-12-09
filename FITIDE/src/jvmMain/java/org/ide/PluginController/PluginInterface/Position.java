package org.ide.PluginController.PluginInterface;

public class Position {
    public int rowS, colS;
    public int rowE, colE;

    public Position() {
    }

    public Position(int rowS, int colS, int rowE, int colE) {
        this.rowS = rowS;
        this.colS = colS;
        this.rowE = rowE;
        this.colE = colE;
    }
}
