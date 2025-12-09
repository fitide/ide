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

program_nomacros : NEWLINE* top_line* section* End ;

program : NEWLINE* line_mark+ top_line* section* End ;

section
    :  asect_header section_body # absoluteSection
    |  rsect_header section_body # relocatableSection
    | tplate_header section_body # templateSection
    ;

asect_header  :  Asect number NEWLINE+ ;
rsect_header  :  Rsect name   NEWLINE+ ;
tplate_header : Tplate name   NEWLINE+ ;

section_body : code_block ;
code_block
    :
    ( break_statement
    | continue_statement
    | line
    | conditional
    | while_loop
    | until_loop
    | line_mark
    )*
    ;

line_mark
    : LINE_MARK_MARKER line_number filepath WORD? NEWLINE+
      {
        currentLine = Integer.parseInt($line_number.text);
        String encoded = $filepath.text.substring(3);
        currentFile = new String(Base64.getDecoder().decode(encoded));
        currentOffset = $line_number.start.getLine() - currentLine + 1;
      }
    ;

line_number: DECIMAL_NUMBER;
filepath: BASE64;

break_statement : Break NEWLINE+ ;
continue_statement : Continue NEWLINE+ ;

top_line: line;

line
    : labels_declaration Ext? NEWLINE+                    # standaloneLabels
    | labels_declaration? instructionWithArg NEWLINE+ # instructionLine
    ;

instructionWithArg: instruction arguments? ;

labels_declaration: labels (COLON | ANGLE_BRACKET) ;
labels: label (COMMA label)*;
arguments : argument (COMMA argument)* ;

conditional : If NEWLINE+ conditions code_block else_clause? Fi NEWLINE+ ;
conditions : connective_condition* condition NEWLINE+ (Then NEWLINE+)? ;
connective_condition : condition COMMA conjunction NEWLINE+ ;
condition : code_block Is branch_mnemonic ;
else_clause : Else NEWLINE+ code_block ;

branch_mnemonic : WORD ;
conjunction : WORD ;

while_loop : While NEWLINE+ while_condition Stays branch_mnemonic NEWLINE+ code_block Wend NEWLINE+ ;
while_condition : code_block ;

until_loop : Do NEWLINE+ code_block Until branch_mnemonic NEWLINE+ ;

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