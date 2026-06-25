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
        if (tree instanceof SMLParser.FunDecContext dec) {
            if (dec.funbind() != null) return new Tag[]{Tag.Func, Tag.Definition};
        } else if (tree instanceof SMLParser.ValDecContext dec) {
            if (dec.valbind() != null) return new Tag[]{Tag.Expression};
        } else if (tree instanceof SMLParser.FunbindContext) {
            //return new Tag[]{Tag.Func, Tag.Definition};
        } else if (tree instanceof SMLParser.ValbindContext) {
            return new Tag[]{Tag.Var, Tag.Definition};
        } else if (tree instanceof SMLParser.PatContext) {
            return new Tag[]{Tag.Var, Tag.Definition};
        } else if (tree instanceof SMLParser.FnExpContext) {
            return new Tag[]{Tag.Func};
        } else if (tree instanceof SMLParser.ApplicationExpContext ctx) {
            return new Tag[]{Tag.Func, Tag.Usage};
        } else if (tree instanceof SMLParser.TupleExpContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IdExpContext id) {
            if (isApplicationHead(id)) return new Tag[]{Tag.Func, Tag.Usage};
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.LongIdExpContext id) {
            if (isApplicationHead(id)) return new Tag[]{Tag.Func, Tag.Usage};
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IntExpContext) {
            return new Tag[]{Tag.Constant};
        } else if (tree instanceof SMLParser.SymbolicExpContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IdArgContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.LongIdArgContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.IntArgContext) {
            return new Tag[]{Tag.Constant};
        } else if (tree instanceof SMLParser.SymbolicArgContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.TupleArgContext) {
            return new Tag[]{Tag.Var};
        } else if (tree instanceof SMLParser.ValContext) {
            return new Tag[]{Tag.KeyWord};
        } else if (tree instanceof SMLParser.FunContext) {
            return new Tag[]{Tag.KeyWord};
        } else if (tree instanceof SMLParser.StructDecContext) {
            return new Tag[]{Tag.Class, Tag.Definition};
        } else if (tree instanceof SMLParser.AddExpContext) {
            return new Tag[]{Tag.Func, Tag.Usage};
        } else if (tree instanceof SMLParser.MulExpContext) {
            return new Tag[]{Tag.Func, Tag.Usage};
        } else if (tree instanceof SMLParser.InfixIdContext) {
            return new Tag[] {Tag.Func, Tag.Usage};
        } else if (tree instanceof SMLParser.InfixargContext) {
            return new Tag[] {Tag.Var, Tag.Usage};
        } else if (tree instanceof SMLParser.ArgContext) {
            return new Tag[]{Tag.Expression};
        }

        return new Tag[0];
    }


    @Override
    public String getNameOfNode(ParseTree node, Object object) {
        if (node instanceof SMLParser.ValDecContext dec) {
            if (dec.valbind() != null) {
                return patName(dec.valbind().pat());
            }
        } else if (node instanceof SMLParser.FunDecContext dec) {
            if (dec.funbind() != null) {
                return dec.funbind().ID().getText();
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

        if (node instanceof SMLParser.LongIdExpContext lid) {
            return lastIdOf(lid.longid()).getText();
        }

        if (node instanceof SMLParser.SymbolicExpContext sym) {
            return sym.SYMBOLIC_ID().getText();
        }

        if (node instanceof SMLParser.IntExpContext num) {
            return num.INT().getText();
        }

        if (node instanceof SMLParser.ApplicationExpContext app) {
            if (app.arg().size() == 2 && object instanceof HashSet<?> table) {
                if (table.contains(app.arg(0).getText())) {
                    return app.arg(0).getText();
                }
            }
            return app.exp().getText();
        }

        if (node instanceof SMLParser.IdArgContext id) {
            return id.ID().getText();
        }

        if (node instanceof SMLParser.LongIdArgContext lid) {
            return lastIdOf(lid.longid()).getText();
        }

        if (node instanceof SMLParser.SymbolicArgContext sym) {
            return sym.SYMBOLIC_ID().getText();
        }

        if (node instanceof SMLParser.IntArgContext num) {
            return num.INT().getText();
        }

        if (node instanceof SMLParser.TupleArgContext te) {
            return te.getText();
        }

        if (node instanceof SMLParser.StructDecContext struct) {
            return struct.structbind().ID().getText();
        }

        if (node instanceof TerminalNode tn) {
            return tn.getSymbol().getText();
        }

        if (node instanceof SMLParser.MulExpContext mul) {
            return mul.mulOp().getText();
        }

        if (node instanceof SMLParser.AddExpContext add) {
            return add.addOp().getText();
        }

        if (node instanceof SMLParser.InfixIdContext infix) {
            return "infix";
        }

        if (node instanceof SMLParser.InfixargContext infixarg) {
            return infixarg.ID().getText();
        }

        if (node instanceof SMLParser.ArgContext arg) {
            return arg.getText();
        }



        return "";
    }

    private String patName(SMLParser.PatContext pat) {
        if (pat == null) return "";
        if (pat.ID() != null) return pat.ID().getText();

        return pat.getText();
    }

    private TerminalNode lastIdOf(SMLParser.LongidContext longid) {
        List<TerminalNode> ids = longid.ID();
        return ids.get(ids.size() - 1);
    }

    private boolean isApplicationHead(ParserRuleContext node) {
        return node.getParent() instanceof SMLParser.ApplicationExpContext app && app.arg(0) == node;
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
    public Position getNamePositionOfModule(ParseTree node, Object state) {
        if (node instanceof SMLParser.FunDecContext dec) {
            if (dec.funbind() != null) {
                Token sym = dec.funbind().ID().getSymbol();
                return tokenPosition(sym);
            }
        }

        if (node instanceof SMLParser.ValDecContext dec) {
            if (dec.valbind() != null) {
                return patNamePosition(dec.valbind().pat());
            }
        }
        if (node instanceof SMLParser.StructDecContext struct) {
            return tokenPosition(struct.structbind().ID().getSymbol());
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
        if (node instanceof SMLParser.LongIdExpContext lid) {
            return tokenPosition(lastIdOf(lid.longid()).getSymbol());
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

        if (node instanceof SMLParser.ApplicationExpContext exp) {
            if (exp.arg().size() == 2 && state instanceof HashSet<?> table) {
                if (table.contains(exp.arg(0).getText())) {
                    return rulePosition(exp.arg(0));
                }
            }
            return tokenPosition(exp.getStart());
        }
        if (node instanceof SMLParser.ValContext val) {
            return tokenPosition(val.start);
        }

        if (node instanceof SMLParser.FunContext fun) {
            return tokenPosition(fun.start);
        }

        if (node instanceof SMLParser.AddExpContext add) {
            return rulePosition(add.addOp());
        }

        if (node instanceof SMLParser.MulExpContext mul) {
            return rulePosition(mul.mulOp());
        }

        if (node instanceof SMLParser.StructureContext struct) {
            return rulePosition(struct);
        }

        if (node instanceof SMLParser.EndContext end) {
            return rulePosition(end);
        }

        if (node instanceof SMLParser.InfixIdContext infix) {
            return tokenPosition(infix.INFIX().getSymbol());
        }

        if (node instanceof SMLParser.InfixargContext infixarg) {
            return tokenPosition(infixarg.getStart());
        }

        if (node instanceof SMLParser.ArgContext arg) {
            return rulePosition(arg);
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
        if (tree instanceof SMLParser.ValDecContext dec) {
            if (dec.valbind() != null) return rulePosition(dec.valbind().exp());
        } else if (tree instanceof SMLParser.FunDecContext dec) {
            if (dec.funbind() != null) return rulePosition(dec.funbind());
        }
        if (tree instanceof SMLParser.TupleExpContext te) {
            return rulePosition(te.tuple());
        }
        if (tree instanceof SMLParser.StructDecContext struct) {
            return rulePosition(struct.structbind());
        }

        return null;
    }

    @Override
    public Position getPositionOfArgsOfFunc(ParseTree tree, Object object) {
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
            if (app.arg().size() == 2 && object instanceof HashSet<?> table) {
                if (table.contains(app.arg(0).getText())) {
                    Token start = app.getStart();
                    Token stop  = app.arg().getLast().getStop();
                    return new Position(
                            start.getLine() - 1, start.getCharPositionInLine(),
                            stop.getLine()  - 1, stop.getCharPositionInLine() + stop.getText().length()
                    );
                }
            }
            List<SMLParser.ArgContext> args = app.arg();
            if (args.isEmpty()) return null;
            Token start = args.get(0).getStart();
            Token stop  = args.get(args.size() - 1).getStop();
            return new Position(
                    start.getLine() - 1, start.getCharPositionInLine(),
                    stop.getLine()  - 1, stop.getCharPositionInLine() + stop.getText().length()
            );
        }
        if (tree instanceof SMLParser.TupleExpContext te) {
            return rulePosition(te.tuple());
        }

        if (tree instanceof SMLParser.AddExpContext add) {
            return rulePosition(add);
        }

        if (tree instanceof SMLParser.MulExpContext mul) {
            return rulePosition(mul);
        }

        if (tree instanceof SMLParser.InfixIdContext infix) {
            return rulePosition(infix.infixarg());
        }

        return null;
    }

    @Override
    public String getType(ParseTree tree) {
        if (tree instanceof SMLParser.IntExpContext)      return "int";
        else if (tree instanceof SMLParser.SymbolicExpContext) return "operator";
        else if (tree instanceof SMLParser.DecContext dec) {
            if (dec instanceof SMLParser.ValDecContext ctx) {
                if (ctx.valbind().typ() != null) {
                    return ctx.valbind().typ().getText();
                }
            } else if (dec instanceof SMLParser.FunDecContext ctx) {
                if (ctx.funbind().typ() != null) {
                    return ctx.funbind().typ().getText();
                }
            }
        }
        return null;
    }

    @Override
    public Position getTypePositionOfModule(ParseTree node) {
        if (node instanceof SMLParser.DecContext dec) {
            if (node instanceof SMLParser.ValDecContext ctx) {
                if (ctx.valbind().typ() != null) {
                    return rulePosition(ctx.valbind().typ());
                }
            } else if (node instanceof SMLParser.FunDecContext ctx) {
                if (ctx.funbind().typ() != null) {
                    return rulePosition(ctx.funbind().typ());
                }
            }
        }


        return null;
    }

    @Override
    public List<ParseTree> getKeyWordsOfModule(ParseTree node) {
        if (node instanceof SMLParser.ValDecContext dec) {
            return List.of(dec.getChild(0));
        }
        if (node instanceof SMLParser.FnExpContext fn) {
            return List.of(fn.getChild(0));
        }
        if (node instanceof SMLParser.StructDecContext struct) {
            return List.of(struct.structure(), struct.structbind().struct_(), struct.structbind().end());
        }
        return List.of();
    }

    @Override
    public List<ParseTree> getChildsOfNode(ParseTree module, Object state) {
        List<ParseTree> res = new ArrayList<>();

        if (module instanceof SMLParser.ProgContext prog) {
            res.addAll(prog.dec());
        } else if (module instanceof SMLParser.FunDecContext dec) {
            res.addAll(getChildsOfNode(dec.funbind(), state));
            res.add(dec.fun());
        } else if (module instanceof SMLParser.ValDecContext dec) {
            res.add(dec.val());
            res.add(dec.valbind().pat());
            res.add(dec.valbind().exp());
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
            if (app.arg().size() == 2 && state instanceof HashSet<?> table) {
                if (table.contains(app.arg(0).getText())) {
                    res.add(app.exp());
                    res.add(app.arg(1));
                }
            } else {
                res.addAll(app.arg());
            }

        } else if (module instanceof SMLParser.TupleArgContext ta) {
            res.addAll(ta.tuple().exp());

        } else if (module instanceof SMLParser.ParensArgContext pa) {
            res.add(pa.exp());

        } else if (module instanceof SMLParser.TupleExpContext te) {
            SMLParser.TupleContext t = te.tuple();
            res.addAll(t.exp());

        } else if (module instanceof SMLParser.TupleContext t) {
            res.addAll(t.exp());

        } else if (module instanceof SMLParser.ParensExpContext par) {
            res.add(par.exp());
        } else if (module instanceof SMLParser.StructDecContext struct) {
            res.addAll(struct.structbind().decs().dec());
        } else if (module instanceof SMLParser.AddExpContext add) {
            res.addAll(add.exp());
        } else if (module instanceof SMLParser.MulExpContext mul) {
            res.addAll(mul.exp());
        }

        return res;
    }

    @Override
    public List<ParseTree> getArgsOfFunc(ParseTree func, Object state) {
        if (func instanceof SMLParser.FunDecContext dec) {
            if (dec.funbind() != null) {
                return new ArrayList<>(dec.funbind().pat());
            }
            return List.of();
        }

        if (func instanceof SMLParser.FnExpContext fn) {
            return new ArrayList<>(fn.match().pat());
        }

        if (func instanceof SMLParser.ApplicationExpContext app) {
            if (app.arg().size() == 2 && state instanceof HashSet<?> table) {
                if (table.contains(app.arg(0).getText())) {
                    return List.of(app.exp(), app.arg(1));
                }
            }
            return new ArrayList<>(app.arg());
        }

        if (func instanceof SMLParser.TupleExpContext te) {
            return new ArrayList<>(te.tuple().exp());
        }

        if (func instanceof SMLParser.AddExpContext add) {
            return new ArrayList<>(add.exp());
        }

        if (func instanceof SMLParser.MulExpContext mul) {
            return new ArrayList<>(mul.exp());
        }

        if (func instanceof SMLParser.InfixIdContext infix) {
            if (state instanceof HashSet<?> table) {
                @SuppressWarnings("unchecked")
                HashSet<String> set = (HashSet<String>) table;
                set.add(infix.infixarg().getText());
            }
            return List.of(infix.infixarg());
        }

        return List.of();
    }

    @Override
    public List<ParseTree> getConstructionArgs(ParseTree constr) {
        if (constr instanceof SMLParser.ApplicationExpContext app) {
            List<ParseTree> all = new ArrayList<>();
            all.add(app.arg(0));
            all.addAll(app.arg());
            return all;
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
        SMLParser.DecsContext decs = null;

        if (classNode instanceof SMLParser.StructDecContext struct) {
            decs = struct.structbind().decs();
        } else if (classNode instanceof SMLParser.StructbindContext sb) {
            decs = sb.decs();
        }

        if (decs == null) {
            return List.of();
        }

        List<ParseTree> result = new ArrayList<>();
        for (SMLParser.DecContext dec : decs.dec()) {
            if (dec instanceof SMLParser.FunDecContext || dec instanceof SMLParser.ValDecContext) {
                result.add(dec);
            }
        }
        return result;
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

    @Override
    public Object newStateObject() {
        return new HashSet<String>();
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

        funcs.put("infix", new ExternalFunc(stringType, "infix", List.of(new ExternalVar(stringType, "ID"))));
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