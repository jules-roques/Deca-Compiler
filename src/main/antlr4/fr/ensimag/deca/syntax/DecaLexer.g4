lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

// Caractères spéciaux
OBRACE : '{';
CBRACE : '}';
SEMI : ';';
COMMA : ',';
EQUALS : '=';
OPARENT : '(';
CPARENT : ')';
PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';

// fonctions print
PRINT : 'print';
PRINTLN : 'println';
PRINTX : 'printx';
PRINTLNX : 'printlnx';

//fonctions read
READINT : 'readInt';
READFLOAT : 'readFloat';

// logique
IF : 'if';
ELSE : 'else';
OR : '||';
AND : '&&';
EQEQ :'==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
GT : '>';
LT : '<';
INSTANCEOF : 'instanceof';
TRUE : 'true';
FALSE : 'false';

// instructions de contrôle
WHILE : 'while';
RETURN : 'return';
NEW : 'new';
THIS : 'this';
CLASS : 'class';
EXTENDS : 'extends';
PROTECTED : 'protected';
ASM : 'asm';

// null
NULL : 'null';


// Fin de ligne
fragment EOL : '\n';

// Identificateurs
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT : '0' .. '9';
IDENT : (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;

// Littéraux entiers
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Littéraux flottants
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | /*epsilon*/;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | /*epsilon*/);
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | /*epsilon*/);
FLOAT : FLOATDEC | FLOATHEX;

// Chaînes de caractères
fragment STRING_CAR : ~('"' | '\\' | '\n');
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Commentaires (à ignorer grâce à skip)
COMMENT : '/*' .*? '*/' { skip(); };
ONE_LINE_COMMENT : '//' (~('\n'))* { skip(); };

// Separateurs: caractères de formatage (à ignorer grâce à skip)
WS : ( ' ' | '\t' | '\r' | EOL ) { skip(); };

// Inclusion de fichier
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"';