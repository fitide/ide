package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DeclarationFinder extends Finder {

    public DeclarationFinder(CommonFileNode commonFileNode, String name, ExecutorService service, Queue<Future<Path>> futures) {
        super(commonFileNode, name, service, futures);
    }

    @Override
    protected Path searchInNode() {
        return commonFileNode.searchForDeclarationInNode(name);
    }

    @Override
    protected void extendForChilds() {
        commonFileNode.searchForDeclaration(name, service, futures);
    }

}
