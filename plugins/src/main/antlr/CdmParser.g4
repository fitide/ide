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

program : NEWLINE* section* End ;

section
    :  asect_header code_block #absoluteSection
    |  rsect_header code_block #relocatableSection
    | tplate_header code_block #templateSection
    ;

asect_header  :  Asect number NEWLINE+ ;
rsect_header  :  Rsect name   NEWLINE+ ;
tplate_header : Tplate name   NEWLINE+ ;


//macros
macro : macro_header macro_body MACRO_FOOTER ;
macro_header : WS? Macro WS? WORD WS? SLASH WS? DECIMAL_NUMBER WS? NEWLINE ;
macro_body : ((WS? NEWLINE) | macro_line)*? ;

macro_line :macro_labels (macro_instruction macro_first_param)? (COMMA macro_param)* NEWLINE;

macro_labels : macro_label* WS? ;
macro_first_param : WS macro_param | ;

macro_label :       (macro_piece | macro_variable+ macro_l_sep | macro_l_sep)* macro_variable* LABEL_END ;
macro_param :       (macro_piece | macro_variable+ macro_p_sep | macro_p_sep)* macro_variable* ;
macro_instruction : (macro_piece | macro_variable+ OTHER | OTHER)+ macro_variable* | macro_variable+ ;
macro_l_sep : OTHER | WS | COMMA ;
macro_p_sep : OTHER | WS ;

macro_piece : macro_text | macro_param_sign | macro_nonce ;
macro_variable : QUESTION_MARK macro_piece+ ;
macro_text : Macro | WORD | DECIMAL_NUMBER | STRING | CHAR ;
macro_param_sign : DOLLAR_SIGN DECIMAL_NUMBER ;
macro_nonce : APOSTROPHE;


code_block
    :
    ( break_statement
    | continue_statement
    | line
    | conditional
    | while_loop
    | until_loop
    )*
    ;

break_statement : Break NEWLINE+ ;
continue_statement : Continue NEWLINE+ ;

line
    : labels_declaration Ext? NEWLINE+                    # standaloneLabels
    | labels_declaration? instructionWithArg NEWLINE+ # instructionLine
    ;

instructionWithArg: WORD arguments? ;

labels_declaration: labels (COLON | ANGLE_BRACKET) ;
labels: name (COMMA name)*;
arguments : argument (COMMA argument)* ;

conditional : If NEWLINE+ conditions code_block else_clause? Fi NEWLINE+ ;
conditions : connective_condition* condition NEWLINE+ (Then NEWLINE+)? ;
connective_condition : condition COMMA WORD NEWLINE+ ;
condition : code_block Is WORD ;
else_clause : Else NEWLINE+ code_block ;


while_loop : While NEWLINE+ code_block Stays WORD NEWLINE+ code_block Wend NEWLINE+ ;

until_loop : Do NEWLINE+ code_block Until WORD NEWLINE+ ;

argument
    : character
    | string
    | register
    | addr_expr
    | byte_expr
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