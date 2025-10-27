package org.ide.antlrTest;

import Simple.SimpleGremmarBaseVisitor;
import Simple.SimpleGremmarLexer;
import Simple.SimpleGremmarParser;
import Simple.SimpleGremmarVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

public class MainAntlr {
    public ParseTree tree;

    public MainAntlr() {
        String input = "3 + 4 * 5\n" +
                "x = 10\n" +
                "y = x + 2\n";

        CharStream stream = CharStreams.fromString(input);
        SimpleGremmarLexer lexer = new SimpleGremmarLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleGremmarParser parser = new SimpleGremmarParser(tokens);
        tree = parser.prog();

        System.out.println(tree.toStringTree(parser));
    }
}
