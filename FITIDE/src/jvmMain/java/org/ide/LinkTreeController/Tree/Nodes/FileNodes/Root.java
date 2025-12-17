package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Finders.DefinitionFinder;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.ARoot;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.LinkTreeCodeTag;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.Construction;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.CommonFileNode;
import org.ide.LinkTreeController.Tree.Finders.DeclarationByPathFinder;
import org.ide.LinkTreeController.Tree.Finders.DeclarationFinder;
import org.ide.LinkTreeController.Tree.Finders.DefinitionByPathFinder;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;
import org.ide.LinkTreeController.Tree.ToolClasses.PathTools;

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
        super(lock, Paths.get(""), cntSearchers);
    }

    @Override
    public Path searchForDeclaration(List<Path> paths, String name) throws NoDeclarationException {

        Path path = searchForDeclarationInNode(name);
        if (path != null) return path;

         path = searchByPaths(paths, name, new SearchByPathMethodBuilder() {
            @Override
            public Callable<Path> makeSearchMethod(CommonFileNode node, Path path, String name) {
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
            public Callable<Path> makeSsearchMethod(CommonFileNode node, String name, ExecutorService service, Queue<Future<Path>> futures) {
                return new DeclarationFinder(node, name, service, futures);
            }
        });

        if (ans == null) {
            throw new NoDeclarationException("No declaration for " + name);
        }

        return ans;
    }

    @Override
    public Path searchForDeclaration(Path path, String name) {
        if (externalFiles.containsKey(PathTools.getRootStr(path))) {
            return externalFiles.get(PathTools.getRootStr(path)).searchForDeclaration(PathTools.deleteRoot(path), name);
        }

        return super.searchForDeclaration(path, name);
    }

    @Override
    public Path searchForDeclarationInNode(String name) {
        if (this.externalFunctions.containsKey(name)) {
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
        if (externalFiles.containsKey(PathTools.getRootStr(path))) {
            return externalFiles.get(PathTools.getRootStr(path)).searchForDefinition(PathTools.deleteRoot(path), name);
        }

        return super.searchForDeclaration(path, name);
    }

    @Override
    public Path searchForDefinition(List<Path> paths, String name) throws NoDefinitionException {

        Path path = searchForDefinitionInNode(name);
        if (path != null) return path;

        path = searchByPaths(paths, name, new SearchByPathMethodBuilder() {
            @Override
            public Callable<Path> makeSearchMethod(CommonFileNode node, Path path, String name) {
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
            public Callable<Path> makeSsearchMethod(CommonFileNode node, String name, ExecutorService service, Queue<Future<Path>> futures) {
                return new DefinitionFinder(node, name, service, futures);
            }
        });

        if (ans == null) {
            throw new NoDefinitionException("No definition for " + name);
        }

        return ans;
    }

    @Override
    public Path searchForDefinitionInNode(String name) {
        if (this.externalFunctions.containsKey(name)) {
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

            if (this.externalVars.containsKey(name)) {
                return this.externalVars.get(name);
            }

        }

        if (path.getNameCount() > 0 && childs.containsKey(path.getRoot().toString())) {
            return childs.get(path.getRoot().toString()).getDefinition(path.subpath(1, path.getNameCount()));
        }

        throw new NoDefinitionException("No definition for " + name);
    }


    @Override
    public Set<HintNode> getHints(Path pathToModule, String prefix) throws BadPathException {
        Set<HintNode> listOfHints = new HashSet<>();
        getHints(pathToModule, prefix, listOfHints);
        return listOfHints;
    }

    @Override
    public void getStandartDecs(Map<String, AInternalCodeNode> decs) {
        decs.putAll(externalFunctions);
        decs.putAll(externalVars);
    }

    @Override
    public void getHints(Path pathToFile, String prefix, Set<HintNode> listOfHints) throws BadPathException {

        for(Construction construction : externalConstrs.values()) {
            construction.getCommonHints(prefix, listOfHints);
        }

        getHintsFromMap(prefix, listOfHints, externalFunctions.keySet(), LinkTreeCodeTag.Func);
        getHintsFromMap(prefix, listOfHints, externalVars.keySet(), LinkTreeCodeTag.Var);

        String nextNode = PathTools.getRootStr(PathTools.deleteRoot(pathToFile));

        if (pathToFile.getNameCount() > 0 && this.childs.containsKey(nextNode)) {
            childs.get(nextNode).getHints(pathToFile.subpath(1, pathToFile.getNameCount()), prefix, listOfHints);
            return;
        } else if (nextNode.length() == 0) {
            return;
        }

        throw new BadPathException("Bad Path");
    }

    private void getHintsFromMap(String prefix, Set<HintNode> listOfHints, Set<String> names, LinkTreeCodeTag codeType) {
        for (String name : names) {
            if (name.startsWith(prefix)) {
                listOfHints.add(new HintNode(codeType, name));
            }
        }
    }

    private interface SearchByPathMethodBuilder {
        Callable<Path> makeSearchMethod (CommonFileNode commonFileNode, Path path, String name);
    }

    private Path searchByPaths(List<Path> paths, String name, SearchByPathMethodBuilder method) {
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
        Callable<Path> makeSsearchMethod(CommonFileNode commonFileNode, String name, ExecutorService service, Queue<Future<Path>> futures);
    }

    private Path search(String name, SearchMethodBuilder searchMethodBuilder) {
        if (externalFunctions.containsKey(name)) {
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
