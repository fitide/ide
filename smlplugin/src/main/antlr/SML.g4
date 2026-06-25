grammar SML;

prog
    : dec (';' dec)* ';'?
    ;

dec
    : val valbind # ValDec
    | fun funbind # FunDec
    | structure structbind # StructDec
    | INFIX infixarg # InfixId
    ;

infixarg
    : ID;

INFIX
    : 'infix';

valbind
    : pat (':' typ)? '=' exp
    ;

funbind
    : ID pat+ (':' typ)? '=' exp
    ;

structbind
    : ID '=' struct_ decs end
    ;

decs
    : dec (';' dec)* ';'?
    ;

pat
    : ID
    | '(' pat (',' pat)+ ')'
    | '(' pat ')'
    ;

exp
    : 'fn' match                        # FnExp
    | <assoc=left> exp arg+               # ApplicationExp
    | <assoc=left> exp mulOp exp         # MulExp
    | <assoc=left> exp addOp exp         # AddExp
    | tuple                             # TupleExp
    | '(' exp ')'                       # ParensExp
    | INT                               # IntExp
    | ID                                # IdExp
    | SYMBOLIC_ID                       # SymbolicExp
    | longid                            # LongIdExp
    ;

arg :
    tuple                             # TupleArg
    | '(' exp ')'                       # ParensArg
    | INT                               # IntArg
    | ID                                # IdArg
    | SYMBOLIC_ID                       # SymbolicArg
    | longid                            # LongIdArg
    ;

mulOp
    : STAR
    | DIV
    | MOD
    ;

addOp
    : PLUS
    | MINUS
    ;


tuple
    : '(' exp (',' exp)+ ')'
    ;

match
    : pat '=>' exp ('|' pat '=>' exp)*
    ;

longid
    : ID ('.' ID)+
    ;

typ
    : INT_TYPE                          # IntType
    | '(' typ ')'                       # ParensType
    | '(' typ (',' typ)+ ')'            # TupleType
    | <assoc=right> typ ARROW typ       # FunType
    ;


val : 'val' ;
fun : 'fun' ;
structure : 'structure' ;
struct_ : 'struct' ;
end : 'end' ;
INT_TYPE : 'int' ;
ARROW : '->' ;

PLUS  : '+' ;
MINUS : '-' ;
STAR  : '*' ;
DIV   : 'div' ;
MOD   : 'mod' ;

ID
    : [a-zA-Z] [a-zA-Z0-9'_]*
    ;

SYMBOLIC_ID
    : SYMCHAR+
    ;

fragment SYMCHAR
    : [!%&$#/:<>=?@\\~^|]
    ;

INT
    : [0-9]+
    ;

WS
    : [ \t\r\n]+ -> skip
    ;
