package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Finders.DefinitionFinder;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;
import org.ide.LinkTreeController.Tree.Finders.DeclarationByPathFinder;
import org.ide.LinkTreeController.Tree.Finders.DeclarationFinder;
import org.ide.LinkTreeController.Tree.Finders.DefinitionByPathFinder;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Root extends ARoot {

    public Root(ReentrantReadWriteLock lock) {
        super(lock);
    }

    public Root(ReentrantReadWriteLock lock, int cntSearchers) {
        super(lock, cntSearchers);
    }

    @Override
    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            return null;
        }

        if (this.childs.containsKey(path.getRoot().toString())) {
            return this.childs
                    .get(path.getRoot().toString())
                            .searchForDeclaration(path.subpath(1, path.getNameCount()), name);
        }

        return null;
    }

    @Override
    public void searchForDeclaration(String name, ExecutorService service, Queue<Future<Path>> futures) {
        synchronized (futures) {
            for (FileNode children : this.childs.values()) {
                futures.add(service.submit(new DeclarationFinder(children, name, service, futures)));
            }
        }
    }

    @Override
    public Path searchForDeclaration(Path[] paths, String name) throws NoDeclarationException {
        Path path = searchByPaths(paths, name, new SearchByPathMethodBuilder() {
            @Override
            public Callable<Path> makeSearchMethod(FileNode node, Path path, String name) {
                return new DeclarationByPathFinder(node, path, name);
            }
        });

        if (path == null) {
            throw new NoDeclarationException("No declaration for: " + name);
        }

        return path;
    }

    @Override
    public Path searchForDeclaration(String name) throws NoDeclarationException {
        Path ans = search(name, new SearchMethodBuilder() {
            @Override
            public Callable<Path> makeSsearchMethod(FileNode node, String name, ExecutorService service, Queue<Future<Path>> futures) {
                return new DeclarationFinder(node, name, service, futures);
            }
        });

        if (ans == null) {
            throw new NoDeclarationException("No declaration for " + name);
        }

        return ans;
    }

    @Override
    public Path searchForDeclarationInNode(String name) {
        if (this.externalFunctions.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalClasses.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalVars.containsKey(name)) {
            return Paths.get(name);
        }

        return null;
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException {
        if (path.getNameCount() == 1) {
            if (this.externalFunctions.containsKey(name)) {
                return this.externalFunctions.get(name);
            }

            if (this.externalClasses.containsKey(name)) {
                return this.externalClasses.get(name);
            }

            if (this.externalVars.containsKey(name)) {
                return this.externalVars.get(name);
            }

        }

        if (path.getNameCount() > 1 && childs.containsKey(path.getRoot().toString())) {
            return childs.get(path.getRoot().toString()).getDeclaration(path.subpath(1, path.getNameCount()));
        }

        throw new NoDeclarationException("No declaration for " + name);
    }


    @Override
    public Path searchForDefinition(Path path, String name) {
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

    @Override
    public void searchForDefinition(String name, ExecutorService service, @NotNull Queue<Future<Path>> futures) {
        synchronized (futures) {
            for (FileNode children : this.childs.values()) {
                futures.add(service.submit(new DefinitionFinder(children, name, service, futures)));
            }
        }
    }

    @Override
    public Path searchForDefinition(Path[] paths, String name) throws NoDefinitionException {
        Path path = searchByPaths(paths, name, new SearchByPathMethodBuilder() {
            @Override
            public Callable<Path> makeSearchMethod(FileNode node, Path path, String name) {
                return new DefinitionByPathFinder(node, path, name);
            }
        });

        if (path == null) {
            throw new NoDefinitionException("No definition for: " + name);
        }

        return path;
    }

    @Override
    public Path searchForDefinition(String Name) throws NoDefinitionException {
        Path ans = search(name, new SearchMethodBuilder() {
            @Override
            public Callable<Path> makeSsearchMethod(FileNode node, String name, ExecutorService service, Queue<Future<Path>> futures) {
                return new DefinitionFinder(node, name, service, futures);
            }
        });

        if (ans == null) {
            throw new NoDefinitionException("No definition for " + name);
        }

        return ans;
    }

    @Override
    public List<String> getHints(Path pathToModule, String prefix) {
        List<String> listOfHints = new ArrayList<>();
        getHints(pathToModule, prefix, listOfHints);
        return listOfHints;
    }

    @Override
    public void getHints(Path pathToFile, String prefix, List<String> listOfHints) {

        whileStatement.getKeyWordsOfStatement(prefix, listOfHints);
        ifStatement.getKeyWordsOfStatement(prefix, listOfHints);
        forStatement.getKeyWordsOfStatement(prefix, listOfHints);
        getHintsFromMap(prefix, listOfHints, externalClasses.keySet());
        getHintsFromMap(prefix, listOfHints, externalFunctions.keySet());
        getHintsFromMap(prefix, listOfHints, externalVars.keySet());

        if (pathToFile.getNameCount() > 0 && this.childs.containsKey(pathToFile.getRoot().toString())) {
            childs.get(pathToFile.getRoot().toString()).getHints(pathToFile.subpath(1, pathToFile.getNameCount()), prefix, listOfHints);
        }
    }

    @Override
    public List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile) {
        if (pathToFile.getNameCount() > 0 && this.childs.containsKey(pathToFile.getRoot().toString())) {
            return childs.get(pathToFile.getRoot().toString()).getSyntaxHughlighting(pathToFile.subpath(1, pathToFile.getNameCount()));
        }
        return new ArrayList<>();
    }

    private void getHintsFromMap(String prefix, List<String> listOfHints, Set<String> names) {
        for (String name : names) {
            if (name.startsWith(prefix)) {
                listOfHints.add(name);
            }
        }
    }


    @Override
    public Path searchForDefinitionInNode(String name) {
        if (this.externalFunctions.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalClasses.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalVars.containsKey(name)) {
            return Paths.get(name);
        }

        return null;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) throws NoDefinitionException {
        if (path.getNameCount() == 1) {
            if (this.externalFunctions.containsKey(name)) {
                return this.externalFunctions.get(name);
            }

            if (this.externalClasses.containsKey(name)) {
                return this.externalClasses.get(name);
            }

            if (this.externalVars.containsKey(name)) {
                return this.externalVars.get(name);
            }

        }

        if (path.getNameCount() > 0 && childs.containsKey(path.getRoot().toString())) {
            return childs.get(path.getRoot().toString()).getDefinition(path.subpath(1, path.getNameCount()));
        }

        throw new NoDefinitionException("No definition for " + name);
    }



    private interface SearchByPathMethodBuilder {
        Callable<Path> makeSearchMethod (FileNode fileNode, Path path, String name);
    }

    private Path searchByPaths(Path[] paths, String name, SearchByPathMethodBuilder method) {
        try {
            ExecutorService service = Executors.newScheduledThreadPool(cntSearches);
            Path ans = null;
            List<Future<Path>> futures = new ArrayList<>();

            for (Path path : paths) {
                futures.add(service.submit(method.makeSearchMethod(this, path, name)));
            }

            for (Future<Path> future : futures) {
                Path curPath = future.get();
                if (curPath != null) {
                    ans = curPath;
                    break;
                }
            }

            service.shutdownNow();
            return ans;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private interface SearchMethodBuilder {
        Callable<Path> makeSsearchMethod(FileNode fileNode, String name, ExecutorService service, Queue<Future<Path>> futures);
    }

    private Path search(String name, SearchMethodBuilder searchMethodBuilder) {
        if (externalFunctions.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalClasses.containsKey(name)) {
            return Paths.get(name);
        }

        if (this.externalVars.containsKey(name)) {
            return Paths.get(name);
        }

        try {
            Path ans = null;
            ExecutorService service = Executors.newScheduledThreadPool(cntSearches);
            Queue<Future<Path>> futures = new ArrayDeque<>();
            futures.add(service.submit(searchMethodBuilder.makeSsearchMethod(this, name, service, futures)));

            while(!futures.isEmpty()) {
                Future<Path> future;
                synchronized (futures) {
                    future = futures.peek();
                    futures.poll();
                }

                ans = future.get();
                if (ans != null) {
                    break;
                }
            }
            service.shutdownNow();

            return ans;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
