parser grammar CdmParser;

options { tokenVocab=CdmLexer; }

@header {
import java.util.Base64;
}

@members {
    private String currentFile = "";
    private int currentLine = 0;
    private int currentOffset = 0;

    public String getCurrentFile() { return currentFile; }
    public int getCurrentLine() { return currentLine; }
    public int getCurrentOffset() { return currentOffset; }
}

program : NEWLINE* (section NEWLINE*)* End ;

section
    :  asect_header code_block #absoluteSection
    |  rsect_header code_block #relocatableSection
    | tplate_header code_block #templateSection
    | macro  #macroSection
    ;

asect_header  :  Asect number NEWLINE+ ;
rsect_header  :  Rsect name   NEWLINE+ ;
tplate_header : Tplate name   NEWLINE+ ;


//macros
macro : macro_header code_block MACRO_FOOTER ;
macro_header : WS? Macro WS? WORD WS? SLASH WS? DECIMAL_NUMBER WS? NEWLINE ;

code_block
    :
    ( break_statement
    | continue_statement
    | line
    | conditional
    | while_loop
    | until_loop
    | macro
    )*
    ;

break_statement : Break NEWLINE+ ;
continue_statement : Continue NEWLINE+ ;

line
    : labels_declaration Ext? NEWLINE+                    # standaloneLabels
    | labels_declaration? instructionWithArg NEWLINE+ # instructionLine
    ;

instructionWithArg: WORD arguments? ;

labels_declaration: label (COLON | ANGLE_BRACKET) ;
label: name;
arguments : argument (COMMA argument)* ;

branch_mnemonic : WORD ;

conditional : If NEWLINE+ conditions code_block else_clause? Fi NEWLINE+ ;
conditions : connective_condition* condition NEWLINE+ (Then NEWLINE+)? ;
connective_condition : condition COMMA WORD NEWLINE+ ;
condition : code_block Is branch_mnemonic ;
else_clause : Else NEWLINE+ code_block ;


while_loop : While NEWLINE+ code_block Stays branch_mnemonic NEWLINE+ code_block Wend NEWLINE+ ;

until_loop : Do NEWLINE+ code_block Until branch_mnemonic NEWLINE+ ;

argument
    : '$'?
    (character
    | string
    | register
    | addr_expr
    | byte_expr)
    ;

byte_expr : name OPEN_PAREN addr_expr CLOSE_PAREN ;
addr_expr : first_term add_term* ;
first_term : (PLUS | MINUS)? term ;
add_term : (PLUS | MINUS) term ;
term : number | name ;

string : STRING ;
register : REGISTER ;
character : CHAR ;
number
    : DECIMAL_NUMBER
    | HEX_NUMBER
    | BINARY_NUMBER
    ;

name
    : Asect
    | Break
    | Continue
    | Do
    | Else
    | End
    | Ext
    | Fi
    | If
    | Is
    | Macro
    | Rsect
    | Stays
    | Then
    | Tplate
    | Until
    | Wend
    | While
    | WORD
    | WORD_WITH_DOTS
    ;