package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.*;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ARoot extends CommonFileNode {
    protected Map<String, Func> externalFunctions = new HashMap<>();
    protected Map<String, CodeClass> externalClasses = new HashMap<>();
    protected Map<String, Var> externalVars = new HashMap<>();
    protected Map<String, CommonFile> externalFiles = new HashMap<>();
    protected final int cntSearches;
    protected Map<String, Construction> externalConstrs = new HashMap<>();


    public ARoot(ReentrantReadWriteLock lock) {
        this(lock, 3);
    }

    public ARoot(ReentrantReadWriteLock lock, int cntSearches) {
        super(lock);
        this.cntSearches = cntSearches;
    }


    public void setExternalConstrs(Map<String, Construction> externalConstrs) {
        this.externalConstrs = externalConstrs;
    }

    public void setExternalFiles(Map<String, CommonFile> externalFiles) {
        this.externalFiles = externalFiles;
    }

    public void setExternalVars(Map<String, Var> externalVars) {
        this.externalVars = externalVars;
    }

    public void setExternalClasses(Map<String, CodeClass> externalClasses) {
        this.externalClasses = externalClasses;
    }

    public void setExternalFunctions(Map<String, Func> externalFunctions) {
        this.externalFunctions = externalFunctions;
    }


    public abstract Path searchForDeclaration(List<Path> paths, String name) throws NoDeclarationException;

    public abstract Path searchForDeclaration(String name) throws NoDeclarationException;

    public abstract Path searchForDefinition(List<Path> paths, String name) throws NoDefinitionException;

    public abstract Path searchForDefinition(String Name) throws NoDefinitionException;

    public abstract List<String> getHints(Path pathToModule, String prefix) throws BadPathException;
}
