lexer grammar FriggaLexer;

WHITESPACE: [ \t\r\n] -> skip;
NEWLINE: ('\r'? '\n' | '\r') ;

//Literals
INT: [0-9]+;
DEC: INT '.' INT ;
BOOL: 'true' | 'false';
CHAR: '\'' (. | '\\' .) '\'';
STRING: QUOTE ( '\\"' | . )*? QUOTE;

// Keywords
MUTABLE: 'mutable';
STATEFUL: 'stateful';
SECRET: 'secret';
STATIC: 'static';
USE: 'use';
NAMESPACE: 'namespace';

//Operators
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
POWER: '^';
INVERSE: '!';
ASSIGN: '=';
COMMA: ',';
LPAREN: '(';
RPAREN: ')';
LSPAREN: '[';
RSPAREN: ']';
LCPAREN: '{';
RCPAREN: '}';
COLON: ':';
DOT: '.';
DOUBLE_COLON: COLON COLON;
ARROW: '->';
QUOTE: '"';
EQUAL: '==';
MORE_THAN: '>';
MORE_EQUAL_THAN: '>=';
LESS_EQUAL_THAN: '<=';
LESS_THAN: '<';
COMMENT: '#' ~('\r'|'\n')* -> skip;
MULTI_COMMENT: '##' .*? '##' -> skip;
BACKSLASH: '\\';
NOTHING: '_';

//Identifier
ID: [a-zA-Z_0-9]+;
NAMESPACE_TEXT: QUOTE [a-z/]+ QUOTE;


ERROR_CHAR: .;
