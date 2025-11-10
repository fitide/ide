package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FileNode extends CommonFileNode {
    public String name;

    public FileNode(ReadWriteLock lock, String name) {
        super(lock);
        this.name = name;
    }
}
