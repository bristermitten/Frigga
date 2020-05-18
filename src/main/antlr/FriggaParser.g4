parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

friggaFile : lines=line* EOF;

line: expression NEWLINE*?;

expression:
         ID #varReference
         | literal #literalExpression
         | functionCall #functionCallExpression
         | left = expression operator=(DIVIDE|ASTERISK|PLUS|MINUS|POWER) right=expression #binaryOperation
         | INVERSE expression #inverseOperation
//         | LPAREN expression RPAREN # parenExpression
         |functionDecl #functionDeclarationExpression
         |assignment #assignmentExpression
         ;

literal:
      (MINUS? INT) #intLiteral
    | (MINUS? DOUBLE) #decLiteral
    | BOOL #boolLiteral
    | (STRING) #stringLiteral;

objectRef: ID DOT;
functionCall: (objectRef)? ID LPAREN functionParams RPAREN;

functionParams: (expression COMMA?)*;

functionSignature: (parameterDefinition COMMA?)+ ARROW ID;
parameterDefinition: (ID typeSpec) | (ID);

functionDecl: functionSignature? LCPAREN (NEWLINE*) line* RCPAREN;

typeSpec: TYPEDECL ID;

assignment: ID typeSpec? ASSIGN expression;

