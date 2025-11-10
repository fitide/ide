package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public abstract  class FinderByPath implements Callable<Path> {
    protected final FileNode fileNode;
    protected Path path;
    protected final String name;

    FinderByPath(FileNode root, Path path, String name) {
        this.path = path;
        this.fileNode = root;
        this.name = name;
    }


    @Override
    public Path call() {
        Path pathToFind;

        try {
            fileNode.Treelock.readLock().lock();
            if (fileNode.childs.containsKey(path.getRoot().toString())) {
                pathToFind = search();
            } else {
                pathToFind = null;
            }
        } finally {
            fileNode.Treelock.readLock().unlock();
        }

        return pathToFind;
    }

    protected abstract Path search();
}
