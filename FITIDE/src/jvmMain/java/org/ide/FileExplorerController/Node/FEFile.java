package org.ide.FileExplorerController.Node;

import javax.swing.*;

public class FEFile extends Node {

    public FEFile(String name) {
        super(name);
    }
    public Icon icon;

    @Override
    protected String toString(int level) {
        StringBuilder builder = new StringBuilder();
        builder.repeat("  ", level).append(name).append(";\n");
        return builder.toString();
    }
}
