grammar SimpleGremmar;

// Parser rules
prog:   stat+ ;
stat:   expr NEWLINE
    |   ID '=' expr NEWLINE
    |   NEWLINE
    ;
expr:   expr ('*'|'/') expr
    |   expr ('+'|'-') expr
    |   INT
    |   ID
    |   '(' expr ')'
    ;

// Lexer rules
ID  :   [a-zA-Z]+ ;
INT :   [0-9]+ ;
NEWLINE:'\r'? '\n' ;
WS  :   [ \t]+ -> skip ;