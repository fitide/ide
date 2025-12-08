package org.cdm;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.cdm.antlr.*;
import org.ide.PluginController.PluginInterface.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CdmPlugin implements Plugin {
    ExternalType registerType = null;
    List<ExternalVar> externalVars = null;

    public CdmPlugin() {
        registerType = new ExternalType("register");
        externalVars = initExternalVars();
    }

    @Override
    public String fileExtension() {
        return ".asm";
    }

    @Override
    public ParseTree getFileParseTree(File file) {
        String fileContent = null;
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                sb.append(line);
            }
            fileContent = sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO : сделать нормальную обработку ошибок
        }

        CharStream charStream = CharStreams.fromString(fileContent);
        CdmLexer lexer = new CdmLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CdmParser parser = new CdmParser(tokens);

        return parser.program();
    }

    //cdm doesn't have standard files
    @Override
    public List<ExternalFile> getStandartFiles() {
        return List.of();
    }

    @Override
    public List<ExternalFunc> getStandartFuncs() {
return List.of(

        );
    }

    @Override
    public List<ExternalVar> getStandartVars() {
        return externalVars;
    }

    //cdm8 doesn't have any standard classes
    @Override
    public List<ExternalClass> getStandartClasses() {
        return List.of();
    }

    @Override
    public List<ExternalType> getStandartTypes() {
        return List.of(registerType);
    }

    @Override
    public List<ExternalConstruction> getStandartConstructionsLike() {
        return List.of();
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree) {
        return null;
    }

    @Override
    public Position getPositionOfModuleBody(ParseTree tree) {
        return null;
    }

    @Override
    public List<ParseTree> getFuncsOfClass(ParseTree classNode) {
        return List.of();
    }

    @Override
    public List<ParseTree> getBodeOfModule(ParseTree module) {
        return List.of();
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        return List.of();
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func) {
        return List.of();
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        return List.of();
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        return null;
    }

    @Override
    public String getType(ParseTree tree) {
        return "";
    }

    @Override
    public Position getNamePositionOfModule(ParseTree node) {
        return null;
    }

    @Override
    public Position getBounds(ParseTree node) {
        return null;
    }

    @Override
    public List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement) {
        return List.of();
    }

    @Override
    public String getNameOfNode(ParseTree node) {
        return "";
    }

    @Override
    public Tag[] getTagsOfNode(ParseTree tree) {
        return new Tag[0];
    }




    private List<ExternalVar> initExternalVars() {
        List<ExternalVar> vars = new ArrayList<>();

        vars.add(new ExternalVar(registerType, "r0"));
        vars.add(new ExternalVar(registerType, "r1"));
        vars.add(new ExternalVar(registerType, "r2"));
        vars.add(new ExternalVar(registerType, "r3"));

        return vars;
    }
}
