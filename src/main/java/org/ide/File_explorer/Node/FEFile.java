package org.ide.File_explorer.Node;

public class FEFile extends Node {

    public FEFile(String name, String path) {
        super(name, path);
    }

    @Override
    protected String toString(int level) {
        StringBuilder builder = new StringBuilder();
        builder.repeat("  ", level).append(name).append(";\n");
        return builder.toString();
    }
}
