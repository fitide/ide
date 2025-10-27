package org.ide.FileExplorerController.Node;

public abstract class Node {
    public String name;

    protected Node(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    protected abstract String toString(int level);
}
