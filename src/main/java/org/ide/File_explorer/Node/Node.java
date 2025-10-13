package org.ide.File_explorer.Node;

public abstract class Node {
    public String name;
    public String path;

    protected Node(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return this.toString(0);
    }

    protected abstract String toString(int level);
}
