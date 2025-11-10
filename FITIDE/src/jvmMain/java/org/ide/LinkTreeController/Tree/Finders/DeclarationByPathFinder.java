package org.ide.LinkTreeController.Tree.Finders;

import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;

import java.nio.file.Path;

public class DeclarationByPathFinder extends FinderByPath {
    public DeclarationByPathFinder(FileNode fileNode, Path path, String name) {
        super(fileNode, path, name);
    }

    @Override
    protected Path search() {
        return fileNode.childs.get(path.getRoot().toString()).searchForDeclaration(path.subpath(1, path.getNameCount()), name);
    }
}
