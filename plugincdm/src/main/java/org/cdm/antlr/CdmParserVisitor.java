package org.cdm.antlr;// Generated from CdmParser.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CdmParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CdmParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CdmParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(CdmParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code absoluteSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbsoluteSection(CdmParser.AbsoluteSectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code relocatableSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelocatableSection(CdmParser.RelocatableSectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code templateSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateSection(CdmParser.TemplateSectionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code macroSection}
	 * labeled alternative in {@link CdmParser#section}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacroSection(CdmParser.MacroSectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#asect_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsect_header(CdmParser.Asect_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#rsect_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRsect_header(CdmParser.Rsect_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#tplate_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTplate_header(CdmParser.Tplate_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#macro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro(CdmParser.MacroContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#macro_header}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMacro_header(CdmParser.Macro_headerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#code_block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode_block(CdmParser.Code_blockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#break_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_statement(CdmParser.Break_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#continue_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_statement(CdmParser.Continue_statementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code standaloneLabels}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStandaloneLabels(CdmParser.StandaloneLabelsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code instructionLine}
	 * labeled alternative in {@link CdmParser#line}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructionLine(CdmParser.InstructionLineContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#instructionWithArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstructionWithArg(CdmParser.InstructionWithArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#labels_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabels_declaration(CdmParser.Labels_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(CdmParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(CdmParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#branch_mnemonic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBranch_mnemonic(CdmParser.Branch_mnemonicContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#conditional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditional(CdmParser.ConditionalContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#conditions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditions(CdmParser.ConditionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#connective_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConnective_condition(CdmParser.Connective_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(CdmParser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#else_clause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_clause(CdmParser.Else_clauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#while_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_loop(CdmParser.While_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#until_loop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil_loop(CdmParser.Until_loopContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(CdmParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#byte_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByte_expr(CdmParser.Byte_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#addr_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddr_expr(CdmParser.Addr_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#first_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFirst_term(CdmParser.First_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#add_term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd_term(CdmParser.Add_termContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(CdmParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(CdmParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#register}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegister(CdmParser.RegisterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#character}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharacter(CdmParser.CharacterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(CdmParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link CdmParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(CdmParser.NameContext ctx);
}