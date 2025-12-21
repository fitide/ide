package org.ide.LinkTreeController.SimplePlugin;

import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ide.PluginController.PluginInterface.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SimplePlugin implements Plugin {
    @Override
    public String fileExtension() {
        return ".txt";
    }

    @Override
    public ParseTree getFileParseTree(File file) {
        return null;
    }

    @Override
    public Tag[] getTagsOfNode(ParseTree tree) {
        SimpleParseTree node = (SimpleParseTree) tree;
        Tag[] tags = new Tag[2];
        var pair = (Pair<Tag, Tag>)node.getPayload();
        tags[0] = pair.a;
        tags[1] = pair.b;
        return tags;
    }

    @Override
    public String getNameOfNode(ParseTree node) {
        return ((SimpleParseTree) node).getText();
    }

    @Override
    public List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement) {
        return List.of();
    }

    @Override
    public Position getBounds(ParseTree node) {
        return ((SimpleParseTree) node).fullPos;
    }

    @Override
    public Position getNamePositionOfModule(ParseTree node) {
        return ((SimpleParseTree) node).namePos;
    }

    @Override
    public String getType(ParseTree tree) {
        return ((SimpleParseTree) tree).toStringTree();
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        return ((SimpleParseTree) node).typePos;
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        return List.of();
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func) {
        return List.of();
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        return List.of();
    }

    @Override
    public List<ParseTree> getChildsOfNode(ParseTree module) {
        return ((SimpleParseTree) module).body;
    }

    @Override
    public List<ParseTree> getFuncsOfClass(ParseTree classNode) {
        return List.of();
    }

    @Override
    public List<ExternalFile> getStandartFiles() {
        return List.of();
    }

    @Override
    public List<ExternalFunc> getStandartFuncs() {
        return List.of();
    }

    @Override
    public List<ExternalVar> getStandartVars() {
        return List.of();
    }

    @Override
    public List<ExternalClass> getStandartClasses() {
        return List.of();
    }

    @Override
    public List<ExternalType> getStandartTypes() {
        String []types = new String[] {"int", "double", "char"};
        List<ExternalType> list = new ArrayList<>();
        for (var type : types) {
            var extType = new ExternalType(type);
            extType.name = type;
            list.add(extType);
        }
        return list;
    }

    @Override
    public List<ExternalConstruction> getStandartConstructionsLike() {
        return List.of();
    }

    @Override
    public Position getPositionOfModuleBody(ParseTree tree) {
        return ((SimpleParseTree) tree).fullPos;
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree) {
        return null;
    }
}
