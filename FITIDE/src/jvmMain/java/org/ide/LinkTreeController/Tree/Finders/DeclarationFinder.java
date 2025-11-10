package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DeclarationFinder extends Finder {

    public DeclarationFinder(FileNode fileNode, String name, ExecutorService service, Queue<Future<Path>> futures) {
        super(fileNode, name, service, futures);
    }

    @Override
    protected Path searchInNode() {
        return fileNode.searchForDeclarationInNode(name);
    }

    @Override
    protected void extendForChilds() {
        fileNode.searchForDeclaration(name, service, futures);
    }

}
