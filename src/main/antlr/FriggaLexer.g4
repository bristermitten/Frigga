lexer grammar FriggaLexer;

WHITESPACE: [ \t]+ -> skip;

fragment SEMICOLON : ';';
fragment NEWLINE   : '\r' '\n' | '\n' | '\r';
ENDLINE : SEMICOLON NEWLINE* | NEWLINE+;

COMMENT: '#' ~[\r\n]* -> skip;
MULTI_COMMENT: '#=' .*? '=#' -> skip;

//Numbers
fragment Digit: '0'..'9';
fragment HexDigit: [0-9A-F];
ScientificNotation: 'E' [+-];

//Literals

IntLiteral: MINUS? AbsoluteIntLiteral;
AbsoluteIntLiteral: Digit+ (ScientificNotation Digit+)?;
DecLiteral: IntLiteral '.' AbsoluteIntLiteral ;
BoolLiteral: 'true' | 'false';
CharLiteral: '\'' (. | '\\' .) '\'';
StringLiteral: QUOTE ( '\\"' | . )*? QUOTE;

// Keywords
MUTABLE: 'mutable';
STATEFUL: 'stateful';
SECRET: 'secret';
NATIVE: 'native';
USE: 'use';
NAMESPACE: 'namespace';
TRAIT: 'trait';
STRUCT: 'struct';
JVM: 'JVM';

//Symbols
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LSPAREN: '[';
RSPAREN: ']';
LCPAREN: '{';
RCPAREN: '}';
COLON: ':';
DOT: '.';

//Identifier
ID: [a-zA-Z_0-9]+;

//Operators
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
POWER: '^';
NOT: '!';
BIN_NOT: '~';
ASSIGN: '=';


DOUBLE_COLON: COLON COLON;
ARROW: '->';
QUOTE: '"';
EQUAL: '==';
MORE_THAN: '>';
MORE_EQUAL_THAN: '>=';
LESS_EQUAL_THAN: '<=';
LESS_THAN: '<';
BACKSLASH: '\\';
UNDERSCORE: '_';
AT: '@';

ERROR_CHAR: .;
