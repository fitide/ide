package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;

public class DefinitionByPathFinder extends FinderByPath {
    public DefinitionByPathFinder(CommonFileNode root, Path path, String name) {
        super(root, path, name);
    }

    @Override
    protected Path search() {
        return commonFileNode.childs.get(path.getRoot().toString()).searchForDefinition(path.subpath(1, path.getNameCount()), name);
    }
}
