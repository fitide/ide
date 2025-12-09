package org.ide.LinkTreeController.cdmTest.cdm.antlr;// Generated from TestCdmParser.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class TestCdmParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Asect=1, Break=2, Continue=3, Do=4, Else=5, End=6, Ext=7, Fi=8, If=9, 
		Is=10, Macro=11, Rsect=12, Stays=13, Then=14, Tplate=15, Until=16, Wend=17, 
		While=18, DOT=19, COMMA=20, PLUS=21, MINUS=22, COLON=23, ASTERISK=24, 
		ANGLE_BRACKET=25, OPEN_PAREN=26, CLOSE_PAREN=27, LINE_MARK_MARKER=28, 
		REGISTER=29, WORD=30, WORD_WITH_DOTS=31, DECIMAL_NUMBER=32, BINARY_NUMBER=33, 
		HEX_NUMBER=34, STRING=35, CHAR=36, NEWLINE=37, COMMENT=38, WS=39, BASE64=40, 
		UNEXPECTED_TOKEN=41;
	public static final int
		RULE_program = 0, RULE_instructionWithArg = 1, RULE_arguments = 2, RULE_argument = 3, 
		RULE_byte_expr = 4, RULE_addr_expr = 5, RULE_first_term = 6, RULE_add_term = 7, 
		RULE_term = 8, RULE_byte_specifier = 9, RULE_label = 10, RULE_instruction = 11, 
		RULE_string = 12, RULE_register = 13, RULE_character = 14, RULE_number = 15, 
		RULE_name = 16;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "instructionWithArg", "arguments", "argument", "byte_expr", 
			"addr_expr", "first_term", "add_term", "term", "byte_specifier", "label", 
			"instruction", "string", "register", "character", "number", "name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'asect'", "'break'", "'continue'", "'do'", "'else'", "'end'", 
			"'ext'", "'fi'", "'if'", "'is'", "'macro'", "'rsect'", "'stays'", "'then'", 
			"'tplate'", "'until'", "'wend'", "'while'", "'.'", "','", "'+'", "'-'", 
			"':'", "'*'", "'>'", "'('", "')'", "'-|'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Asect", "Break", "Continue", "Do", "Else", "End", "Ext", "Fi", 
			"If", "Is", "Macro", "Rsect", "Stays", "Then", "Tplate", "Until", "Wend", 
			"While", "DOT", "COMMA", "PLUS", "MINUS", "COLON", "ASTERISK", "ANGLE_BRACKET", 
			"OPEN_PAREN", "CLOSE_PAREN", "LINE_MARK_MARKER", "REGISTER", "WORD", 
			"WORD_WITH_DOTS", "DECIMAL_NUMBER", "BINARY_NUMBER", "HEX_NUMBER", "STRING", 
			"CHAR", "NEWLINE", "COMMENT", "WS", "BASE64", "UNEXPECTED_TOKEN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "TestCdmParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    private String currentFile = "";
	    private int currentLine = 0;
	    private int currentOffset = 0;

	    public String getCurrentFile() { return currentFile; }
	    public int getCurrentLine() { return currentLine; }
	    public int getCurrentOffset() { return currentOffset; }

	public TestCdmParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode End() { return getToken(TestCdmParser.End, 0); }
		public List<InstructionWithArgContext> instructionWithArg() {
			return getRuleContexts(InstructionWithArgContext.class);
		}
		public InstructionWithArgContext instructionWithArg(int i) {
			return getRuleContext(InstructionWithArgContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WORD) {
				{
				{
				setState(34);
				instructionWithArg();
				}
				}
				setState(39);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(40);
			match(End);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstructionWithArgContext extends ParserRuleContext {
		public InstructionContext instruction() {
			return getRuleContext(InstructionContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public InstructionWithArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instructionWithArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterInstructionWithArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitInstructionWithArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitInstructionWithArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionWithArgContext instructionWithArg() throws RecognitionException {
		InstructionWithArgContext _localctx = new InstructionWithArgContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_instructionWithArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			instruction();
			setState(44);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(43);
				arguments();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentsContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TestCdmParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TestCdmParser.COMMA, i);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			argument();
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(47);
				match(COMMA);
				setState(48);
				argument();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentContext extends ParserRuleContext {
		public CharacterContext character() {
			return getRuleContext(CharacterContext.class,0);
		}
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public RegisterContext register() {
			return getRuleContext(RegisterContext.class,0);
		}
		public Addr_exprContext addr_expr() {
			return getRuleContext(Addr_exprContext.class,0);
		}
		public Byte_exprContext byte_expr() {
			return getRuleContext(Byte_exprContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_argument);
		try {
			setState(59);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				character();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				string();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				register();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(57);
				addr_expr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(58);
				byte_expr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Byte_exprContext extends ParserRuleContext {
		public Byte_specifierContext byte_specifier() {
			return getRuleContext(Byte_specifierContext.class,0);
		}
		public TerminalNode OPEN_PAREN() { return getToken(TestCdmParser.OPEN_PAREN, 0); }
		public Addr_exprContext addr_expr() {
			return getRuleContext(Addr_exprContext.class,0);
		}
		public TerminalNode CLOSE_PAREN() { return getToken(TestCdmParser.CLOSE_PAREN, 0); }
		public Byte_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_byte_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterByte_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitByte_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitByte_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Byte_exprContext byte_expr() throws RecognitionException {
		Byte_exprContext _localctx = new Byte_exprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_byte_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			byte_specifier();
			setState(62);
			match(OPEN_PAREN);
			setState(63);
			addr_expr();
			setState(64);
			match(CLOSE_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Addr_exprContext extends ParserRuleContext {
		public First_termContext first_term() {
			return getRuleContext(First_termContext.class,0);
		}
		public List<Add_termContext> add_term() {
			return getRuleContexts(Add_termContext.class);
		}
		public Add_termContext add_term(int i) {
			return getRuleContext(Add_termContext.class,i);
		}
		public Addr_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addr_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterAddr_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitAddr_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitAddr_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Addr_exprContext addr_expr() throws RecognitionException {
		Addr_exprContext _localctx = new Addr_exprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_addr_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			first_term();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS || _la==MINUS) {
				{
				{
				setState(67);
				add_term();
				}
				}
				setState(72);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class First_termContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(TestCdmParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(TestCdmParser.MINUS, 0); }
		public First_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_first_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterFirst_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitFirst_term(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitFirst_term(this);
			else return visitor.visitChildren(this);
		}
	}

	public final First_termContext first_term() throws RecognitionException {
		First_termContext _localctx = new First_termContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_first_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PLUS || _la==MINUS) {
				{
				setState(73);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(76);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Add_termContext extends ParserRuleContext {
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(TestCdmParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(TestCdmParser.MINUS, 0); }
		public Add_termContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_add_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterAdd_term(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitAdd_term(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitAdd_term(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Add_termContext add_term() throws RecognitionException {
		Add_termContext _localctx = new Add_termContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_add_term);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_la = _input.LA(1);
			if ( !(_la==PLUS || _la==MINUS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(79);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_term);
		try {
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DECIMAL_NUMBER:
			case BINARY_NUMBER:
			case HEX_NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				number();
				}
				break;
			case Asect:
			case Break:
			case Continue:
			case Do:
			case Else:
			case End:
			case Ext:
			case Fi:
			case If:
			case Is:
			case Macro:
			case Rsect:
			case Stays:
			case Then:
			case Tplate:
			case Until:
			case Wend:
			case While:
			case WORD:
			case WORD_WITH_DOTS:
				enterOuterAlt(_localctx, 2);
				{
				setState(82);
				label();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Byte_specifierContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Byte_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_byte_specifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterByte_specifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitByte_specifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitByte_specifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Byte_specifierContext byte_specifier() throws RecognitionException {
		Byte_specifierContext _localctx = new Byte_specifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_byte_specifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LabelContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstructionContext extends ParserRuleContext {
		public TerminalNode WORD() { return getToken(TestCdmParser.WORD, 0); }
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitInstruction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitInstruction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_instruction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(WORD);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(TestCdmParser.STRING, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegisterContext extends ParserRuleContext {
		public TerminalNode REGISTER() { return getToken(TestCdmParser.REGISTER, 0); }
		public RegisterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_register; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterRegister(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitRegister(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitRegister(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegisterContext register() throws RecognitionException {
		RegisterContext _localctx = new RegisterContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_register);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			match(REGISTER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CharacterContext extends ParserRuleContext {
		public TerminalNode CHAR() { return getToken(TestCdmParser.CHAR, 0); }
		public CharacterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_character; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterCharacter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitCharacter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitCharacter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CharacterContext character() throws RecognitionException {
		CharacterContext _localctx = new CharacterContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_character);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(CHAR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NumberContext extends ParserRuleContext {
		public TerminalNode DECIMAL_NUMBER() { return getToken(TestCdmParser.DECIMAL_NUMBER, 0); }
		public TerminalNode HEX_NUMBER() { return getToken(TestCdmParser.HEX_NUMBER, 0); }
		public TerminalNode BINARY_NUMBER() { return getToken(TestCdmParser.BINARY_NUMBER, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30064771072L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NameContext extends ParserRuleContext {
		public TerminalNode Asect() { return getToken(TestCdmParser.Asect, 0); }
		public TerminalNode Break() { return getToken(TestCdmParser.Break, 0); }
		public TerminalNode Continue() { return getToken(TestCdmParser.Continue, 0); }
		public TerminalNode Do() { return getToken(TestCdmParser.Do, 0); }
		public TerminalNode Else() { return getToken(TestCdmParser.Else, 0); }
		public TerminalNode End() { return getToken(TestCdmParser.End, 0); }
		public TerminalNode Ext() { return getToken(TestCdmParser.Ext, 0); }
		public TerminalNode Fi() { return getToken(TestCdmParser.Fi, 0); }
		public TerminalNode If() { return getToken(TestCdmParser.If, 0); }
		public TerminalNode Is() { return getToken(TestCdmParser.Is, 0); }
		public TerminalNode Macro() { return getToken(TestCdmParser.Macro, 0); }
		public TerminalNode Rsect() { return getToken(TestCdmParser.Rsect, 0); }
		public TerminalNode Stays() { return getToken(TestCdmParser.Stays, 0); }
		public TerminalNode Then() { return getToken(TestCdmParser.Then, 0); }
		public TerminalNode Tplate() { return getToken(TestCdmParser.Tplate, 0); }
		public TerminalNode Until() { return getToken(TestCdmParser.Until, 0); }
		public TerminalNode Wend() { return getToken(TestCdmParser.Wend, 0); }
		public TerminalNode While() { return getToken(TestCdmParser.While, 0); }
		public TerminalNode WORD() { return getToken(TestCdmParser.WORD, 0); }
		public TerminalNode WORD_WITH_DOTS() { return getToken(TestCdmParser.WORD_WITH_DOTS, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TestCdmParserListener) ((TestCdmParserListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TestCdmParserVisitor) return ((TestCdmParserVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 3221749758L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001)f\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002"+
		"\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005"+
		"\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007"+
		"\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007"+
		"\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002"+
		"\u0010\u0007\u0010\u0001\u0000\u0005\u0000$\b\u0000\n\u0000\f\u0000\'"+
		"\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0003\u0001-"+
		"\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u00022\b\u0002\n\u0002"+
		"\f\u00025\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0003\u0003<\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0005\u0005E\b\u0005\n\u0005"+
		"\f\u0005H\t\u0005\u0001\u0006\u0003\u0006K\b\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0003\bT\b\b\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0000\u0000\u0011\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \u0000\u0003"+
		"\u0001\u0000\u0015\u0016\u0001\u0000 \"\u0002\u0000\u0001\u0012\u001e"+
		"\u001f^\u0000%\u0001\u0000\u0000\u0000\u0002*\u0001\u0000\u0000\u0000"+
		"\u0004.\u0001\u0000\u0000\u0000\u0006;\u0001\u0000\u0000\u0000\b=\u0001"+
		"\u0000\u0000\u0000\nB\u0001\u0000\u0000\u0000\fJ\u0001\u0000\u0000\u0000"+
		"\u000eN\u0001\u0000\u0000\u0000\u0010S\u0001\u0000\u0000\u0000\u0012U"+
		"\u0001\u0000\u0000\u0000\u0014W\u0001\u0000\u0000\u0000\u0016Y\u0001\u0000"+
		"\u0000\u0000\u0018[\u0001\u0000\u0000\u0000\u001a]\u0001\u0000\u0000\u0000"+
		"\u001c_\u0001\u0000\u0000\u0000\u001ea\u0001\u0000\u0000\u0000 c\u0001"+
		"\u0000\u0000\u0000\"$\u0003\u0002\u0001\u0000#\"\u0001\u0000\u0000\u0000"+
		"$\'\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000%&\u0001\u0000\u0000"+
		"\u0000&(\u0001\u0000\u0000\u0000\'%\u0001\u0000\u0000\u0000()\u0005\u0006"+
		"\u0000\u0000)\u0001\u0001\u0000\u0000\u0000*,\u0003\u0016\u000b\u0000"+
		"+-\u0003\u0004\u0002\u0000,+\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000"+
		"\u0000-\u0003\u0001\u0000\u0000\u0000.3\u0003\u0006\u0003\u0000/0\u0005"+
		"\u0014\u0000\u000002\u0003\u0006\u0003\u00001/\u0001\u0000\u0000\u0000"+
		"25\u0001\u0000\u0000\u000031\u0001\u0000\u0000\u000034\u0001\u0000\u0000"+
		"\u00004\u0005\u0001\u0000\u0000\u000053\u0001\u0000\u0000\u00006<\u0003"+
		"\u001c\u000e\u00007<\u0003\u0018\f\u00008<\u0003\u001a\r\u00009<\u0003"+
		"\n\u0005\u0000:<\u0003\b\u0004\u0000;6\u0001\u0000\u0000\u0000;7\u0001"+
		"\u0000\u0000\u0000;8\u0001\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000"+
		";:\u0001\u0000\u0000\u0000<\u0007\u0001\u0000\u0000\u0000=>\u0003\u0012"+
		"\t\u0000>?\u0005\u001a\u0000\u0000?@\u0003\n\u0005\u0000@A\u0005\u001b"+
		"\u0000\u0000A\t\u0001\u0000\u0000\u0000BF\u0003\f\u0006\u0000CE\u0003"+
		"\u000e\u0007\u0000DC\u0001\u0000\u0000\u0000EH\u0001\u0000\u0000\u0000"+
		"FD\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000G\u000b\u0001\u0000"+
		"\u0000\u0000HF\u0001\u0000\u0000\u0000IK\u0007\u0000\u0000\u0000JI\u0001"+
		"\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000"+
		"LM\u0003\u0010\b\u0000M\r\u0001\u0000\u0000\u0000NO\u0007\u0000\u0000"+
		"\u0000OP\u0003\u0010\b\u0000P\u000f\u0001\u0000\u0000\u0000QT\u0003\u001e"+
		"\u000f\u0000RT\u0003\u0014\n\u0000SQ\u0001\u0000\u0000\u0000SR\u0001\u0000"+
		"\u0000\u0000T\u0011\u0001\u0000\u0000\u0000UV\u0003 \u0010\u0000V\u0013"+
		"\u0001\u0000\u0000\u0000WX\u0003 \u0010\u0000X\u0015\u0001\u0000\u0000"+
		"\u0000YZ\u0005\u001e\u0000\u0000Z\u0017\u0001\u0000\u0000\u0000[\\\u0005"+
		"#\u0000\u0000\\\u0019\u0001\u0000\u0000\u0000]^\u0005\u001d\u0000\u0000"+
		"^\u001b\u0001\u0000\u0000\u0000_`\u0005$\u0000\u0000`\u001d\u0001\u0000"+
		"\u0000\u0000ab\u0007\u0001\u0000\u0000b\u001f\u0001\u0000\u0000\u0000"+
		"cd\u0007\u0002\u0000\u0000d!\u0001\u0000\u0000\u0000\u0007%,3;FJS";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}