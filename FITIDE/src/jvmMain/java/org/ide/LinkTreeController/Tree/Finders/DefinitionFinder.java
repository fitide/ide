package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DefinitionFinder extends Finder {


    public DefinitionFinder(CommonFileNode commonFileNode, String name, ExecutorService service, Queue<Future<Path>> futures) {
        super(commonFileNode, name, service, futures);
    }

    @Override
    protected Path searchInNode() {
        return commonFileNode.searchForDefinitionInNode(name);
    }

    @Override
    protected void extendForChilds() {
        commonFileNode.searchForDefinition(name, service, futures);
    }
}
