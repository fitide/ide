parser grammar TestCdmParser;

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

program : instructionWithArg* NEWLINE+ End ;

instructionWithArg: instruction arguments? ;

arguments : argument (COMMA argument)* ;

argument
    : character
    | string
    | register
    | addr_expr
    | byte_expr
    ;

byte_expr : byte_specifier OPEN_PAREN addr_expr CLOSE_PAREN ;
addr_expr : first_term add_term* ;
first_term : (PLUS | MINUS)? term ;
add_term : (PLUS | MINUS) term ;
term : number | label ;
byte_specifier : name;

label : name ;
instruction : WORD ;
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