package org.cdm;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.cdm.antlr.CdmLexer;
import org.cdm.antlr.TestCdmParser;
import org.ide.PluginController.PluginInterface.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CdmPluginTest implements Plugin {
    ExternalType registerType = null;
    ExternalType immediateType = null;
    ExternalType voidType = null;
    List<ExternalVar> externalVars = null;
    Map<String, ExternalFunc> externalFuncs = null;
    List<ExternalConstruction> standardConstructs = null;

    public CdmPluginTest() {
        immediateType = new ExternalType("immediate");
        registerType = new ExternalType("register");
        voidType = new ExternalType("void");
        externalVars = initExternalVars();
        externalFuncs = initExternalFuncs();
        standardConstructs = initStandardConstructs();
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
        TestCdmParser parser = new TestCdmParser(tokens);

        return parser.program();
    }

    @Override
    public Tag[] getTagsOfNode(ParseTree tree) {
        if (tree instanceof TestCdmParser.InstructionWithArgContext) {
            return new Tag[]{Tag.Func};
        }

        if (tree instanceof TestCdmParser.ArgumentContext) {
            return new Tag[]{Tag.Var};
        }

        return new Tag[0];
    }

    @Override
    public String getNameOfNode(ParseTree node) {
        return "";
    }

    @Override
    public List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement) {
        return List.of();
    }

    @Override
    public Position getBounds(ParseTree node) {
        var ctx = (ParserRuleContext)node;
        return new Position(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine(), ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine());
    }

    @Override
    public Position getNamePositionOfModule(ParseTree node) {
        return new Position(0, 0, 0, 3); //костыль под add!!
    }

    @Override
    public String getType(ParseTree tree) {
        return "void";
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        return null;
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        return List.of();
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func) {
        return List.of(func.getChild(1).getChild(0), func.getChild(1).getChild(2));
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        return List.of();
    }

    @Override
    public List<ParseTree> getBodeOfModule(ParseTree module) {
        if (module instanceof TestCdmParser.ProgramContext prog) {
            return List.of(module.getChild(0));
        }

        if (module instanceof TestCdmParser.InstructionWithArgContext) {
            return List.of();
        }


        return List.of();
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
        return List.of(registerType, immediateType, voidType);
    }

    @Override
    public List<ExternalConstruction> getStandartConstructionsLike() {
        return standardConstructs;
    }

    @Override
    public Position getPositionOfModuleBody(ParseTree tree) {
        return null;
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree) {
        return new Position(0, 0, 0, 4);
    }

    private List<ExternalVar> initExternalVars() {
        List<ExternalVar> vars = new ArrayList<>();

        vars.add(new ExternalVar(registerType, "r0"));
        vars.add(new ExternalVar(registerType, "r1"));
        vars.add(new ExternalVar(registerType, "r2"));
        vars.add(new ExternalVar(registerType, "r3"));

        return vars;
    }

    private Map<String, ExternalFunc> initExternalFuncs() {
        Map<String, ExternalFunc> funcs = new HashMap<>();

        funcs.put("ldi", new ExternalFunc(voidType, "ldi", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("ld", new ExternalFunc(voidType, "ld", Arrays.asList(
                new ExternalVar(registerType, "rm"),
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("st", new ExternalFunc(voidType, "st", Arrays.asList(
                new ExternalVar(registerType, "rm"),
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("push", new ExternalFunc(voidType, "push", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("pop", new ExternalFunc(voidType, "pop", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("move", new ExternalFunc(voidType, "move", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("ldsp", new ExternalFunc(voidType, "ldsp", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("stsp", new ExternalFunc(voidType, "stsp", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("add", new ExternalFunc(voidType, "add", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("addc", new ExternalFunc(voidType, "addc", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("sub", new ExternalFunc(voidType, "sub", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("cmp", new ExternalFunc(voidType, "cmp", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("neg", new ExternalFunc(voidType, "neg", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("inc", new ExternalFunc(voidType, "inc", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("dec", new ExternalFunc(voidType, "dec", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("and", new ExternalFunc(voidType, "and", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("or", new ExternalFunc(voidType, "or", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("xor", new ExternalFunc(voidType, "xor", Arrays.asList(
                new ExternalVar(registerType, "rn"),
                new ExternalVar(registerType, "rm")
        )));

        funcs.put("not", new ExternalFunc(voidType, "not", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("shr", new ExternalFunc(voidType, "shr", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("shra", new ExternalFunc(voidType, "shra", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("shla", new ExternalFunc(voidType, "shla", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("rol", new ExternalFunc(voidType, "rol", List.of(
                new ExternalVar(registerType, "rn")
        )));

        funcs.put("br", new ExternalFunc(voidType, "br", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("bz", new ExternalFunc(voidType, "bz", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("beq", new ExternalFunc(voidType, "beq", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("bnz", new ExternalFunc(voidType, "bnz", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("bne", new ExternalFunc(voidType, "bne", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("blt", new ExternalFunc(voidType, "blt", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("ble", new ExternalFunc(voidType, "ble", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("bgt", new ExternalFunc(voidType, "bgt", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("bge", new ExternalFunc(voidType, "bge", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("jsr", new ExternalFunc(voidType, "jsr", List.of(
                new ExternalVar(immediateType, "const")
        )));

        funcs.put("rts", new ExternalFunc(voidType, "rts", Collections.emptyList()));
        funcs.put("osi", new ExternalFunc(voidType, "osi", Collections.emptyList()));
        funcs.put("osix", new ExternalFunc(voidType, "osix", Collections.emptyList()));
        funcs.put("rti", new ExternalFunc(voidType, "rti", Collections.emptyList()));
        funcs.put("crc", new ExternalFunc(voidType, "crc", Collections.emptyList()));
        funcs.put("halt", new ExternalFunc(voidType, "halt", Collections.emptyList()));
        funcs.put("wait", new ExternalFunc(voidType, "wait", Collections.emptyList()));

        return funcs;
    }

    private List<ExternalConstruction> initStandardConstructs() {
        List<ExternalConstruction> ret = new ArrayList<>();

        ret.add(new ExternalConstruction(List.of("if", "is", "fi"), "if without else"));
        ret.add(new ExternalConstruction(List.of("if", "is", "fi", "else"), "if with else"));
        ret.add(new ExternalConstruction(List.of("while", "stays", "wend"), "while"));
        ret.add(new ExternalConstruction(List.of("macro", "/", "mend"), "macro"));
        ret.add(new ExternalConstruction(List.of("do", "until"), "do-while"));
        ret.add(new ExternalConstruction(List.of("asect"), "asect"));
        ret.add(new ExternalConstruction(List.of("rsect"), "rsect"));
        ret.add(new ExternalConstruction(List.of("end"), "end"));


        return ret;
    }
}

