package org.ide.LinkTreeController.Tree.Nodes.FileNodes;

import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.AInternalCodeNode;
import org.ide.LinkTreeController.Tree.Nodes.CodeStrForColour;
import org.ide.LinkTreeController.Tree.Nodes.Abstract.FileNode;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

public class CommonFile extends FileNode {
    Map<String, AInternalCodeNode> codeNodes = new HashMap<>();

    public CommonFile(ReadWriteLock lock, String name) {
        super(lock, name);
    }


    @Override
    public Path searchForDeclaration(Path path, String name) {
        if (path.getNameCount() == 0) {
            for (AInternalCodeNode node : codeNodes.values()) {
                if (node.)
            }
        }

        return null;
    }

    @Override
    public void searchForDeclaration(String name, ExecutorService service, Queue<Future<Path>> futures) {
        //TODO implement
    }

    @Override
    public Path searchForDeclarationInNode(String name) {
        //TODO implement
        return null;
    }

    @Override
    public AInternalCodeNode getDeclaration(Path path) throws NoDeclarationException {
        //TODO implement
        return null;
    }

    @Override
    public Path searchForDefinition(Path path, String name) {
        //TODO implement
        return null;
    }

    @Override
    public void searchForDefinition(String name, ExecutorService service, Queue<Future<Path>> futures) {
        //TODO implement
    }

    @Override
    public Path searchForDefinitionInNode(String name) {
        //TODO implement
        return null;
    }

    @Override
    public AInternalCodeNode getDefinition(Path path) throws NoDefinitionException {
        //TODO implement
        return null;
    }

    @Override
    public void getHints(Path pathToFile, String prefix, List<String> listOfHints) {
        //TODO implement
    }

    @Override
    public List<CodeStrForColour> getSyntaxHughlighting(Path pathToFile) {
        //TODO implement
        return List.of();
    }

    @Override
    public void addDir(Path pathToDir, String name) {
        throw new RuntimeException("Create dir in file");
    }

    @Override
    public void updateCode(Path pathToModule, ParseTree tree) {
        //TODO implement
    }
}
