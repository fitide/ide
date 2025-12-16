package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Finders.DeclarationFinder;
import org.ide.LinkTreeController.Tree.Finders.DefinitionFinder;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.ToolClasses.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.Directory;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.LinkTreePosition;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;
import org.ide.PluginController.PluginInterface.Plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

public abstract class CommonFileNode {
    public String name = null;
    public Path pathToFile;
    public CommonFileNode parent;
    public List<LinkTreeFileTag> tags = new ArrayList<>();
    public Map<String, CommonFileNode> childs = new HashMap<>();
    public ReadWriteLock Treelock;

    public CommonFileNode(ReadWriteLock lock, Path pathToFile, String name) {
        this.pathToFile = pathToFile;
        this.Treelock = lock;
        this.name = name;
    }

    protected CommonFileNode() {
    }

    public List<Path> getPathsToSearchDeclaration(Path pathToModule) {
        if (pathToModule.getNameCount() == 0) return null;

        if (this.childs.containsKey(PathTools.getRootStr(pathToModule))){
            return this.childs.get(PathTools.getRootStr(pathToModule)).getPathsToSearchDeclaration(PathTools.deleteRoot(pathToModule));
        }

        return null;
    }

    public List<Path> getPathsToSearchDeclaration(Path pathToFile, LinkTreePosition linkTreePosition) {
        if (pathToFile.getNameCount() == 0) return null;

        if (this.childs.containsKey(PathTools.getRootStr(pathToFile))){
            return this.childs.get(PathTools.getRootStr(pathToFile)).getPathsToSearchDeclaration(PathTools.deleteRoot(pathToFile), linkTreePosition);
        }

        return null;
    }

    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            return null;
        }

        if (this.childs.containsKey(PathTools.getRootStr(path))) {
            return this.childs
                    .get(PathTools.getRootStr(path))
                    .searchForDeclaration(PathTools.deleteRoot(path), name);
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

    public List<Path> getPathsToSearchDefinition(Path pathToModule) {
        if (pathToModule.getNameCount() == 0) return null;

        if (this.childs.containsKey(PathTools.getRootStr(pathToModule))){
            return this.childs.get(PathTools.getRootStr(pathToModule)).getPathsToSearchDefinition(PathTools.deleteRoot(pathToModule));
        }

        return null;
    }

    public List<Path> getPathsToSearchDefinition(Path pathToFile, LinkTreePosition linkTreePosition) {
        if (pathToFile.getNameCount() == 0) return null;

        if (this.childs.containsKey(PathTools.getRootStr(pathToFile))){
            return this.childs.get(PathTools.getRootStr(pathToFile)).getPathsToSearchDefinition(PathTools.deleteRoot(pathToFile), linkTreePosition);
        }

        return null;
    }

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

    public abstract void getHints(Path pathToFile, String prefix, Set<HintNode> listOfHints) throws BadPathException;

    public List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile) {
        CommonFileNode node = getFileNode(pathToFile);

        if (node != null && node instanceof CommonFile) {
            return ((CommonFile) node).getSyntaxHughlighting(null);
        }
        return new ArrayList<>();
    }

    public void addDir(Path pathToDir, String name) throws BadPathException {
        if (pathToDir.getNameCount() == 0) {
            this.childs.put(name, new Directory(this.Treelock, pathToDir, name));
        }
        else {
            if (this.childs.containsKey(PathTools.getRootStr(pathToDir))) {
                this.childs.get(PathTools.getRootStr(pathToDir)).addDir(PathTools.deleteRoot(pathToDir), name);
            }

            throw new BadPathException("Path error");
        }
    }

    public void createFile(Path pathToFile, String name) throws BadPathException {
        if (pathToFile.getNameCount() == 0) {
            this.childs.put(name, new Directory(this.Treelock, pathToFile, name));
        }
        else {
            if (this.childs.containsKey(PathTools.getRootStr(pathToFile))) {
                this.childs.get(PathTools.getRootStr(pathToFile)).createFile(PathTools.deleteRoot(pathToFile), name);
            }

            throw new BadPathException("Path error");
        }
    }

    public void updateCode(Plugin plugin, Path pathToModule, ParseTree tree) throws BadPathException {
        if (pathToModule.getNameCount() == 0) {
            throw new BadPathException("Path error");
        }
        else {
            if (this.childs.containsKey(PathTools.getRootStr(pathToModule))) {
                this.childs.get(PathTools.getRootStr(pathToModule)).createFile(PathTools.deleteRoot(pathToModule), name);
            }


        }
    }

    public CommonFileNode getFileNode(Path pathToFile) {
        if (pathToFile == null || pathToFile.getNameCount() == 0) {
            return null;
        }
        if (this.pathToFile != null && pathToFile.isAbsolute()) {
            try {
                pathToFile = this.pathToFile.relativize(pathToFile);
            } catch (IllegalArgumentException e) {
                return null;
            }

            if (pathToFile.getNameCount() == 0) {
                return this;
            }
        }

        String firstName = pathToFile.getName(0).toString();
        if (pathToFile.getNameCount() == 1) {
            if (firstName.equals(this.name)) {
                return this;
            }
            if (this.childs.containsKey(firstName)) {
                var child = this.childs.get(firstName);
                return child;
            }
            return null;
        }

        if (this.childs.containsKey(firstName)) {
            Path remainingPath = pathToFile.subpath(1, pathToFile.getNameCount());
            return this.childs.get(firstName).getFileNode(remainingPath);
        }

        return null;
    }
}
