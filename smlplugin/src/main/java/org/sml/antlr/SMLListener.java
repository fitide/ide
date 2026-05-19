package org.sml.antlr;// Generated from SML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SMLParser}.
 */
public interface SMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SMLParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(SMLParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(SMLParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterDec(SMLParser.DecContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitDec(SMLParser.DecContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#valbind}.
	 * @param ctx the parse tree
	 */
	void enterValbind(SMLParser.ValbindContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#valbind}.
	 * @param ctx the parse tree
	 */
	void exitValbind(SMLParser.ValbindContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#funbind}.
	 * @param ctx the parse tree
	 */
	void enterFunbind(SMLParser.FunbindContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#funbind}.
	 * @param ctx the parse tree
	 */
	void exitFunbind(SMLParser.FunbindContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#pat}.
	 * @param ctx the parse tree
	 */
	void enterPat(SMLParser.PatContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#pat}.
	 * @param ctx the parse tree
	 */
	void exitPat(SMLParser.PatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterIdExp(SMLParser.IdExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitIdExp(SMLParser.IdExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterParensExp(SMLParser.ParensExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitParensExp(SMLParser.ParensExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ApplicationExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterApplicationExp(SMLParser.ApplicationExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ApplicationExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitApplicationExp(SMLParser.ApplicationExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FnExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterFnExp(SMLParser.FnExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FnExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitFnExp(SMLParser.FnExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterIntExp(SMLParser.IntExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitIntExp(SMLParser.IntExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SymbolicExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterSymbolicExp(SMLParser.SymbolicExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SymbolicExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitSymbolicExp(SMLParser.SymbolicExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TupleExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterTupleExp(SMLParser.TupleExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TupleExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitTupleExp(SMLParser.TupleExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#tuple}.
	 * @param ctx the parse tree
	 */
	void enterTuple(SMLParser.TupleContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#tuple}.
	 * @param ctx the parse tree
	 */
	void exitTuple(SMLParser.TupleContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#match}.
	 * @param ctx the parse tree
	 */
	void enterMatch(SMLParser.MatchContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#match}.
	 * @param ctx the parse tree
	 */
	void exitMatch(SMLParser.MatchContext ctx);
}