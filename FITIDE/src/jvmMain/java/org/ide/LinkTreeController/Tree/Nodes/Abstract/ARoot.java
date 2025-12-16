package org.ide.LinkTreeController.Tree.Nodes.Abstract;

import org.ide.LinkTreeController.Exceptions.BadPathException;
import org.ide.LinkTreeController.Exceptions.NoDeclarationException;
import org.ide.LinkTreeController.Exceptions.NoDefinitionException;
import org.ide.LinkTreeController.Tree.Nodes.CodeNodes.*;
import org.ide.LinkTreeController.Tree.Nodes.FileNodes.CommonFile;
import org.ide.LinkTreeController.Tree.ToolClasses.HintNode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ARoot extends CommonFileNode {
    protected Map<String, Func> externalFunctions = new HashMap<>();
    protected Map<String, Var> externalVars = new HashMap<>();
    protected Map<String, CommonFile> externalFiles = new HashMap<>();
    protected Set<String> standartTypes = new HashSet<>();
    protected final int cntSearches;
    protected Map<String, Construction> externalConstrs = new HashMap<>();


    public ARoot(ReentrantReadWriteLock lock) {
        this(lock, Paths.get(""), 3);
    }

    public ARoot(ReentrantReadWriteLock lock, Path pathToFile, int cntSearches) {
        super(lock, pathToFile, null);
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

    public void setExternalFunctions(Map<String, Func> externalFunctions) {
        this.externalFunctions = externalFunctions;
    }

    public void setStandartTypes(Set<String> standartTypes) {
        this.standartTypes = standartTypes;
    }


    public abstract Path searchForDeclaration(List<Path> paths, String name) throws NoDeclarationException;

    public abstract Path searchForDeclaration(String name) throws NoDeclarationException;

    public abstract Path searchForDefinition(List<Path> paths, String name) throws NoDefinitionException;

    public abstract Path searchForDefinition(String Name) throws NoDefinitionException;

    public abstract Set<HintNode> getHints(Path pathToModule, String prefix) throws BadPathException;

    public Set<String> getStandartTypes() {
        return standartTypes;
    }

    public Map<String, Construction> getExternalConstrs() {
        return externalConstrs;
    }

    public Map<String, CommonFile> getExternalFiles() {
        return externalFiles;
    }

    public Map<String, Var> getExternalVars() {
        return externalVars;
    }

    public Map<String, Func> getExternalFunctions() {
        return externalFunctions;
    }

    public abstract void getStandartDecs(Map<String, AInternalCodeNode> decs);
}
