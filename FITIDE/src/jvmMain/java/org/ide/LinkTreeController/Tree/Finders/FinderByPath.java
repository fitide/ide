package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public abstract  class FinderByPath implements Callable<Path> {
    protected final CommonFileNode commonFileNode;
    protected Path path;
    protected final String name;

    FinderByPath(CommonFileNode root, Path path, String name) {
        this.path = path;
        this.commonFileNode = root;
        this.name = name;
    }


    @Override
    public Path call() {
        Path pathToFind;

        try {
            commonFileNode.Treelock.readLock().lock();
            if (commonFileNode.childs.containsKey(path.getRoot().toString())) {
                pathToFind = search();
            } else {
                pathToFind = null;
            }
        } finally {
            commonFileNode.Treelock.readLock().unlock();
        }

        return pathToFind;
    }

    protected abstract Path search();
}
