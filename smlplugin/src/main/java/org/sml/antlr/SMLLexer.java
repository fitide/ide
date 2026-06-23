package org.sml.antlr;// Generated from SML.g4 by ANTLR 4.13.1

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class SMLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, INFIX=16, INT_TYPE=17, 
		ARROW=18, PLUS=19, MINUS=20, STAR=21, DIV=22, MOD=23, ID=24, SYMBOLIC_ID=25, 
		INT=26, WS=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "INFIX", "INT_TYPE", 
			"ARROW", "PLUS", "MINUS", "STAR", "DIV", "MOD", "ID", "SYMBOLIC_ID", 
			"SYMCHAR", "INT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'='", "'('", "','", "')'", "'fn'", "'=>'", "'|'", 
			"'.'", "'val'", "'fun'", "'structure'", "'struct'", "'end'", "'infix'", 
			"'int'", "'->'", "'+'", "'-'", "'*'", "'div'", "'mod'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "INFIX", "INT_TYPE", "ARROW", "PLUS", "MINUS", 
			"STAR", "DIV", "MOD", "ID", "SYMBOLIC_ID", "INT", "WS"
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
		"\u0004\u0000\u001b\u00a1\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0002\u001b\u0007\u001b\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0017\u0001\u0017\u0005\u0017\u008a\b\u0017\n\u0017\f\u0017\u008d"+
		"\t\u0017\u0001\u0018\u0004\u0018\u0090\b\u0018\u000b\u0018\f\u0018\u0091"+
		"\u0001\u0019\u0001\u0019\u0001\u001a\u0004\u001a\u0097\b\u001a\u000b\u001a"+
		"\f\u001a\u0098\u0001\u001b\u0004\u001b\u009c\b\u001b\u000b\u001b\f\u001b"+
		"\u009d\u0001\u001b\u0001\u001b\u0000\u0000\u001c\u0001\u0001\u0003\u0002"+
		"\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"+
		"\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"+
		"#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u00005\u001a"+
		"7\u001b\u0001\u0000\u0005\u0002\u0000AZaz\u0005\u0000\'\'09AZ__az\t\u0000"+
		"!!#&//::<@\\\\^^||~~\u0001\u000009\u0003\u0000\t\n\r\r  \u00a3\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000"+
		"\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000"+
		"\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000"+
		"\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000"+
		"\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000"+
		"\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000"+
		"\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000"+
		"\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001"+
		"\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000"+
		"\u0000\u00001\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u0000"+
		"7\u0001\u0000\u0000\u0000\u00019\u0001\u0000\u0000\u0000\u0003;\u0001"+
		"\u0000\u0000\u0000\u0005=\u0001\u0000\u0000\u0000\u0007?\u0001\u0000\u0000"+
		"\u0000\tA\u0001\u0000\u0000\u0000\u000bC\u0001\u0000\u0000\u0000\rE\u0001"+
		"\u0000\u0000\u0000\u000fH\u0001\u0000\u0000\u0000\u0011K\u0001\u0000\u0000"+
		"\u0000\u0013M\u0001\u0000\u0000\u0000\u0015O\u0001\u0000\u0000\u0000\u0017"+
		"S\u0001\u0000\u0000\u0000\u0019W\u0001\u0000\u0000\u0000\u001ba\u0001"+
		"\u0000\u0000\u0000\u001dh\u0001\u0000\u0000\u0000\u001fl\u0001\u0000\u0000"+
		"\u0000!r\u0001\u0000\u0000\u0000#v\u0001\u0000\u0000\u0000%y\u0001\u0000"+
		"\u0000\u0000\'{\u0001\u0000\u0000\u0000)}\u0001\u0000\u0000\u0000+\u007f"+
		"\u0001\u0000\u0000\u0000-\u0083\u0001\u0000\u0000\u0000/\u0087\u0001\u0000"+
		"\u0000\u00001\u008f\u0001\u0000\u0000\u00003\u0093\u0001\u0000\u0000\u0000"+
		"5\u0096\u0001\u0000\u0000\u00007\u009b\u0001\u0000\u0000\u00009:\u0005"+
		";\u0000\u0000:\u0002\u0001\u0000\u0000\u0000;<\u0005:\u0000\u0000<\u0004"+
		"\u0001\u0000\u0000\u0000=>\u0005=\u0000\u0000>\u0006\u0001\u0000\u0000"+
		"\u0000?@\u0005(\u0000\u0000@\b\u0001\u0000\u0000\u0000AB\u0005,\u0000"+
		"\u0000B\n\u0001\u0000\u0000\u0000CD\u0005)\u0000\u0000D\f\u0001\u0000"+
		"\u0000\u0000EF\u0005f\u0000\u0000FG\u0005n\u0000\u0000G\u000e\u0001\u0000"+
		"\u0000\u0000HI\u0005=\u0000\u0000IJ\u0005>\u0000\u0000J\u0010\u0001\u0000"+
		"\u0000\u0000KL\u0005|\u0000\u0000L\u0012\u0001\u0000\u0000\u0000MN\u0005"+
		".\u0000\u0000N\u0014\u0001\u0000\u0000\u0000OP\u0005v\u0000\u0000PQ\u0005"+
		"a\u0000\u0000QR\u0005l\u0000\u0000R\u0016\u0001\u0000\u0000\u0000ST\u0005"+
		"f\u0000\u0000TU\u0005u\u0000\u0000UV\u0005n\u0000\u0000V\u0018\u0001\u0000"+
		"\u0000\u0000WX\u0005s\u0000\u0000XY\u0005t\u0000\u0000YZ\u0005r\u0000"+
		"\u0000Z[\u0005u\u0000\u0000[\\\u0005c\u0000\u0000\\]\u0005t\u0000\u0000"+
		"]^\u0005u\u0000\u0000^_\u0005r\u0000\u0000_`\u0005e\u0000\u0000`\u001a"+
		"\u0001\u0000\u0000\u0000ab\u0005s\u0000\u0000bc\u0005t\u0000\u0000cd\u0005"+
		"r\u0000\u0000de\u0005u\u0000\u0000ef\u0005c\u0000\u0000fg\u0005t\u0000"+
		"\u0000g\u001c\u0001\u0000\u0000\u0000hi\u0005e\u0000\u0000ij\u0005n\u0000"+
		"\u0000jk\u0005d\u0000\u0000k\u001e\u0001\u0000\u0000\u0000lm\u0005i\u0000"+
		"\u0000mn\u0005n\u0000\u0000no\u0005f\u0000\u0000op\u0005i\u0000\u0000"+
		"pq\u0005x\u0000\u0000q \u0001\u0000\u0000\u0000rs\u0005i\u0000\u0000s"+
		"t\u0005n\u0000\u0000tu\u0005t\u0000\u0000u\"\u0001\u0000\u0000\u0000v"+
		"w\u0005-\u0000\u0000wx\u0005>\u0000\u0000x$\u0001\u0000\u0000\u0000yz"+
		"\u0005+\u0000\u0000z&\u0001\u0000\u0000\u0000{|\u0005-\u0000\u0000|(\u0001"+
		"\u0000\u0000\u0000}~\u0005*\u0000\u0000~*\u0001\u0000\u0000\u0000\u007f"+
		"\u0080\u0005d\u0000\u0000\u0080\u0081\u0005i\u0000\u0000\u0081\u0082\u0005"+
		"v\u0000\u0000\u0082,\u0001\u0000\u0000\u0000\u0083\u0084\u0005m\u0000"+
		"\u0000\u0084\u0085\u0005o\u0000\u0000\u0085\u0086\u0005d\u0000\u0000\u0086"+
		".\u0001\u0000\u0000\u0000\u0087\u008b\u0007\u0000\u0000\u0000\u0088\u008a"+
		"\u0007\u0001\u0000\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u008a\u008d"+
		"\u0001\u0000\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008c"+
		"\u0001\u0000\u0000\u0000\u008c0\u0001\u0000\u0000\u0000\u008d\u008b\u0001"+
		"\u0000\u0000\u0000\u008e\u0090\u00033\u0019\u0000\u008f\u008e\u0001\u0000"+
		"\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000"+
		"\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u00922\u0001\u0000\u0000"+
		"\u0000\u0093\u0094\u0007\u0002\u0000\u0000\u00944\u0001\u0000\u0000\u0000"+
		"\u0095\u0097\u0007\u0003\u0000\u0000\u0096\u0095\u0001\u0000\u0000\u0000"+
		"\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000\u0000"+
		"\u0098\u0099\u0001\u0000\u0000\u0000\u00996\u0001\u0000\u0000\u0000\u009a"+
		"\u009c\u0007\u0004\u0000\u0000\u009b\u009a\u0001\u0000\u0000\u0000\u009c"+
		"\u009d\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009d"+
		"\u009e\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000\u009f"+
		"\u00a0\u0006\u001b\u0000\u0000\u00a08\u0001\u0000\u0000\u0000\u0005\u0000"+
		"\u008b\u0091\u0098\u009d\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}