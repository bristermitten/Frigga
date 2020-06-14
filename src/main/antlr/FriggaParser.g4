parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

friggaFile:
    headers?
    body EOF;

//Headers
use: USE STRING;
headers:
    use+?;

//Body
body: line+?;
line: expression+? NEWLINE*?;


expression:
           ID #varReference
         | assignment #assignmentExpression
         | lambda #lambdaExpression
         | call #callExpression
         | referencedCall #referencedCallExpression
         | literal #literalExpression
         | access #accessExpression
         | expression operator=(PLUS | MINUS | TIMES | DIVIDE ) expression #binaryOperator
         | expression operator=(EQUAL | MORE_THAN | MORE_EQUAL_THAN | LESS_EQUAL_THAN | LESS_THAN ) expression #binaryLogicalOperator
        ;

         assignment: ID typeSpec? ASSIGN expression;
         block: LCPAREN body RCPAREN;
         call: LPAREN args RPAREN;
         referencedCall: LSPAREN args RSPAREN;

         args: (expression COMMA?)*;
         access: DOT expression;
         typeSpec: DOUBLE_COLON type;

         type: functionType | ID | NOTHING | tuple;

         tuple: (LPAREN (typeParam) (COMMA typeParam)* RPAREN);
         tupleParam : ID typeSpec;

/*
Generics
*/
generic: LESS_THAN typeParam (COMMA typeParam)* MORE_THAN;
typeParam: (ID typeSpec) | ID;
/*
Functions
*/
lambda: generic? (functionParams ARROW type? (expression | block)) | block; //{} OR (a::Int) -> Int OR (a::Int) -> Int {}
functionParams: (LPAREN functionParam? (COMMA functionParam)* RPAREN); //(a::Int) OR (a)
functionParam: (ID typeSpec) | ID; //a::Int OR a

/*
Function Types
*/
functionType: (functionParamTypes) ARROW type;
functionParamTypes: (LPAREN type? (COMMA type)* RPAREN);

literal:
      MINUS? INT #intLiteral
    | MINUS? DOUBLE #decLiteral
    | BOOL #boolLiteral
    | STRING #stringLiteral
    | CHAR #charLiteral;

