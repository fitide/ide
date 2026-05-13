grammar SML;

prog
    : dec (';' dec)* ';'?
    ;

dec
    : 'val' valbind
    | 'fun' funbind
    ;

valbind
    : pat '=' exp
    ;

funbind
    : funmatch ('and' funmatch)*
    ;

funmatch
    : ID pat+ '=' exp
    ;

pat
    : ID
    ;

exp
    : <assoc=left> exp exp   # ApplicationExp
    | 'fn' match                     # FnExp
    | '(' exp ')'                         # ParensExp
    | INT                                 # IntExp
    | ID                                  # IdExp
    | SYMBOLIC_ID                         # SymbolicExp
    ;

match
    : pat '=>' exp ('|' pat '=>' exp)*
    ;

ID
    : [a-zA-Z] [a-zA-Z0-9'_]*
    ;

SYMBOLIC_ID
    : [!%&$#+\-/:<>=?@\\~^|*]+
    ;

INT
    : [0-9]+
    ;

WS
    : [ \t\r\n]+ -> skip
    ;