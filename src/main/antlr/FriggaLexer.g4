lexer grammar FriggaLexer;

Whitespace:  [ \t\r\n] -> skip;
NEWLINE: ('\r'? '\n' | '\r') ;

// Keywords
MUTABLE: 'mutable';
STATEFUL: 'stateful';
SECRET: 'secret';
STATIC: 'static';

//Literals
INTLIT: '0'|[1-9][0-9]* ;
DECLIT: INTLIT '.' INTLIT ;

//Operators
PLUS: '+';
MINUS: '-';
ASTERISK: '*';
DIVIDE: '/';
POWER: '^';
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

//Identifier
ID: [a-zA-Z0-9]+;
