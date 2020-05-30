parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

friggaFile : line* EOF;

line: expression NEWLINE*?;

expression:
         ID #varReference
         | literal #literalExpression
         | functionCall #functionCallExpression
         | left = expression operator=(DIVIDE|ASTERISK|PLUS|MINUS|POWER|EQUAL) right=expression #binaryOperation
         | INVERSE expression #inverseOperation
         | functionDecl #functionDeclarationExpression
         | assignment #assignmentExpression
         | paranthesisExpression # parenExpression
         ;

paranthesisExpression: LPAREN expression RPAREN;

literal:
      (MINUS? INT) #intLiteral
    | (MINUS? DOUBLE) #decLiteral
    | BOOL #boolLiteral
    | (STRING) #stringLiteral;

objectRef: ID DOT;
functionCall: (objectRef)? ID LPAREN functionParams RPAREN;

functionParams: (expression COMMA?)*;


functionDecl: functionSignature? LCPAREN (NEWLINE*) line* RCPAREN; //value::Any -> _ { print(value) }
functionSignature: (LPAREN? (parameterDefinition COMMA?)* RPAREN?) ARROW ID; //value::Any, other::Any -> _
parameterDefinition: (ID typeSpec) | (ID); //value::Any

typeSpec: DOUBLE_COLON ID; //::Any

assignment: ID typeSpec? ASSIGN expression;

