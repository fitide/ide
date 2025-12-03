package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

public class Directory extends FileNode {

    public Directory(ReadWriteLock lock, Path pathToFile, String name) {
        super(lock, pathToFile, name);
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException {
        if (path.getNameCount() > 1 && childs.containsKey(path.getRoot().toString())) {
            return childs.get(path.getRoot().toString()).getDeclaration(path.subpath(1, path.getNameCount()));
        }

        throw new NoDeclarationException("No declaration for " + name);
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) throws NoDefinitionException {
        if (path.getNameCount() > 0 && childs.containsKey(path.getRoot().toString())) {
            return childs.get(path.getRoot().toString()).getDefinition(path.subpath(1, path.getNameCount()));
        }

        throw new NoDefinitionException("No definition for " + name);
    }

    @Override
    public void getHints(Path pathToFile, String prefix, Set<String> listOfHints) throws BadPathException {
        if (pathToFile.getNameCount() > 0 && this.childs.containsKey(pathToFile.getRoot().toString())) {
            childs.get(pathToFile.getRoot().toString()).getHints(pathToFile.subpath(1, pathToFile.getNameCount()), prefix, listOfHints);
        }

        throw new BadPathException("Bad Path");
    }
}
