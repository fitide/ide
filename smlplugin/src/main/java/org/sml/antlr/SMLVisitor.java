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
	 * Visit a parse tree produced by the {@code ValDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValDec(SMLParser.ValDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunDec(SMLParser.FunDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StructDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructDec(SMLParser.StructDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InfixId}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixId(SMLParser.InfixIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#infixarg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInfixarg(SMLParser.InfixargContext ctx);
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
	 * Visit a parse tree produced by {@link SMLParser#structbind}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructbind(SMLParser.StructbindContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#decs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecs(SMLParser.DecsContext ctx);
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
	 * Visit a parse tree produced by the {@code LongIdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongIdExp(SMLParser.LongIdExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulExp(SMLParser.MulExpContext ctx);
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
	 * Visit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddExp(SMLParser.AddExpContext ctx);
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
	 * Visit a parse tree produced by the {@code TupleExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleExp(SMLParser.TupleExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleArg(SMLParser.TupleArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensArg(SMLParser.ParensArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntArg(SMLParser.IntArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdArg(SMLParser.IdArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SymbolicArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSymbolicArg(SMLParser.SymbolicArgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LongIdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongIdArg(SMLParser.LongIdArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#mulOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOp(SMLParser.MulOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#addOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOp(SMLParser.AddOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#tuple}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTuple(SMLParser.TupleContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#match}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(SMLParser.MatchContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#longid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongid(SMLParser.LongidContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTupleType(SMLParser.TupleTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FunType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunType(SMLParser.FunTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParensType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensType(SMLParser.ParensTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntType(SMLParser.IntTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#val}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVal(SMLParser.ValContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#fun}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFun(SMLParser.FunContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#structure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructure(SMLParser.StructureContext ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#struct_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStruct_(SMLParser.Struct_Context ctx);
	/**
	 * Visit a parse tree produced by {@link SMLParser#end}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnd(SMLParser.EndContext ctx);
}