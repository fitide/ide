package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class Finder implements Callable<Path> {
    protected final CommonFileNode commonFileNode;
    protected final String name;
    protected final ExecutorService service;
    protected final Queue<Future<Path>> futures;

    protected Finder(CommonFileNode commonFileNode, String name, ExecutorService service, Queue<Future<Path>> futures) {
        this.commonFileNode = commonFileNode;
        this.name = name;
        this.service = service;
        this.futures = futures;
    }

    @Override
    public Path call() {
        Path pathToFind = searchInNode();
        if (pathToFind != null) return pathToFind;

        extendForChilds();

        return null;
    }

    protected abstract Path searchInNode();

    protected abstract void extendForChilds();
}
