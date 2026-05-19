package org.sml.antlr;// Generated from SML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class SMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, ID=11, SYMBOLIC_ID=12, INT=13, WS=14;
	public static final int
		RULE_prog = 0, RULE_dec = 1, RULE_valbind = 2, RULE_funbind = 3, RULE_funmatch = 4, 
		RULE_pat = 5, RULE_exp = 6, RULE_match = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "dec", "valbind", "funbind", "funmatch", "pat", "exp", "match"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'val'", "'fun'", "'='", "'and'", "'fn'", "'('", "')'", 
			"'=>'", "'|'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "ID", 
			"SYMBOLIC_ID", "INT", "WS"
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
	public String getGrammarFileName() { return "SML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public List<DecContext> dec() {
			return getRuleContexts(DecContext.class);
		}
		public DecContext dec(int i) {
			return getRuleContext(DecContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			dec();
			setState(21);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(17);
					match(T__0);
					setState(18);
					dec();
					}
					} 
				}
				setState(23);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(25);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(24);
				match(T__0);
				}
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
	public static class DecContext extends ParserRuleContext {
		public ValbindContext valbind() {
			return getRuleContext(ValbindContext.class,0);
		}
		public FunbindContext funbind() {
			return getRuleContext(FunbindContext.class,0);
		}
		public DecContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterDec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitDec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitDec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecContext dec() throws RecognitionException {
		DecContext _localctx = new DecContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_dec);
		try {
			setState(31);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(27);
				match(T__1);
				setState(28);
				valbind();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(29);
				match(T__2);
				setState(30);
				funbind();
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
	public static class ValbindContext extends ParserRuleContext {
		public PatContext pat() {
			return getRuleContext(PatContext.class,0);
		}
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ValbindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valbind; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterValbind(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitValbind(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitValbind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValbindContext valbind() throws RecognitionException {
		ValbindContext _localctx = new ValbindContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_valbind);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(33);
			pat();
			setState(34);
			match(T__3);
			setState(35);
			exp(0);
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
	public static class FunbindContext extends ParserRuleContext {
		public List<FunmatchContext> funmatch() {
			return getRuleContexts(FunmatchContext.class);
		}
		public FunmatchContext funmatch(int i) {
			return getRuleContext(FunmatchContext.class,i);
		}
		public FunbindContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funbind; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterFunbind(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitFunbind(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitFunbind(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunbindContext funbind() throws RecognitionException {
		FunbindContext _localctx = new FunbindContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_funbind);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			funmatch();
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(38);
				match(T__4);
				setState(39);
				funmatch();
				}
				}
				setState(44);
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
	public static class FunmatchContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SMLParser.ID, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public List<PatContext> pat() {
			return getRuleContexts(PatContext.class);
		}
		public PatContext pat(int i) {
			return getRuleContext(PatContext.class,i);
		}
		public FunmatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funmatch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterFunmatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitFunmatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitFunmatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunmatchContext funmatch() throws RecognitionException {
		FunmatchContext _localctx = new FunmatchContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_funmatch);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(ID);
			setState(47); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(46);
				pat();
				}
				}
				setState(49); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(51);
			match(T__3);
			setState(52);
			exp(0);
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
	public static class PatContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SMLParser.ID, 0); }
		public PatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterPat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitPat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitPat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PatContext pat() throws RecognitionException {
		PatContext _localctx = new PatContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_pat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(ID);
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
	public static class ExpContext extends ParserRuleContext {
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	 
		public ExpContext() { }
		public void copyFrom(ExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdExpContext extends ExpContext {
		public TerminalNode ID() { return getToken(SMLParser.ID, 0); }
		public IdExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterIdExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitIdExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitIdExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParensExpContext extends ExpContext {
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public ParensExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterParensExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitParensExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitParensExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ApplicationExpContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public ApplicationExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterApplicationExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitApplicationExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitApplicationExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FnExpContext extends ExpContext {
		public MatchContext match() {
			return getRuleContext(MatchContext.class,0);
		}
		public FnExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterFnExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitFnExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitFnExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntExpContext extends ExpContext {
		public TerminalNode INT() { return getToken(SMLParser.INT, 0); }
		public IntExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterIntExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitIntExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitIntExp(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SymbolicExpContext extends ExpContext {
		public TerminalNode SYMBOLIC_ID() { return getToken(SMLParser.SYMBOLIC_ID, 0); }
		public SymbolicExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterSymbolicExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitSymbolicExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitSymbolicExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_exp, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__5:
				{
				_localctx = new FnExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(57);
				match(T__5);
				setState(58);
				match();
				}
				break;
			case T__6:
				{
				_localctx = new ParensExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(59);
				match(T__6);
				setState(60);
				exp(0);
				setState(61);
				match(T__7);
				}
				break;
			case INT:
				{
				_localctx = new IntExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(63);
				match(INT);
				}
				break;
			case ID:
				{
				_localctx = new IdExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(64);
				match(ID);
				}
				break;
			case SYMBOLIC_ID:
				{
				_localctx = new SymbolicExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(65);
				match(SYMBOLIC_ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(72);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ApplicationExpContext(new ExpContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_exp);
					setState(68);
					if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
					setState(69);
					exp(7);
					}
					} 
				}
				setState(74);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MatchContext extends ParserRuleContext {
		public List<PatContext> pat() {
			return getRuleContexts(PatContext.class);
		}
		public PatContext pat(int i) {
			return getRuleContext(PatContext.class,i);
		}
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public MatchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_match; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).enterMatch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SMLListener ) ((SMLListener)listener).exitMatch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SMLVisitor) return ((SMLVisitor<? extends T>)visitor).visitMatch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MatchContext match() throws RecognitionException {
		MatchContext _localctx = new MatchContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_match);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			pat();
			setState(76);
			match(T__8);
			setState(77);
			exp(0);
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(78);
					match(T__9);
					setState(79);
					pat();
					setState(80);
					match(T__8);
					setState(81);
					exp(0);
					}
					} 
				}
				setState(87);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u000eY\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u0014\b\u0000\n\u0000\f\u0000"+
		"\u0017\t\u0000\u0001\u0000\u0003\u0000\u001a\b\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u0001 \b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u0003)\b\u0003\n\u0003\f\u0003,\t\u0003\u0001\u0004\u0001\u0004\u0004"+
		"\u00040\b\u0004\u000b\u0004\f\u00041\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006C\b\u0006\u0001\u0006\u0001\u0006\u0005\u0006G\b\u0006\n\u0006"+
		"\f\u0006J\t\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007T\b\u0007\n\u0007"+
		"\f\u0007W\t\u0007\u0001\u0007\u0000\u0001\f\b\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0000\u0000[\u0000\u0010\u0001\u0000\u0000\u0000\u0002\u001f"+
		"\u0001\u0000\u0000\u0000\u0004!\u0001\u0000\u0000\u0000\u0006%\u0001\u0000"+
		"\u0000\u0000\b-\u0001\u0000\u0000\u0000\n6\u0001\u0000\u0000\u0000\fB"+
		"\u0001\u0000\u0000\u0000\u000eK\u0001\u0000\u0000\u0000\u0010\u0015\u0003"+
		"\u0002\u0001\u0000\u0011\u0012\u0005\u0001\u0000\u0000\u0012\u0014\u0003"+
		"\u0002\u0001\u0000\u0013\u0011\u0001\u0000\u0000\u0000\u0014\u0017\u0001"+
		"\u0000\u0000\u0000\u0015\u0013\u0001\u0000\u0000\u0000\u0015\u0016\u0001"+
		"\u0000\u0000\u0000\u0016\u0019\u0001\u0000\u0000\u0000\u0017\u0015\u0001"+
		"\u0000\u0000\u0000\u0018\u001a\u0005\u0001\u0000\u0000\u0019\u0018\u0001"+
		"\u0000\u0000\u0000\u0019\u001a\u0001\u0000\u0000\u0000\u001a\u0001\u0001"+
		"\u0000\u0000\u0000\u001b\u001c\u0005\u0002\u0000\u0000\u001c \u0003\u0004"+
		"\u0002\u0000\u001d\u001e\u0005\u0003\u0000\u0000\u001e \u0003\u0006\u0003"+
		"\u0000\u001f\u001b\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000"+
		"\u0000 \u0003\u0001\u0000\u0000\u0000!\"\u0003\n\u0005\u0000\"#\u0005"+
		"\u0004\u0000\u0000#$\u0003\f\u0006\u0000$\u0005\u0001\u0000\u0000\u0000"+
		"%*\u0003\b\u0004\u0000&\'\u0005\u0005\u0000\u0000\')\u0003\b\u0004\u0000"+
		"(&\u0001\u0000\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000"+
		"\u0000*+\u0001\u0000\u0000\u0000+\u0007\u0001\u0000\u0000\u0000,*\u0001"+
		"\u0000\u0000\u0000-/\u0005\u000b\u0000\u0000.0\u0003\n\u0005\u0000/.\u0001"+
		"\u0000\u0000\u000001\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u0000"+
		"12\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u000034\u0005\u0004\u0000"+
		"\u000045\u0003\f\u0006\u00005\t\u0001\u0000\u0000\u000067\u0005\u000b"+
		"\u0000\u00007\u000b\u0001\u0000\u0000\u000089\u0006\u0006\uffff\uffff"+
		"\u00009:\u0005\u0006\u0000\u0000:C\u0003\u000e\u0007\u0000;<\u0005\u0007"+
		"\u0000\u0000<=\u0003\f\u0006\u0000=>\u0005\b\u0000\u0000>C\u0001\u0000"+
		"\u0000\u0000?C\u0005\r\u0000\u0000@C\u0005\u000b\u0000\u0000AC\u0005\f"+
		"\u0000\u0000B8\u0001\u0000\u0000\u0000B;\u0001\u0000\u0000\u0000B?\u0001"+
		"\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BA\u0001\u0000\u0000\u0000"+
		"CH\u0001\u0000\u0000\u0000DE\n\u0006\u0000\u0000EG\u0003\f\u0006\u0007"+
		"FD\u0001\u0000\u0000\u0000GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000"+
		"\u0000HI\u0001\u0000\u0000\u0000I\r\u0001\u0000\u0000\u0000JH\u0001\u0000"+
		"\u0000\u0000KL\u0003\n\u0005\u0000LM\u0005\t\u0000\u0000MU\u0003\f\u0006"+
		"\u0000NO\u0005\n\u0000\u0000OP\u0003\n\u0005\u0000PQ\u0005\t\u0000\u0000"+
		"QR\u0003\f\u0006\u0000RT\u0001\u0000\u0000\u0000SN\u0001\u0000\u0000\u0000"+
		"TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000"+
		"\u0000V\u000f\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000\b\u0015"+
		"\u0019\u001f*1BHU";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}