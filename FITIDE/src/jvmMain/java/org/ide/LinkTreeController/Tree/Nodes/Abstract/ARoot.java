package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.*;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.File;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ARoot extends FileNode {
    protected Map<String, Func> externalFunctions = new HashMap<>();
    protected Map<String, CodeClass> externalClasses = new HashMap<>();
    protected Map<String, Var> externalVars = new HashMap<>();
    protected Map<String, File> externalFiles = new HashMap<>();
    protected While whileStatement;
    protected For forStatement;
    protected If ifStatement;

    protected final int cntSearches;

    public ARoot(ReentrantReadWriteLock lock) {
        this(lock, 3);
    }

    public ARoot(ReentrantReadWriteLock lock, int cntSearches) {
        super(lock);
        this.cntSearches = cntSearches;
    }

    public void setWhileStatement(List<String> keyWordsName) {
        this.whileStatement = new While(keyWordsName);
    }

    public void setIfStatement(List<String> keyWordsName) {
        this.ifStatement = new If(keyWordsName);
    }

    public void setForStatement(List<String> keyWordsName) {
        this.forStatement = new For(keyWordsName);
    }




    public abstract Path searchForDeclaration(Path[] paths, String name) throws NoDeclarationException;

    public abstract Path searchForDeclaration(String name) throws NoDeclarationException;

    public abstract Path searchForDefinition(Path[] paths, String name) throws NoDefinitionException;

    public abstract Path searchForDefinition(String Name) throws NoDefinitionException;

    public abstract List<String> getHints(Path pathToModule, String prefix);
}
