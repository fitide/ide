package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FileNode extends CommonFileNode {
    public String name;

    protected FileNode() {};

    public FileNode(ReadWriteLock lock, Path pathToFile, String name) {
        super(lock, pathToFile);
        this.name = name;
    }
}
