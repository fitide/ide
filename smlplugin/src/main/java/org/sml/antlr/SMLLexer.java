package org.sml.antlr;// Generated from SML.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SMLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, ID=11, SYMBOLIC_ID=12, INT=13, WS=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "ID", "SYMBOLIC_ID", "INT", "WS"
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


	public SMLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SML.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u000eQ\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t"+
		"\u0001\n\u0001\n\u0005\n<\b\n\n\n\f\n?\t\n\u0001\u000b\u0004\u000bB\b"+
		"\u000b\u000b\u000b\f\u000bC\u0001\f\u0004\fG\b\f\u000b\f\f\fH\u0001\r"+
		"\u0004\rL\b\r\u000b\r\f\rM\u0001\r\u0001\r\u0000\u0000\u000e\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u0001\u0000"+
		"\u0005\u0002\u0000AZaz\u0005\u0000\'\'09AZ__az\u000b\u0000!!#&*+--//:"+
		":<@\\\\^^||~~\u0001\u000009\u0003\u0000\t\n\r\r  T\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0001\u001d\u0001\u0000\u0000\u0000"+
		"\u0003\u001f\u0001\u0000\u0000\u0000\u0005#\u0001\u0000\u0000\u0000\u0007"+
		"\'\u0001\u0000\u0000\u0000\t)\u0001\u0000\u0000\u0000\u000b-\u0001\u0000"+
		"\u0000\u0000\r0\u0001\u0000\u0000\u0000\u000f2\u0001\u0000\u0000\u0000"+
		"\u00114\u0001\u0000\u0000\u0000\u00137\u0001\u0000\u0000\u0000\u00159"+
		"\u0001\u0000\u0000\u0000\u0017A\u0001\u0000\u0000\u0000\u0019F\u0001\u0000"+
		"\u0000\u0000\u001bK\u0001\u0000\u0000\u0000\u001d\u001e\u0005;\u0000\u0000"+
		"\u001e\u0002\u0001\u0000\u0000\u0000\u001f \u0005v\u0000\u0000 !\u0005"+
		"a\u0000\u0000!\"\u0005l\u0000\u0000\"\u0004\u0001\u0000\u0000\u0000#$"+
		"\u0005f\u0000\u0000$%\u0005u\u0000\u0000%&\u0005n\u0000\u0000&\u0006\u0001"+
		"\u0000\u0000\u0000\'(\u0005=\u0000\u0000(\b\u0001\u0000\u0000\u0000)*"+
		"\u0005a\u0000\u0000*+\u0005n\u0000\u0000+,\u0005d\u0000\u0000,\n\u0001"+
		"\u0000\u0000\u0000-.\u0005f\u0000\u0000./\u0005n\u0000\u0000/\f\u0001"+
		"\u0000\u0000\u000001\u0005(\u0000\u00001\u000e\u0001\u0000\u0000\u0000"+
		"23\u0005)\u0000\u00003\u0010\u0001\u0000\u0000\u000045\u0005=\u0000\u0000"+
		"56\u0005>\u0000\u00006\u0012\u0001\u0000\u0000\u000078\u0005|\u0000\u0000"+
		"8\u0014\u0001\u0000\u0000\u00009=\u0007\u0000\u0000\u0000:<\u0007\u0001"+
		"\u0000\u0000;:\u0001\u0000\u0000\u0000<?\u0001\u0000\u0000\u0000=;\u0001"+
		"\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>\u0016\u0001\u0000\u0000"+
		"\u0000?=\u0001\u0000\u0000\u0000@B\u0007\u0002\u0000\u0000A@\u0001\u0000"+
		"\u0000\u0000BC\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000CD\u0001"+
		"\u0000\u0000\u0000D\u0018\u0001\u0000\u0000\u0000EG\u0007\u0003\u0000"+
		"\u0000FE\u0001\u0000\u0000\u0000GH\u0001\u0000\u0000\u0000HF\u0001\u0000"+
		"\u0000\u0000HI\u0001\u0000\u0000\u0000I\u001a\u0001\u0000\u0000\u0000"+
		"JL\u0007\u0004\u0000\u0000KJ\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000"+
		"\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NO\u0001\u0000"+
		"\u0000\u0000OP\u0006\r\u0000\u0000P\u001c\u0001\u0000\u0000\u0000\u0005"+
		"\u0000=CHM\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}