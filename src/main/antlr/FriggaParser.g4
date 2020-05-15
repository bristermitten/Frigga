parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

friggaFile : lines=line* EOF;

line: (expression|statement) NEWLINE*?;

expression:
         ID #varReference
         | functionCall #functionExpression
         | literal #literalExpression
         |left = expression operator=(DIVIDE|ASTERISK|PLUS|MINUS|POWER) right=expression #binaryOperation
         |left = expression operator=(PLUS|MINUS) right=expression #binaryOperation
//         | LPAREN expression RPAREN # parenExpression
         |functionDecl #functionDeclarationExpression
         ;

objectRef: (ID DOT);
functionCall: objectRef? ID LPAREN functionParams RPAREN;

functionParams: (expression COMMA?)*;

statement:  assignment #assignmentStatement;

functionSignature: (parameterDefinition COMMA?)+ ARROW ID;
parameterDefinition: (ID typeSpec) | (ID);

functionDecl: functionSignature? LCPAREN (NEWLINE*) line* RCPAREN;

typeSpec: TYPEDECL ID;

assignment: ID typeSpec? ASSIGN expression;

literal:     INTLIT #intLiteral
            | DECLIT #decLiteral;

