package org.cdm.antlr;// Generated from TestCdmParser.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TestCdmParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TestCdmParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(TestCdmParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructionWithArg(TestCdmParser.InstructionWithArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(TestCdmParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(TestCdmParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#byte_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByte_expr(TestCdmParser.Byte_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#addr_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddr_expr(TestCdmParser.Addr_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#first_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFirst_term(TestCdmParser.First_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#add_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_term(TestCdmParser.Add_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(TestCdmParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#byte_specifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByte_specifier(TestCdmParser.Byte_specifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(TestCdmParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstruction(TestCdmParser.InstructionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(TestCdmParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#register}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegister(TestCdmParser.RegisterContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(TestCdmParser.CharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(TestCdmParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link TestCdmParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(TestCdmParser.NameContext ctx);
}