package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class FileNode {
    public String name = null;
    public FileNode parent;
    public List<LinkTreeFileTag> tags = new ArrayList<>();
    public Map<String, FileNode> childs = new HashMap<>();
    public ReadWriteLock Treelock;

    public FileNode(ReentrantReadWriteLock lock) {
        this.Treelock = lock;
    }

    public abstract Path searchForDeclaration(Path path, String name);

    public abstract void searchForDeclaration(String name, ExecutorService service, Queue<Future<Path>> futures);

    public abstract Path searchForDeclarationInNode(String name);

    public abstract AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException;

    public abstract Path searchForDefinition(Path path, String name);

    public abstract void searchForDefinition(String name, ExecutorService service, Queue<Future<Path>> futures);

    public abstract Path searchForDefinitionInNode(String name);

    public abstract AInternalCodeNode getDefinition(Path path) throws NoDefinitionException;

    public abstract void getHints(Path pathToFile, String prefix, List<String> listOfHints);

    public abstract List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile);
}
