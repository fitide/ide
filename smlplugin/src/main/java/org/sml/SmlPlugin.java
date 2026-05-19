package org.sml;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.sml.antlr.*;
import org.ide.PluginController.PluginInterface.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SmlPlugin implements Plugin {
    ExternalType intType    = null;
    ExternalType boolType   = null;
    ExternalType realType   = null;
    ExternalType stringType = null;
    ExternalType unitType   = null;
    ExternalType numberType   = null;
    //ExternalType listType   = null;

    Map<String, ExternalFunc> externalFuncs   = null;
    List<ExternalVar>         externalVars    = null;
    List<ExternalConstruction> standardConstructs = null;

    public SmlPlugin() {
        intType    = new ExternalType("int");
        boolType   = new ExternalType("bool");
        realType   = new ExternalType("real");
        stringType = new ExternalType("string");
        unitType   = new ExternalType("unit");
        numberType   = new ExternalType("number");
        //TODO: в sml списки параметризуются типом - надо или писать 'a list, или хардкодить под каждый тип
        //listType   = new ExternalType("list");

        externalFuncs    = initExternalFuncs();
        externalVars     = initExternalVars();
        standardConstructs = initStandardConstructs();
    }

    @Override
    public String fileExtension() {
        return ".sml";
    }

    @Override
    public ParseTree getFileParseTree(File file) {
        String src;
        try {
            src = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CharStream      chars  = CharStreams.fromString(src);
        SMLLexer        lexer  = new SMLLexer(chars);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SMLParser       parser = new SMLParser(tokens);
        return parser.prog();
    }

    @Override
    public Tag[] getTagsOfNode(ParseTree tree) {
        if (tree instanceof SMLParser.DecContext dec) {
            if (dec.funbind() != null) return new Tag[]{Tag.Func, Tag.Definition};
            if (dec.valbind() != null) return new Tag[]{Tag.Var,  Tag.Definition};
        } else if (tree instanceof SMLParser.FunbindContext) {
            //return new Tag[]{Tag.Func, Tag.Definition};
        } else if (tree instanceof SMLParser.ValbindContext) {
            //return new Tag[]{Tag.Var, Tag.Definition};
        } else if (tree instanceof SMLParser.PatContext) {
            //return new Tag[]{Tag.Var, Tag.Definition};
        } else if (tree instanceof SMLParser.FnExpContext) {
            return new Tag[]{Tag.Func};
        } else if (tree instanceof SMLParser.ApplicationExpContext) {
            return new Tag[]{Tag.Func};
        } else if (tree instanceof SMLParser.TupleExpContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IdExpContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IntExpContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.SymbolicExpContext) {
            return new Tag[]{Tag.Var};
        }
        return new Tag[0];
    }


    @Override
    public String getNameOfNode(ParseTree node) {
        if (node instanceof SMLParser.DecContext dec) {
            if (dec.funbind() != null) {
                return dec.funbind().ID().getText();
            }
            if (dec.valbind() != null) {
                return patName(dec.valbind().pat());
            }
        }

        if (node instanceof SMLParser.FunbindContext fb) {
            return fb.ID().getText();
        }

        if (node instanceof SMLParser.ValbindContext vb) {
            return patName(vb.pat());
        }

        if (node instanceof SMLParser.PatContext pat) {
            return patName(pat);
        }

        if (node instanceof SMLParser.TupleExpContext te) {
            return te.getText();
        }

        if (node instanceof SMLParser.IdExpContext id) {
            return id.ID().getText();
        }

        if (node instanceof SMLParser.SymbolicExpContext sym) {
            return sym.SYMBOLIC_ID().getText();
        }

        if (node instanceof SMLParser.IntExpContext num) {
            return num.INT().getText();
        }

        if (node instanceof TerminalNode tn) {
            return tn.getSymbol().getText();
        }
        return "";
    }

    private String patName(SMLParser.PatContext pat) {
        if (pat == null) return "";
        if (pat.ID() != null) return pat.ID().getText();

        return pat.getText();
    }


    @Override
    public List<Path> getPathsOfSearchingByImportStatement(ParseTree tree, Path pathToFileWithStatement) {
        return List.of();
    }


    @Override
    public Position getBounds(ParseTree node) {
        if (node instanceof ParserRuleContext ctx) {
            Token start = ctx.getStart();
            Token stop  = ctx.getStop();
            if (start == null || stop == null) return new Position(0, 0, 0, 0);
            return new Position(
                    start.getLine() - 1, start.getCharPositionInLine(),
                    stop.getLine()  - 1, stop.getCharPositionInLine() + stop.getText().length()
            );
        }
        if (node instanceof TerminalNode tn) {
            Token sym = tn.getSymbol();
            return new Position(
                    sym.getLine() - 1, sym.getCharPositionInLine(),
                    sym.getLine() - 1, sym.getCharPositionInLine() + sym.getText().length()
            );
        }
        return null;
    }


    @Override
    public Position getNamePositionOfModule(ParseTree node) {
        if (node instanceof SMLParser.DecContext dec) {
            if (dec.funbind() != null) {
                Token sym = dec.funbind().ID().getSymbol();
                return tokenPosition(sym);
            }
            if (dec.valbind() != null) {
                return patNamePosition(dec.valbind().pat());
            }
        }
        if (node instanceof SMLParser.FunbindContext fb) {
            return tokenPosition(fb.ID().getSymbol());
        }

        if (node instanceof SMLParser.ValbindContext vb) {
            return patNamePosition(vb.pat());
        }
        if (node instanceof SMLParser.PatContext pat) {
            return patNamePosition(pat);
        }
        if (node instanceof SMLParser.TupleExpContext te) {
            // Position of the entire tuple expression
            return rulePosition(te.tuple());
        }
        if (node instanceof SMLParser.IdExpContext id) {
            return tokenPosition(id.ID().getSymbol());
        }
        if (node instanceof SMLParser.SymbolicExpContext sym) {
            return tokenPosition(sym.SYMBOLIC_ID().getSymbol());
        }
        if (node instanceof SMLParser.IntExpContext num) {
            return tokenPosition(num.INT().getSymbol());
        }
        if (node instanceof TerminalNode tn) {
            return tokenPosition(tn.getSymbol());
        }
        return null;
    }

    private Position patNamePosition(SMLParser.PatContext pat) {
        if (pat == null) return null;
        if (pat.ID() != null) return tokenPosition(pat.ID().getSymbol());

        return rulePosition(pat);
    }

    @Override
    public Position getPositionOfModuleBody(ParseTree tree) {
        if (tree instanceof SMLParser.FunbindContext fb) {
            return rulePosition(fb.exp());
        }
        if (tree instanceof SMLParser.ValbindContext vb) {
            return rulePosition(vb.exp());
        }
        if (tree instanceof SMLParser.FnExpContext fn) {
            return rulePosition(fn.match());
        }
        if (tree instanceof SMLParser.DecContext dec) {
            if (dec.funbind() != null) return rulePosition(dec.funbind());
            if (dec.valbind() != null) return rulePosition(dec.valbind().exp());
        }
        if (tree instanceof SMLParser.TupleExpContext te) {
            return rulePosition(te.tuple());
        }
        return null;
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree) {
        if (tree instanceof SMLParser.FunbindContext fb) {
            List<SMLParser.PatContext> pats = fb.pat();
            if (pats == null || pats.isEmpty()) return null;
            Token start = pats.get(0).getStart();
            Token stop  = pats.get(pats.size() - 1).getStop();
            return new Position(
                    start.getLine() - 1, start.getCharPositionInLine(),
                    stop.getLine()  - 1, stop.getCharPositionInLine() + stop.getText().length()
            );
        }
        if (tree instanceof SMLParser.FnExpContext fn) {
            SMLParser.MatchContext m = fn.match();
            Token sym = m.pat(0).getStart();
            return new Position(
                    sym.getLine() - 1, sym.getCharPositionInLine(),
                    sym.getLine() - 1, sym.getCharPositionInLine() + sym.getText().length()
            );
        }
        if (tree instanceof SMLParser.ApplicationExpContext app) {
            return rulePosition((ParserRuleContext) app.exp(1));
        }
        if (tree instanceof SMLParser.TupleExpContext te) {
            return rulePosition(te.tuple());
        }
        return null;
    }

    @Override
    public String getType(ParseTree tree) {
        if (tree instanceof SMLParser.IntExpContext)      return "int";
        if (tree instanceof SMLParser.SymbolicExpContext) return "operator";
        return null;
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        return null;
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        if (node instanceof SMLParser.DecContext dec) {
            return List.of(dec.getChild(0));
        }
        if (node instanceof SMLParser.FnExpContext fn) {
            return List.of(fn.getChild(0));
        }
        return List.of();
    }

    @Override
    public List<ParseTree> getChildsOfNode(ParseTree module) {
        List<ParseTree> res = new ArrayList<>();

        if (module instanceof SMLParser.ProgContext prog) {
            res.addAll(prog.dec());
        } else if (module instanceof SMLParser.DecContext dec) {
            if (dec.funbind() != null) {
                res.add(dec.funbind());
            } else if (dec.valbind() != null) {
                res.add(dec.valbind());
            }

        } else if (module instanceof SMLParser.FunbindContext fb) {
            res.addAll(fb.pat());
            res.add(fb.exp());

        } else if (module instanceof SMLParser.ValbindContext vb) {
            res.add(vb.pat());
            res.add(vb.exp());

        } else if (module instanceof SMLParser.FnExpContext fn) {
            for (int i = 0; i < fn.match().pat().size(); i++) {
                res.add(fn.match().pat(i));
                res.add(fn.match().exp(i));
            }

        } else if (module instanceof SMLParser.ApplicationExpContext app) {
            res.add(app.exp(0));
            res.add(app.exp(1));

        } else if (module instanceof SMLParser.TupleExpContext te) {
            SMLParser.TupleContext t = te.tuple();
            res.addAll(t.exp());

        } else if (module instanceof SMLParser.TupleContext t) {
            res.addAll(t.exp());

        } else if (module instanceof SMLParser.ParensExpContext par) {
            res.add(par.exp());
        }

        return res;
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func) {
        if (func instanceof SMLParser.FunbindContext fb) {
            return new ArrayList<>(fb.pat());
        }
        if (func instanceof SMLParser.FnExpContext fn) {
            return new ArrayList<>(fn.match().pat());
        }

        if (func instanceof SMLParser.ApplicationExpContext app) {
            return List.of(app.exp(0), app.exp(1));
        }

        if (func instanceof SMLParser.TupleExpContext te) {
            return new ArrayList<>(te.tuple().exp());
        }
        return List.of();
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        if (constr instanceof SMLParser.ApplicationExpContext app) {
            return List.of(app.exp(0), app.exp(1));
        }
        if (constr instanceof SMLParser.FnExpContext fn) {
            return new ArrayList<>(fn.match().pat());
        }
        if (constr instanceof SMLParser.TupleExpContext te) {
            return new ArrayList<>(te.tuple().exp());
        }
        return List.of();
    }

    @Override
    public List<ParseTree> getFuncsOfClass(ParseTree classNode) {
        return List.of();
    }


    @Override
    public List<ExternalFile> getStandardFiles() {
        return List.of();
    }

    @Override
    public List<ExternalFunc> getStandardFuncs() {
        return new ArrayList<>(externalFuncs.values());
    }

    @Override
    public List<ExternalVar> getStandardVars() {
        return externalVars;
    }

    @Override
    public List<ExternalClass> getStandardClasses() {
        return List.of();
    }

    @Override
    public List<ExternalType> getStandardTypes() {
        //return List.of(intType, boolType, realType, stringType, unitType, listType);
        return List.of(intType, boolType, realType, stringType, unitType);
    }

    @Override
    public List<ExternalConstruction> getStandardConstructionsLike() {
        return standardConstructs;
    }

    private List<ExternalVar> initExternalVars() {
        List<ExternalVar> vars = new ArrayList<>();

        vars.add(new ExternalVar(boolType, "true"));
        vars.add(new ExternalVar(boolType, "false"));

        //TODO: какого типа nil?
        //vars.add(new ExternalVar(listType, "nil"));

        return vars;
    }

    private Map<String, ExternalFunc> initExternalFuncs() {
        Map<String, ExternalFunc> funcs = new HashMap<>();

        funcs.put("+",   new ExternalFunc(numberType, "+",   args(numberType, "x", numberType, "y")));
        funcs.put("-",   new ExternalFunc(numberType, "-",   args(numberType, "x", numberType, "y")));
        funcs.put("*",   new ExternalFunc(numberType, "*",   args(numberType, "x", numberType, "y")));
        funcs.put("div", new ExternalFunc(numberType, "div", args(numberType, "x", numberType, "y")));
        funcs.put("mod", new ExternalFunc(numberType, "mod", args(numberType, "x", numberType, "y")));
        funcs.put("~",   new ExternalFunc(numberType, "~",   List.of(new ExternalVar(numberType, "x"))));
        funcs.put("abs", new ExternalFunc(numberType, "abs", List.of(new ExternalVar(numberType, "x"))));

        funcs.put("=",  new ExternalFunc(boolType, "=",  args(numberType, "x", numberType, "y")));
        funcs.put("<>", new ExternalFunc(boolType, "<>", args(numberType, "x", numberType, "y")));
        funcs.put("<",  new ExternalFunc(boolType, "<",  args(numberType, "x", numberType, "y")));
        funcs.put("<=", new ExternalFunc(boolType, "<=", args(numberType, "x", numberType, "y")));
        funcs.put(">",  new ExternalFunc(boolType, ">",  args(numberType, "x", numberType, "y")));
        funcs.put(">=", new ExternalFunc(boolType, ">=", args(numberType, "x", numberType, "y")));

        funcs.put("not", new ExternalFunc(boolType, "not", List.of(new ExternalVar(boolType, "b"))));
        funcs.put("andalso", new ExternalFunc(boolType, "andalso", args(boolType, "a", boolType, "b")));
        funcs.put("orelse",  new ExternalFunc(boolType, "orelse",  args(boolType, "a", boolType, "b")));

        //TODO: все это работает со списками, надо поддержать после решения проблемы с listType
//        funcs.put("hd",     new ExternalFunc(anyType,  "hd",     List.of(new ExternalVar(listType, "ls"))));
//        funcs.put("tl",     new ExternalFunc(listType, "tl",     List.of(new ExternalVar(listType, "ls"))));
//        funcs.put("null",   new ExternalFunc(boolType, "null",   List.of(new ExternalVar(listType, "ls"))));
//        funcs.put("length", new ExternalFunc(intType,  "length", List.of(new ExternalVar(listType, "ls"))));
//        funcs.put("::",     new ExternalFunc(listType, "::",     args(anyType, "x", listType, "xs")));
//        funcs.put("@",      new ExternalFunc(listType, "@",      args(listType, "xs", listType, "ys")));
//        funcs.put("rev",    new ExternalFunc(listType, "rev",    List.of(new ExternalVar(listType, "ls"))));
//        funcs.put("map",    new ExternalFunc(listType, "map",    args(anyType, "f",  listType, "ls")));
//        funcs.put("filter", new ExternalFunc(listType, "filter", args(anyType, "f",  listType, "ls")));
//        funcs.put("foldl",  new ExternalFunc(anyType,  "foldl",  args(anyType, "f",  anyType, "acc", listType, "ls")));
//        funcs.put("foldr",  new ExternalFunc(anyType,  "foldr",  args(anyType, "f",  anyType, "acc", listType, "ls")));

        funcs.put("print", new ExternalFunc(unitType, "print", List.of(new ExternalVar(stringType, "s"))));

        funcs.put("Int.toString",    new ExternalFunc(stringType, "Int.toString",    List.of(new ExternalVar(intType,    "n"))));
        funcs.put("Real.fromInt",    new ExternalFunc(realType,   "Real.fromInt",    List.of(new ExternalVar(intType,    "n"))));
        funcs.put("String.size",     new ExternalFunc(intType,    "String.size",     List.of(new ExternalVar(stringType, "s"))));
        //funcs.put("String.explode",  new ExternalFunc(listType,   "String.explode",  List.of(new ExternalVar(stringType, "s"))));
        //funcs.put("String.implode",  new ExternalFunc(stringType, "String.implode",  List.of(new ExternalVar(listType,   "cs"))));

        return funcs;
    }

    private List<ExternalConstruction> initStandardConstructs() {
        List<ExternalConstruction> ret = new ArrayList<>();

        ret.add(new ExternalConstruction(List.of("val", "="),      "value binding"));
        ret.add(new ExternalConstruction(List.of("fun", "="),      "function definition"));
        ret.add(new ExternalConstruction(List.of("fn", "=>"),      "lambda (fn expression)"));

        return ret;
    }

    private Position tokenPosition(Token sym) {
        return new Position(
                sym.getLine() - 1, sym.getCharPositionInLine(),
                sym.getLine() - 1, sym.getCharPositionInLine() + sym.getText().length()
        );
    }

    private Position rulePosition(ParserRuleContext ctx) {
        if (ctx == null) return null;
        Token start = ctx.getStart();
        Token stop  = ctx.getStop();
        if (start == null || stop == null) return new Position(0, 0, 0, 0);
        return new Position(
                start.getLine() - 1, start.getCharPositionInLine(),
                stop.getLine()  - 1, stop.getCharPositionInLine() + stop.getText().length()
        );
    }

    private List<ExternalVar> args(Object... pairs) {
        List<ExternalVar> result = new ArrayList<>();
        for (int i = 0; i < pairs.length - 1; i += 2) {
            result.add(new ExternalVar((ExternalType) pairs[i], (String) pairs[i + 1]));
        }
        return result;
    }
}