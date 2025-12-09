package org.cdm.antlr;// Generated from TestCdmParser.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TestCdmParser}.
 */
public interface TestCdmParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(TestCdmParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(TestCdmParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 */
	void enterInstructionWithArg(TestCdmParser.InstructionWithArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 */
	void exitInstructionWithArg(TestCdmParser.InstructionWithArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(TestCdmParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(TestCdmParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(TestCdmParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(TestCdmParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#byte_expr}.
	 * @param ctx the parse tree
	 */
	void enterByte_expr(TestCdmParser.Byte_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#byte_expr}.
	 * @param ctx the parse tree
	 */
	void exitByte_expr(TestCdmParser.Byte_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#addr_expr}.
	 * @param ctx the parse tree
	 */
	void enterAddr_expr(TestCdmParser.Addr_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#addr_expr}.
	 * @param ctx the parse tree
	 */
	void exitAddr_expr(TestCdmParser.Addr_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#first_term}.
	 * @param ctx the parse tree
	 */
	void enterFirst_term(TestCdmParser.First_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#first_term}.
	 * @param ctx the parse tree
	 */
	void exitFirst_term(TestCdmParser.First_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#add_term}.
	 * @param ctx the parse tree
	 */
	void enterAdd_term(TestCdmParser.Add_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#add_term}.
	 * @param ctx the parse tree
	 */
	void exitAdd_term(TestCdmParser.Add_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(TestCdmParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(TestCdmParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#byte_specifier}.
	 * @param ctx the parse tree
	 */
	void enterByte_specifier(TestCdmParser.Byte_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#byte_specifier}.
	 * @param ctx the parse tree
	 */
	void exitByte_specifier(TestCdmParser.Byte_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(TestCdmParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(TestCdmParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(TestCdmParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(TestCdmParser.InstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(TestCdmParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(TestCdmParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#register}.
	 * @param ctx the parse tree
	 */
	void enterRegister(TestCdmParser.RegisterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#register}.
	 * @param ctx the parse tree
	 */
	void exitRegister(TestCdmParser.RegisterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(TestCdmParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(TestCdmParser.CharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(TestCdmParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(TestCdmParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link TestCdmParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(TestCdmParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link TestCdmParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(TestCdmParser.NameContext ctx);
}