package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Finders.DeclarationFinder;
import org.ide.LinkTreeController.Tree.Finders.DefinitionFinder;
import org.ide.LinkTreeController.Tree.Nodes.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory;
import org.ide.LinkTreeController.Tree.Tools;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class CommonFileNode {
    public String name = null;
    public CommonFileNode parent;
    public List<LinkTreeFileTag> tags = new ArrayList<>();
    public Map<String, CommonFileNode> childs = new HashMap<>();
    public ReadWriteLock Treelock;

    public CommonFileNode(ReadWriteLock lock) {
        this.Treelock = lock;
    }

    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            return null;
        }

        if (this.childs.containsKey(Tools.getRootStr(path))) {
            return this.childs
                    .get(Tools.getRootStr(path))
                    .searchForDeclaration(Tools.deleteRoot(path), name);
        }

        return null;
    }

    public void searchForDeclaration(String name, ExecutorService service, Queue<Future<Path>> futures)  {
        synchronized (futures) {
            for (CommonFileNode children : this.childs.values()) {
                futures.add(service.submit(new DeclarationFinder(children, name, service, futures)));
            }
        }
    }

    public Path searchForDeclarationInNode(String name) {
        return null;
    }

    public abstract AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException;

    public Path searchForDefinition(Path path, String name)  {
        if (path.getNameCount() == 0) {
            return null;
        }

        if (this.childs.containsKey(path.getRoot().toString())) {
            return this.childs
                    .get(path.getRoot().toString())
                    .searchForDefinition(path.subpath(1, path.getNameCount()), name);
        }

        return null;
    }

    public void searchForDefinition(String name, ExecutorService service, Queue<Future<Path>> futures) {
        synchronized (futures) {
            for (CommonFileNode children : this.childs.values()) {
                futures.add(service.submit(new DefinitionFinder(children, name, service, futures)));
            }
        }
    }

    public Path searchForDefinitionInNode(String name) {return null;}

    public abstract AInternalCodeNode getDefinition(Path path) throws NoDefinitionException;

    public abstract void getHints(Path pathToFile, String prefix, List<String> listOfHints) throws BadPathException;

    public List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile) {
        if (pathToFile.getNameCount() > 0 && this.childs.containsKey(pathToFile.getRoot().toString())) {
            return childs.get(pathToFile.getRoot().toString()).getSyntaxHughlighting(pathToFile.subpath(1, pathToFile.getNameCount()));
        }
        return new ArrayList<>();
    }

    public void addDir(Path pathToDir, String name) throws BadPathException {
        if (pathToDir.getNameCount() == 0) {
            this.childs.put(name, new Directory(this.Treelock, name));
        }
        else {
            if (this.childs.containsKey(Tools.getRootStr(pathToDir))) {
                this.childs.get(Tools.getRootStr(pathToDir)).addDir(Tools.deleteRoot(pathToDir), name);
            }

            throw new BadPathException("Path error");
        }
    }

    public void createFile(Path pathToFile, String name) throws BadPathException {
        if (pathToFile.getNameCount() == 0) {
            this.childs.put(name, new Directory(this.Treelock, name));
        }
        else {
            if (this.childs.containsKey(Tools.getRootStr(pathToFile))) {
                this.childs.get(Tools.getRootStr(pathToFile)).createFile(Tools.deleteRoot(pathToFile), name);
            }

            throw new BadPathException("Path error");
        }
    }

    public void updateCode(Path pathToModule, ParseTree tree) throws BadPathException {
        if (pathToModule.getNameCount() == 0) {
            throw new BadPathException("Path error");
        }
        else {
            if (this.childs.containsKey(Tools.getRootStr(pathToModule))) {
                this.childs.get(Tools.getRootStr(pathToModule)).createFile(Tools.deleteRoot(pathToModule), name);
            }


        }
    }
}
