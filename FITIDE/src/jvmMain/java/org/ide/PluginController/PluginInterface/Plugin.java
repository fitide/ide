package org.ide.PluginController.PluginInterface;

import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public interface Plugin {

    ParseTree getFileParseTree(File file);

    Tag[] getTagsOfNode(ParseTree tree);

    List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement);


    // node is a class/func/var
    Position getNamePositionOfModule(ParseTree node);

    // node is a class/func/var
    Position getTypePositionOfModule(ParseTree node);

    List<Position> getPositionsOfKeyWordsOfModule(ParseTree node);

    List<ParseTree> getArgsOfFunc(ParseTree func);

    ParseTree getWhileCondStatement(ParseTree whileNode);

    List<ParseTree> getForStatements(ParseTree forNode);

    ParseTree getIfCondStatement(ParseTree ifNode);

    //module is func/while/for/if
    List<ParseTree> getBodeOfModule(ParseTree module);

    List<ParseTree> getFuncsOfClass(ParseTree classNode);

    List<ExternalFile> getStandartFiles();

    List<ExternalFunc> getStandartFuncs();

    List<ExternalVar> getStandartVars();

    List<ExternalClass> getStandartClasses();

    List<ExternalType> getStandartTypes();
}
