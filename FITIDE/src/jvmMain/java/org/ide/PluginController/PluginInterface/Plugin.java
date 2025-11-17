package org.ide.PluginController.PluginInterface;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Plugin {

    ParseTree getFileParseTree(File file);

    Tag[] getTagsOfNode(ParseTree tree);

    List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement);

    Position getBounds(ParseTree node);

    // node is a class/func/var
    Position getNamePositionOfModule(ParseTree node);

    // node is a class/func/var
    Position getTypePositionOfModule(ParseTree node);

    List<ParseTree> getKeyWordsOfModule(ParseTree node);

    List<ParseTree> getArgsOfFunc(ParseTree func);

    List<ParseTree> getConstructionArgs(ParseTree constr);
    //module is func/while/for/if etc
    List<ParseTree> getBodeOfModule(ParseTree module);

    List<ParseTree> getFuncsOfClass(ParseTree classNode);

    List<ExternalFile> getStandartFiles();

    List<ExternalFunc> getStandartFuncs();

    List<ExternalVar> getStandartVars();

    List<ExternalClass> getStandartClasses();

    List<ExternalType> getStandartTypes();

    // for/while/if etc
    List<ExternalConstruction> getStandartConstructionsLike();
}
