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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, INT_TYPE=16, 
		ARROW=17, PLUS=18, MINUS=19, STAR=20, DIV=21, MOD=22, ID=23, SYMBOLIC_ID=24, 
		INT=25, WS=26;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "INT_TYPE", "ARROW", 
			"PLUS", "MINUS", "STAR", "DIV", "MOD", "ID", "SYMBOLIC_ID", "SYMCHAR", 
			"INT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'='", "'('", "','", "')'", "'fn'", "'=>'", "'|'", 
			"'.'", "'val'", "'fun'", "'structure'", "'struct'", "'end'", "'int'", 
			"'->'", "'+'", "'-'", "'*'", "'div'", "'mod'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "INT_TYPE", "ARROW", "PLUS", "MINUS", "STAR", 
			"DIV", "MOD", "ID", "SYMBOLIC_ID", "INT", "WS"
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
		"\u0004\u0000\u001a\u0099\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0005\u0016\u0082\b\u0016\n\u0016\f\u0016\u0085\t\u0016\u0001\u0017\u0004"+
		"\u0017\u0088\b\u0017\u000b\u0017\f\u0017\u0089\u0001\u0018\u0001\u0018"+
		"\u0001\u0019\u0004\u0019\u008f\b\u0019\u000b\u0019\f\u0019\u0090\u0001"+
		"\u001a\u0004\u001a\u0094\b\u001a\u000b\u001a\f\u001a\u0095\u0001\u001a"+
		"\u0001\u001a\u0000\u0000\u001b\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013"+
		"\'\u0014)\u0015+\u0016-\u0017/\u00181\u00003\u00195\u001a\u0001\u0000"+
		"\u0005\u0002\u0000AZaz\u0005\u0000\'\'09AZ__az\t\u0000!!#&//::<@\\\\^"+
		"^||~~\u0001\u000009\u0003\u0000\t\n\r\r  \u009b\u0000\u0001\u0001\u0000"+
		"\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000"+
		"\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000"+
		"\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000"+
		"\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000"+
		"\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000"+
		"\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000"+
		"\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000"+
		"#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001"+
		"\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000"+
		"\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u0000"+
		"3\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000\u00017\u0001"+
		"\u0000\u0000\u0000\u00039\u0001\u0000\u0000\u0000\u0005;\u0001\u0000\u0000"+
		"\u0000\u0007=\u0001\u0000\u0000\u0000\t?\u0001\u0000\u0000\u0000\u000b"+
		"A\u0001\u0000\u0000\u0000\rC\u0001\u0000\u0000\u0000\u000fF\u0001\u0000"+
		"\u0000\u0000\u0011I\u0001\u0000\u0000\u0000\u0013K\u0001\u0000\u0000\u0000"+
		"\u0015M\u0001\u0000\u0000\u0000\u0017Q\u0001\u0000\u0000\u0000\u0019U"+
		"\u0001\u0000\u0000\u0000\u001b_\u0001\u0000\u0000\u0000\u001df\u0001\u0000"+
		"\u0000\u0000\u001fj\u0001\u0000\u0000\u0000!n\u0001\u0000\u0000\u0000"+
		"#q\u0001\u0000\u0000\u0000%s\u0001\u0000\u0000\u0000\'u\u0001\u0000\u0000"+
		"\u0000)w\u0001\u0000\u0000\u0000+{\u0001\u0000\u0000\u0000-\u007f\u0001"+
		"\u0000\u0000\u0000/\u0087\u0001\u0000\u0000\u00001\u008b\u0001\u0000\u0000"+
		"\u00003\u008e\u0001\u0000\u0000\u00005\u0093\u0001\u0000\u0000\u00007"+
		"8\u0005;\u0000\u00008\u0002\u0001\u0000\u0000\u00009:\u0005:\u0000\u0000"+
		":\u0004\u0001\u0000\u0000\u0000;<\u0005=\u0000\u0000<\u0006\u0001\u0000"+
		"\u0000\u0000=>\u0005(\u0000\u0000>\b\u0001\u0000\u0000\u0000?@\u0005,"+
		"\u0000\u0000@\n\u0001\u0000\u0000\u0000AB\u0005)\u0000\u0000B\f\u0001"+
		"\u0000\u0000\u0000CD\u0005f\u0000\u0000DE\u0005n\u0000\u0000E\u000e\u0001"+
		"\u0000\u0000\u0000FG\u0005=\u0000\u0000GH\u0005>\u0000\u0000H\u0010\u0001"+
		"\u0000\u0000\u0000IJ\u0005|\u0000\u0000J\u0012\u0001\u0000\u0000\u0000"+
		"KL\u0005.\u0000\u0000L\u0014\u0001\u0000\u0000\u0000MN\u0005v\u0000\u0000"+
		"NO\u0005a\u0000\u0000OP\u0005l\u0000\u0000P\u0016\u0001\u0000\u0000\u0000"+
		"QR\u0005f\u0000\u0000RS\u0005u\u0000\u0000ST\u0005n\u0000\u0000T\u0018"+
		"\u0001\u0000\u0000\u0000UV\u0005s\u0000\u0000VW\u0005t\u0000\u0000WX\u0005"+
		"r\u0000\u0000XY\u0005u\u0000\u0000YZ\u0005c\u0000\u0000Z[\u0005t\u0000"+
		"\u0000[\\\u0005u\u0000\u0000\\]\u0005r\u0000\u0000]^\u0005e\u0000\u0000"+
		"^\u001a\u0001\u0000\u0000\u0000_`\u0005s\u0000\u0000`a\u0005t\u0000\u0000"+
		"ab\u0005r\u0000\u0000bc\u0005u\u0000\u0000cd\u0005c\u0000\u0000de\u0005"+
		"t\u0000\u0000e\u001c\u0001\u0000\u0000\u0000fg\u0005e\u0000\u0000gh\u0005"+
		"n\u0000\u0000hi\u0005d\u0000\u0000i\u001e\u0001\u0000\u0000\u0000jk\u0005"+
		"i\u0000\u0000kl\u0005n\u0000\u0000lm\u0005t\u0000\u0000m \u0001\u0000"+
		"\u0000\u0000no\u0005-\u0000\u0000op\u0005>\u0000\u0000p\"\u0001\u0000"+
		"\u0000\u0000qr\u0005+\u0000\u0000r$\u0001\u0000\u0000\u0000st\u0005-\u0000"+
		"\u0000t&\u0001\u0000\u0000\u0000uv\u0005*\u0000\u0000v(\u0001\u0000\u0000"+
		"\u0000wx\u0005d\u0000\u0000xy\u0005i\u0000\u0000yz\u0005v\u0000\u0000"+
		"z*\u0001\u0000\u0000\u0000{|\u0005m\u0000\u0000|}\u0005o\u0000\u0000}"+
		"~\u0005d\u0000\u0000~,\u0001\u0000\u0000\u0000\u007f\u0083\u0007\u0000"+
		"\u0000\u0000\u0080\u0082\u0007\u0001\u0000\u0000\u0081\u0080\u0001\u0000"+
		"\u0000\u0000\u0082\u0085\u0001\u0000\u0000\u0000\u0083\u0081\u0001\u0000"+
		"\u0000\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084.\u0001\u0000\u0000"+
		"\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0086\u0088\u00031\u0018\u0000"+
		"\u0087\u0086\u0001\u0000\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000"+
		"\u0089\u0087\u0001\u0000\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000"+
		"\u008a0\u0001\u0000\u0000\u0000\u008b\u008c\u0007\u0002\u0000\u0000\u008c"+
		"2\u0001\u0000\u0000\u0000\u008d\u008f\u0007\u0003\u0000\u0000\u008e\u008d"+
		"\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000\u0000\u0090\u008e"+
		"\u0001\u0000\u0000\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u00914\u0001"+
		"\u0000\u0000\u0000\u0092\u0094\u0007\u0004\u0000\u0000\u0093\u0092\u0001"+
		"\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000\u0000\u0095\u0093\u0001"+
		"\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096\u0097\u0001"+
		"\u0000\u0000\u0000\u0097\u0098\u0006\u001a\u0000\u0000\u00986\u0001\u0000"+
		"\u0000\u0000\u0005\u0000\u0083\u0089\u0090\u0095\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}