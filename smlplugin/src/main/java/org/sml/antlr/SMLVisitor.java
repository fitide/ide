package org.sml.antlr;// Generated from SML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SMLParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(SMLParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDec(SMLParser.DecContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#valbind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValbind(SMLParser.ValbindContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#funbind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunbind(SMLParser.FunbindContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#funmatch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunmatch(SMLParser.FunmatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#pat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPat(SMLParser.PatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExp(SMLParser.IdExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensExp(SMLParser.ParensExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ApplicationExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplicationExp(SMLParser.ApplicationExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FnExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFnExp(SMLParser.FnExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExp(SMLParser.IntExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SymbolicExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbolicExp(SMLParser.SymbolicExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(SMLParser.MatchContext ctx);
}