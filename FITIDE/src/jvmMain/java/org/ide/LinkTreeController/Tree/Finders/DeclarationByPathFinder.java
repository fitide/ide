package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;

import java.nio.file.Path;

public class DeclarationByPathFinder extends FinderByPath {
    public DeclarationByPathFinder(CommonFileNode commonFileNode, Path path, String name) {
        super(commonFileNode, path, name);
    }

    @Override
    protected Path search() {
        return commonFileNode.childs.get(path.getRoot().toString()).searchForDeclaration(path.subpath(1, path.getNameCount()), name);
    }
}
