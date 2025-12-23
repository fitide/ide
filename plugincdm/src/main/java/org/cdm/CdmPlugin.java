package org.cdm;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.cdm.antlr.*;
import org.ide.PluginController.PluginInterface.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CdmPlugin implements Plugin {
    ExternalType registerType = null;
    ExternalType immediateType = null;
    ExternalType voidType = null;
    List<ExternalVar> externalVars = null;
    Map<String, ExternalFunc> externalFuncs = null;
    List<ExternalConstruction> standardConstructs = null;

    public CdmPlugin() {
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
            fileContent = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e); //TODO : сделать нормальную обработку ошибок
        }

        CharStream charStream = CharStreams.fromString(fileContent);
        CdmLexer lexer = new CdmLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CdmParser parser = new CdmParser(tokens);

        return parser.program();
    }

    //в самом деле, у него есть стандартные файлы,
    //но для них вроде как нет чего-либо типа #include
    @Override
    public List<ExternalFile> getStandartFiles() {
        return List.of();
    }

    @Override
    public List<ExternalFunc> getStandartFuncs() {
        return new ArrayList<>(externalFuncs.values());
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
        return List.of(registerType, immediateType, voidType);
    }

    @Override
    public List<ExternalConstruction> getStandartConstructionsLike() {
        return standardConstructs;
    }

    @Override
    public Tag[] getTagsOfNode(ParseTree tree) {
        if (tree instanceof CdmParser.MacroSectionContext section) {
            return new Tag[]{Tag.Func, Tag.Definition};
        } else if (tree instanceof CdmParser.MacroContext) {
            return new Tag[]{Tag.Func, Tag.Definition};
        } else if (tree instanceof CdmParser.AbsoluteSectionContext section) {
            return new Tag[]{Tag.Construction};
        } else if (tree instanceof CdmParser.RelocatableSectionContext section) {
            return new Tag[]{Tag.Construction};
        } else if (tree instanceof CdmParser.InstructionWithArgContext line) {
            return new Tag[]{Tag.Func};
        } else if (tree instanceof CdmParser.LabelContext line) {
            return new Tag[]{Tag.Var, Tag.Definition};
        } else if (tree instanceof CdmParser.While_loopContext loop) {
            return new Tag[]{Tag.Construction};
        } else if (tree instanceof CdmParser.Until_loopContext loop) {
            return new Tag[]{Tag.Construction};
        } else if (tree instanceof CdmParser.ConditionalContext cond) {
            return new Tag[]{Tag.Construction};
        } else if (tree instanceof CdmParser.ArgumentContext argument) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof CdmParser.Branch_mnemonicContext mnemonic) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof CdmParser.NumberContext number) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof CdmParser.NameContext name) {
            return new Tag[]{Tag.Var};
        }

        //unreachable
        return new Tag[0];
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree) {
        Position pos = new Position();
        if (tree instanceof CdmParser.InstructionWithArgContext line) {
            if (line.arguments() != null) {
                var start = line.arguments().getStart();
                var stop = line.arguments().getStop();
                pos.rowS = start.getLine() - 1;
                pos.colS = start.getCharPositionInLine();
                pos.rowE = stop.getLine() - 1;
                pos.colE = stop.getCharPositionInLine() + stop.getText().length();
                return pos;
            }
        } else if (tree instanceof CdmParser.MacroContext macro) {

        } else if (tree instanceof CdmParser.While_loopContext loop) {
            var arg = loop.branch_mnemonic().getStart();
            pos.rowS = arg.getLine() - 1;
            pos.colS = arg.getCharPositionInLine();
            pos.rowE = arg.getLine() - 1;
            pos.colE = arg.getCharPositionInLine() + arg.getText().length();
            return pos;
        } else if (tree instanceof CdmParser.Until_loopContext loop) {
            var arg = loop.branch_mnemonic().getStart();
            pos.rowS = arg.getLine() - 1;
            pos.colS = arg.getCharPositionInLine();
            pos.rowE = arg.getLine() - 1;
            pos.colE = arg.getCharPositionInLine() + arg.getText().length();
            return pos;
        } else if (tree instanceof CdmParser.ConditionalContext cond) {
            var arg = cond.conditions().condition().branch_mnemonic().getStart();
            pos.rowS = arg.getLine() - 1;
            pos.colS = arg.getCharPositionInLine();
            pos.rowE = arg.getLine() - 1;
            pos.colE = arg.getCharPositionInLine() + arg.getText().length();
            return pos;
        } else if (tree instanceof CdmParser.AbsoluteSectionContext asection) {
            var arg = asection.asect_header().number().getStart();
            pos.rowS = arg.getLine() - 1;
            pos.colS = arg.getCharPositionInLine();
            pos.rowE = arg.getLine() - 1;
            pos.colE = arg.getCharPositionInLine() + arg.getText().length();
            return pos;
        } else if (tree instanceof CdmParser.RelocatableSectionContext rsection) {
            var arg = rsection.rsect_header().name().getStart();
            pos.rowS = arg.getLine() - 1;
            pos.colS = arg.getCharPositionInLine();
            pos.rowE = arg.getLine() - 1;
            pos.colE = arg.getCharPositionInLine() + arg.getText().length();
            return pos;
        }

        return null;
    }

    @Override
    public Position getPositionOfModuleBody(ParseTree tree) {
        Position pos = new Position();
        if (tree instanceof CdmParser.While_loopContext loop) {
            pos.rowS = loop.code_block().get(1).getStart().getLine() - 1;
            pos.colS = loop.code_block().get(1).getStart().getCharPositionInLine();
            pos.rowE = loop.code_block().get(1).getStop().getLine() - 1;
            pos.colE = loop.code_block().get(1).getStop().getCharPositionInLine()
                    + loop.code_block().get(1).getStop().getText().length();
            return pos;
        } else if (tree instanceof CdmParser.ConditionalContext cond) {
            pos.rowS = cond.code_block().getStart().getLine() - 1;
            pos.colS = cond.code_block().getStart().getCharPositionInLine();
            pos.rowE = cond.code_block().getStop().getLine() - 1;
            pos.colE = cond.code_block().getStop().getCharPositionInLine()
                    + cond.code_block().getStop().getText().length();
            return pos;
        } else if (tree instanceof CdmParser.Until_loopContext loop) {
            pos.rowS = loop.code_block().getStart().getLine() - 1;
            pos.colS = loop.code_block().getStart().getCharPositionInLine();
            pos.rowE = loop.code_block().getStop().getLine() - 1;
            pos.colE = loop.code_block().getStop().getCharPositionInLine()
                    + loop.code_block().getStop().getText().length();
            return pos;
        } else if (tree instanceof CdmParser.AbsoluteSectionContext section) {
            pos.rowS = section.code_block().getStart().getLine() - 1;
            pos.colS = section.code_block().getStart().getCharPositionInLine();
            pos.rowE = section.code_block().getStop().getLine() - 1;
            pos.colE = section.code_block().getStop().getCharPositionInLine()
                    + section.code_block().getStop().getText().length();
            return pos;
        } else if (tree instanceof CdmParser.RelocatableSectionContext section) {
            pos.rowS = section.code_block().getStart().getLine() - 1;
            pos.colS = section.code_block().getStart().getCharPositionInLine();
            pos.rowE = section.code_block().getStop().getLine() - 1;
            pos.colE = section.code_block().getStop().getCharPositionInLine()
                    + section.code_block().getStop().getText().length();
            return pos;
        }

        return null;
    }

    @Override
    public List<ParseTree> getFuncsOfClass(ParseTree classNode) {
        return List.of();
    }

    @Override
    public List<ParseTree> getChildsOfNode(ParseTree module) {
        var res = new ArrayList<ParseTree>();
        if (module instanceof CdmParser.ProgramContext program) {
            res.addAll(program.section());
        } else if (module instanceof CdmParser.MacroSectionContext section) {
            res.addAll(getChildsOfNode(section.macro().code_block()));
        } else if (module instanceof CdmParser.AbsoluteSectionContext section) {
            if (section.code_block() != null) {
                for (int j = 0; j < section.code_block().getChildCount(); j++) {
                    res.addAll(getChildsOfNode(section.code_block().getChild(j)));
                }
            }
        } else if (module instanceof CdmParser.RelocatableSectionContext section) {
            if (section.code_block() != null) {
                for (int j = 0; j < section.code_block().getChildCount(); j++) {
                    res.addAll(getChildsOfNode(section.getChild(j)));
                }
            }
        } else if (module instanceof CdmParser.Code_blockContext code_block) {
            for (var child : code_block.children) {
                if (child instanceof CdmParser.LineContext line) {
                    res.addAll(getChildsOfNode(line));
                    continue;
                }

                res.add(child);
            }
        } else if (module instanceof CdmParser.StandaloneLabelsContext line) {
            res.add(line.labels_declaration().label());
        } else if (module instanceof CdmParser.InstructionLineContext line) {
            if (line.labels_declaration() != null) {
                res.add(line.labels_declaration().label());
            }

            res.add(line.instructionWithArg());
        } else if (module instanceof CdmParser.While_loopContext loop) {
            for (var child : loop.code_block()) {
                for (var line : child.children) {
                    if (line instanceof CdmParser.InstructionLineContext l) {
                        res.add(l.instructionWithArg());
                    } else {
                        res.add(line);
                    }
                }
            }
        } else if (module instanceof CdmParser.Until_loopContext loop) {
            for (var line : loop.code_block().children) {
                if (line instanceof CdmParser.InstructionLineContext l) {
                    res.add(l.instructionWithArg());
                } else {
                    res.add(line);
                }
            }
        } else if (module instanceof CdmParser.MacroContext macro) {
            res.addAll(getChildsOfNode(macro.code_block()));
        } else if (module instanceof CdmParser.ConditionalContext cond) {
            var conditions = cond.conditions();
            if (conditions.connective_condition() != null) {
                for (var connect_cond : conditions.connective_condition()) {
                    for (var line : connect_cond.condition().code_block().children) {
                        if (line instanceof CdmParser.InstructionLineContext l) {
                            res.add(l.instructionWithArg());
                        } else {
                            res.add(line);
                        }
                    }
                }
            };

            for (var line : conditions.condition().code_block().children) {
                if (line instanceof CdmParser.InstructionLineContext l) {
                    res.add(l.instructionWithArg());
                } else {
                    res.add(line);
                }
            }

            for (var line : cond.code_block().children) {
                if (line instanceof CdmParser.InstructionLineContext l) {
                    res.add(l.instructionWithArg());
                } else {
                    res.add(line);
                }
            }

            if (cond.else_clause() != null) {
                for (var line : cond.else_clause().code_block().children) {
                    if (line instanceof CdmParser.InstructionLineContext l) {
                        res.add(l.instructionWithArg());
                    } else {
                        res.add(line);
                    }
                }
            }
        }

        return res;
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        if (constr instanceof CdmParser.While_loopContext loop) {
            return List.of(loop.branch_mnemonic());
        } else if (constr instanceof CdmParser.Until_loopContext loop) {
            return List.of(loop.branch_mnemonic());
        } else if (constr instanceof CdmParser.ConditionalContext cond) {
            return List.of(cond.conditions().condition().branch_mnemonic());
        } else if (constr instanceof CdmParser.AbsoluteSectionContext section) {
            return List.of(section.asect_header().number());
        } else if (constr instanceof CdmParser.RelocatableSectionContext section) {
            return List.of(section.rsect_header().name());
        }

        return List.of();
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func) {
        if (func instanceof CdmParser.InstructionWithArgContext instr) {
            if (instr.arguments() != null) {
                return new ArrayList<ParseTree>(instr.arguments().argument());
            }
        }

        return List.of();
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        if (node instanceof CdmParser.While_loopContext loop) {
            return List.of(loop.While(), loop.Stays(), loop.Wend());
        } else if (node instanceof CdmParser.Until_loopContext loop) {
            return List.of(loop.Do(), loop.Until());
        } else if (node instanceof CdmParser.ConditionalContext cond) {
            if (cond.else_clause() != null) {
                return List.of(cond.If(), cond.conditions().condition().Is(),
                        cond.else_clause().Else(), cond.Fi());
            }

            return List.of(cond.If(), cond.conditions().condition().Is(), cond.Fi());
        } else if (node instanceof CdmParser.RelocatableSectionContext section) {
            return List.of(section.rsect_header().Rsect());
        } else if (node instanceof CdmParser.AbsoluteSectionContext section) {
            return List.of(section.asect_header().Asect());
        }

        return List.of();
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        return null;
    }

    @Override
    public String getType(ParseTree tree) {
        if (tree instanceof CdmParser.InstructionWithArgContext) {
            return "void";
        } else if (tree instanceof CdmParser.LabelContext) {
            return "integer";
        } else if (tree instanceof CdmParser.RegisterContext) {
            return "register";
        }

        return null;
    }

    @Override
    public Position getNamePositionOfModule(ParseTree node) {
        Position pos = new Position();
        if (node instanceof CdmParser.InstructionWithArgContext instr) {
            var name = instr.WORD();
            pos.rowS = name.getSymbol().getLine() - 1;
            pos.colS = name.getSymbol().getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + name.getText().length();
            return pos;
        } else if (node instanceof CdmParser.ArgumentContext arg) {
            pos.rowS = arg.getStart().getLine() - 1;
            pos.colS = arg.getStart().getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + arg.getText().length();
            return pos;
        } else if (node instanceof CdmParser.Branch_mnemonicContext arg) {
            pos.rowS = arg.getStart().getLine() - 1;
            pos.colS = arg.getStart().getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + arg.getText().length();
            return pos;
        } else if (node instanceof CdmParser.AbsoluteSectionContext section) {
            var sym = section.asect_header().Asect().getSymbol();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.RelocatableSectionContext section) {
            var sym = section.rsect_header().Rsect().getSymbol();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.NumberContext number) {
            var sym = number.getStart();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.NameContext name) {
            var sym = name.getStart();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.LabelContext label) {
            var sym = label.name().getStart();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.MacroSectionContext macro) {
            var sym = macro.macro().macro_header().WORD().getSymbol();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof CdmParser.MacroContext macro) {
            var sym = macro.macro_header().WORD().getSymbol();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        } else if (node instanceof TerminalNode tnode) {
            var sym = tnode.getSymbol();
            pos.rowS = sym.getLine() - 1;
            pos.colS = sym.getCharPositionInLine();
            pos.rowE = pos.rowS;
            pos.colE = pos.colS + sym.getText().length();
            return pos;
        }

        return null;
    }

    @Override
    public Position getBounds(ParseTree node) {
        if (node instanceof ParserRuleContext ctx) {
            var start = ctx.getStart();
            var stop = ctx.getStop();
            if (start == null || stop == null) {
                return new Position(0, 0, 0, 0);
            }

            return new Position(ctx.getStart().getLine() - 1,
                    ctx.getStart().getCharPositionInLine(),
                    ctx.getStop().getLine() - 1,
                    ctx.getStop().getCharPositionInLine() + ctx.getStop().getText().length());
        } else if (node instanceof TerminalNode tnode) {
            return new Position(tnode.getSymbol().getLine() - 1,
                                tnode.getSymbol().getCharPositionInLine(),
                                tnode.getSymbol().getLine() - 1,
                    tnode.getSymbol().getCharPositionInLine() + tnode.getSymbol().getText().length());
        }

        return null;
    }

    @Override
    public List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement) {
        return List.of();
    }

    @Override
    public String getNameOfNode(ParseTree node) {
        if (node instanceof CdmParser.InstructionWithArgContext instr) {
            return instr.WORD().getText();
        } else if (node instanceof CdmParser.Branch_mnemonicContext branch) {
            return branch.getText();
        } else if (node instanceof TerminalNode tnode) {
            return tnode.getSymbol().getText();
        } else if (node instanceof CdmParser.MacroSectionContext macro) {
            return macro.macro().macro_header().WORD().getText();
        } else if (node instanceof CdmParser.MacroContext macro) {
            return macro.macro_header().WORD().getText();
        } else if (node instanceof CdmParser.LabelContext label) {
            return label.getText();
        } else if (node instanceof CdmParser.ArgumentContext arg) {
            return arg.getText();
        }

        return "";
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

        funcs.put("tst", new ExternalFunc(voidType, "tst", List.of(
                new ExternalVar(registerType, "const")
        )));

        funcs.put("rts", new ExternalFunc(voidType, "rts", Collections.emptyList()));
        funcs.put("osi", new ExternalFunc(voidType, "osi", Collections.emptyList()));
        funcs.put("osix", new ExternalFunc(voidType, "osix", Collections.emptyList()));
        funcs.put("rti", new ExternalFunc(voidType, "rti", Collections.emptyList()));
        funcs.put("crc", new ExternalFunc(voidType, "crc", Collections.emptyList()));
        funcs.put("halt", new ExternalFunc(voidType, "halt", Collections.emptyList()));
        funcs.put("wait", new ExternalFunc(voidType, "wait", Collections.emptyList()));
        funcs.put("dc", new ExternalFunc(voidType, "dc", Collections.emptyList()));
        funcs.put("ds", new ExternalFunc(voidType, "ds", Collections.emptyList()));

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