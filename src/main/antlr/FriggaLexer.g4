lexer grammar FriggaLexer;

Whitespace:  [ \t\r\n] -> skip;
NEWLINE: ('\r'? '\n' | '\r') ;

// Keywords
MUTABLE: 'mutable';
STATEFUL: 'stateful';
SECRET: 'secret';
STATIC: 'static';

//Literals
INT: [0-9]+;
DOUBLE: INT '.' INT ;
BOOL: 'true' | 'false';

//Operators
PLUS: '+';
MINUS: '-';
ASTERISK: '*';
DIVIDE: '/';
POWER: '^';
INVERSE: '!';
ASSIGN: '=';
LPAREN: '(';
RPAREN: ')';
COMMA: ',';
LCPAREN: '{';
RCPAREN: '}';
COLON: ':';
DOT: '.';
TYPEDECL: COLON COLON;
ARROW: '->';
QUOTE: '"';

//Identifier
ID: [a-zA-Z0-9]+;
STRING : '"' ( '\\"' | . )*? '"' ;
