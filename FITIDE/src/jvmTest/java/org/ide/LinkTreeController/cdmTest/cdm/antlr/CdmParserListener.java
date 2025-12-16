package org.ide.LinkTreeController.cdmTest.cdm.antlr;// Generated from CdmParser.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CdmParser}.
 */
public interface CdmParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CdmParser#program_nomacros}.
	 * @param ctx the parse tree
	 */
	void enterProgram_nomacros(CdmParser.Program_nomacrosContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#program_nomacros}.
	 * @param ctx the parse tree
	 */
	void exitProgram_nomacros(CdmParser.Program_nomacrosContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CdmParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CdmParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code absoluteSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void enterAbsoluteSection(CdmParser.AbsoluteSectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code absoluteSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void exitAbsoluteSection(CdmParser.AbsoluteSectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relocatableSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void enterRelocatableSection(CdmParser.RelocatableSectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relocatableSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void exitRelocatableSection(CdmParser.RelocatableSectionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code templateSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void enterTemplateSection(CdmParser.TemplateSectionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code templateSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 */
	void exitTemplateSection(CdmParser.TemplateSectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#asect_header}.
	 * @param ctx the parse tree
	 */
	void enterAsect_header(CdmParser.Asect_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#asect_header}.
	 * @param ctx the parse tree
	 */
	void exitAsect_header(CdmParser.Asect_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#rsect_header}.
	 * @param ctx the parse tree
	 */
	void enterRsect_header(CdmParser.Rsect_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#rsect_header}.
	 * @param ctx the parse tree
	 */
	void exitRsect_header(CdmParser.Rsect_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#tplate_header}.
	 * @param ctx the parse tree
	 */
	void enterTplate_header(CdmParser.Tplate_headerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#tplate_header}.
	 * @param ctx the parse tree
	 */
	void exitTplate_header(CdmParser.Tplate_headerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#section_body}.
	 * @param ctx the parse tree
	 */
	void enterSection_body(CdmParser.Section_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#section_body}.
	 * @param ctx the parse tree
	 */
	void exitSection_body(CdmParser.Section_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#code_block}.
	 * @param ctx the parse tree
	 */
	void enterCode_block(CdmParser.Code_blockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#code_block}.
	 * @param ctx the parse tree
	 */
	void exitCode_block(CdmParser.Code_blockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#line_mark}.
	 * @param ctx the parse tree
	 */
	void enterLine_mark(CdmParser.Line_markContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#line_mark}.
	 * @param ctx the parse tree
	 */
	void exitLine_mark(CdmParser.Line_markContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#line_number}.
	 * @param ctx the parse tree
	 */
	void enterLine_number(CdmParser.Line_numberContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#line_number}.
	 * @param ctx the parse tree
	 */
	void exitLine_number(CdmParser.Line_numberContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#filepath}.
	 * @param ctx the parse tree
	 */
	void enterFilepath(CdmParser.FilepathContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#filepath}.
	 * @param ctx the parse tree
	 */
	void exitFilepath(CdmParser.FilepathContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void enterBreak_statement(CdmParser.Break_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#break_statement}.
	 * @param ctx the parse tree
	 */
	void exitBreak_statement(CdmParser.Break_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void enterContinue_statement(CdmParser.Continue_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#continue_statement}.
	 * @param ctx the parse tree
	 */
	void exitContinue_statement(CdmParser.Continue_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#top_line}.
	 * @param ctx the parse tree
	 */
	void enterTop_line(CdmParser.Top_lineContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#top_line}.
	 * @param ctx the parse tree
	 */
	void exitTop_line(CdmParser.Top_lineContext ctx);
	/**
	 * Enter a parse tree produced by the {@code standaloneLabels}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 */
	void enterStandaloneLabels(CdmParser.StandaloneLabelsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code standaloneLabels}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 */
	void exitStandaloneLabels(CdmParser.StandaloneLabelsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code instructionLine}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 */
	void enterInstructionLine(CdmParser.InstructionLineContext ctx);
	/**
	 * Exit a parse tree produced by the {@code instructionLine}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 */
	void exitInstructionLine(CdmParser.InstructionLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 */
	void enterInstructionWithArg(CdmParser.InstructionWithArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 */
	void exitInstructionWithArg(CdmParser.InstructionWithArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#labels_declaration}.
	 * @param ctx the parse tree
	 */
	void enterLabels_declaration(CdmParser.Labels_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#labels_declaration}.
	 * @param ctx the parse tree
	 */
	void exitLabels_declaration(CdmParser.Labels_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#labels}.
	 * @param ctx the parse tree
	 */
	void enterLabels(CdmParser.LabelsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#labels}.
	 * @param ctx the parse tree
	 */
	void exitLabels(CdmParser.LabelsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(CdmParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(CdmParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#conditional}.
	 * @param ctx the parse tree
	 */
	void enterConditional(CdmParser.ConditionalContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#conditional}.
	 * @param ctx the parse tree
	 */
	void exitConditional(CdmParser.ConditionalContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#conditions}.
	 * @param ctx the parse tree
	 */
	void enterConditions(CdmParser.ConditionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#conditions}.
	 * @param ctx the parse tree
	 */
	void exitConditions(CdmParser.ConditionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#connective_condition}.
	 * @param ctx the parse tree
	 */
	void enterConnective_condition(CdmParser.Connective_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#connective_condition}.
	 * @param ctx the parse tree
	 */
	void exitConnective_condition(CdmParser.Connective_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(CdmParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(CdmParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void enterElse_clause(CdmParser.Else_clauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#else_clause}.
	 * @param ctx the parse tree
	 */
	void exitElse_clause(CdmParser.Else_clauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#branch_mnemonic}.
	 * @param ctx the parse tree
	 */
	void enterBranch_mnemonic(CdmParser.Branch_mnemonicContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#branch_mnemonic}.
	 * @param ctx the parse tree
	 */
	void exitBranch_mnemonic(CdmParser.Branch_mnemonicContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(CdmParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(CdmParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void enterWhile_loop(CdmParser.While_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#while_loop}.
	 * @param ctx the parse tree
	 */
	void exitWhile_loop(CdmParser.While_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#while_condition}.
	 * @param ctx the parse tree
	 */
	void enterWhile_condition(CdmParser.While_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#while_condition}.
	 * @param ctx the parse tree
	 */
	void exitWhile_condition(CdmParser.While_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#until_loop}.
	 * @param ctx the parse tree
	 */
	void enterUntil_loop(CdmParser.Until_loopContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#until_loop}.
	 * @param ctx the parse tree
	 */
	void exitUntil_loop(CdmParser.Until_loopContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(CdmParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(CdmParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#byte_expr}.
	 * @param ctx the parse tree
	 */
	void enterByte_expr(CdmParser.Byte_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#byte_expr}.
	 * @param ctx the parse tree
	 */
	void exitByte_expr(CdmParser.Byte_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#addr_expr}.
	 * @param ctx the parse tree
	 */
	void enterAddr_expr(CdmParser.Addr_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#addr_expr}.
	 * @param ctx the parse tree
	 */
	void exitAddr_expr(CdmParser.Addr_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#first_term}.
	 * @param ctx the parse tree
	 */
	void enterFirst_term(CdmParser.First_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#first_term}.
	 * @param ctx the parse tree
	 */
	void exitFirst_term(CdmParser.First_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#add_term}.
	 * @param ctx the parse tree
	 */
	void enterAdd_term(CdmParser.Add_termContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#add_term}.
	 * @param ctx the parse tree
	 */
	void exitAdd_term(CdmParser.Add_termContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(CdmParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(CdmParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#byte_specifier}.
	 * @param ctx the parse tree
	 */
	void enterByte_specifier(CdmParser.Byte_specifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#byte_specifier}.
	 * @param ctx the parse tree
	 */
	void exitByte_specifier(CdmParser.Byte_specifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(CdmParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(CdmParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(CdmParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(CdmParser.InstructionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(CdmParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(CdmParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#register}.
	 * @param ctx the parse tree
	 */
	void enterRegister(CdmParser.RegisterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#register}.
	 * @param ctx the parse tree
	 */
	void exitRegister(CdmParser.RegisterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#character}.
	 * @param ctx the parse tree
	 */
	void enterCharacter(CdmParser.CharacterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#character}.
	 * @param ctx the parse tree
	 */
	void exitCharacter(CdmParser.CharacterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(CdmParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(CdmParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link CdmParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(CdmParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CdmParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(CdmParser.NameContext ctx);
}