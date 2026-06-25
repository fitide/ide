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
	 * Enter a parse tree produced by the {@code ValDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterValDec(SMLParser.ValDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitValDec(SMLParser.ValDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterFunDec(SMLParser.FunDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitFunDec(SMLParser.FunDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StructDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterStructDec(SMLParser.StructDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StructDec}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitStructDec(SMLParser.StructDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InfixId}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterInfixId(SMLParser.InfixIdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InfixId}
	 * labeled alternative in {@link SMLParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitInfixId(SMLParser.InfixIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#infixarg}.
	 * @param ctx the parse tree
	 */
	void enterInfixarg(SMLParser.InfixargContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#infixarg}.
	 * @param ctx the parse tree
	 */
	void exitInfixarg(SMLParser.InfixargContext ctx);
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
	 * Enter a parse tree produced by {@link SMLParser#structbind}.
	 * @param ctx the parse tree
	 */
	void enterStructbind(SMLParser.StructbindContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#structbind}.
	 * @param ctx the parse tree
	 */
	void exitStructbind(SMLParser.StructbindContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#decs}.
	 * @param ctx the parse tree
	 */
	void enterDecs(SMLParser.DecsContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#decs}.
	 * @param ctx the parse tree
	 */
	void exitDecs(SMLParser.DecsContext ctx);
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
	 * Enter a parse tree produced by the {@code LongIdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterLongIdExp(SMLParser.LongIdExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LongIdExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitLongIdExp(SMLParser.LongIdExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterMulExp(SMLParser.MulExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitMulExp(SMLParser.MulExpContext ctx);
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
	 * Enter a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp(SMLParser.AddExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddExp}
	 * labeled alternative in {@link SMLParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp(SMLParser.AddExpContext ctx);
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
	 * Enter a parse tree produced by the {@code TupleArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterTupleArg(SMLParser.TupleArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TupleArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitTupleArg(SMLParser.TupleArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterParensArg(SMLParser.ParensArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitParensArg(SMLParser.ParensArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterIntArg(SMLParser.IntArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitIntArg(SMLParser.IntArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterIdArg(SMLParser.IdArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitIdArg(SMLParser.IdArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SymbolicArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterSymbolicArg(SMLParser.SymbolicArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SymbolicArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitSymbolicArg(SMLParser.SymbolicArgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LongIdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void enterLongIdArg(SMLParser.LongIdArgContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LongIdArg}
	 * labeled alternative in {@link SMLParser#arg}.
	 * @param ctx the parse tree
	 */
	void exitLongIdArg(SMLParser.LongIdArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void enterMulOp(SMLParser.MulOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#mulOp}.
	 * @param ctx the parse tree
	 */
	void exitMulOp(SMLParser.MulOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#addOp}.
	 * @param ctx the parse tree
	 */
	void enterAddOp(SMLParser.AddOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#addOp}.
	 * @param ctx the parse tree
	 */
	void exitAddOp(SMLParser.AddOpContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link SMLParser#longid}.
	 * @param ctx the parse tree
	 */
	void enterLongid(SMLParser.LongidContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#longid}.
	 * @param ctx the parse tree
	 */
	void exitLongid(SMLParser.LongidContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void enterTupleType(SMLParser.TupleTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TupleType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void exitTupleType(SMLParser.TupleTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FunType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void enterFunType(SMLParser.FunTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FunType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void exitFunType(SMLParser.FunTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParensType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void enterParensType(SMLParser.ParensTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParensType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void exitParensType(SMLParser.ParensTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void enterIntType(SMLParser.IntTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntType}
	 * labeled alternative in {@link SMLParser#typ}.
	 * @param ctx the parse tree
	 */
	void exitIntType(SMLParser.IntTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#val}.
	 * @param ctx the parse tree
	 */
	void enterVal(SMLParser.ValContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#val}.
	 * @param ctx the parse tree
	 */
	void exitVal(SMLParser.ValContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#fun}.
	 * @param ctx the parse tree
	 */
	void enterFun(SMLParser.FunContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#fun}.
	 * @param ctx the parse tree
	 */
	void exitFun(SMLParser.FunContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#structure}.
	 * @param ctx the parse tree
	 */
	void enterStructure(SMLParser.StructureContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#structure}.
	 * @param ctx the parse tree
	 */
	void exitStructure(SMLParser.StructureContext ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#struct_}.
	 * @param ctx the parse tree
	 */
	void enterStruct_(SMLParser.Struct_Context ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#struct_}.
	 * @param ctx the parse tree
	 */
	void exitStruct_(SMLParser.Struct_Context ctx);
	/**
	 * Enter a parse tree produced by {@link SMLParser#end}.
	 * @param ctx the parse tree
	 */
	void enterEnd(SMLParser.EndContext ctx);
	/**
	 * Exit a parse tree produced by {@link SMLParser#end}.
	 * @param ctx the parse tree
	 */
	void exitEnd(SMLParser.EndContext ctx);
}