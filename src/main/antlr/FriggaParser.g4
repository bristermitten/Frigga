parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

friggaFile:
    namespace?
    usingList
    body
    EOF;

//Headers
use: USE STRING;
namespace: NAMESPACE STRING;

usingList:
    (use)*;

//Body
body: line+? | NEWLINE*;

line: expression NEWLINE*?;

expression:
           assignment #assignmentExpression
         |  literal #literalExpression
         | function #functionExpression
         | lambda #lambdaExpression
         | expression call #callExpression //something()
         | left=expression operator=(PLUS | MINUS | TIMES | DIVIDE | POWER | EQUAL | MORE_THAN | MORE_EQUAL_THAN | LESS_EQUAL_THAN | LESS_THAN) right=expression #binaryOperatorExpression
         | left=expression WHITESPACE infixFunction=ID WHITESPACE right=expression #infixFunction
         | expression referencedCall #referencedCallExpression
         | expression DOT ID #accessExpression //something.property
         | parenthesizedExpression #parenthesisExpression
         | ID #propertyReference
         | structureDef #structureDefinition
         | inverse #booleanNot
         | fullDeclaration #declarationExpression

        ;

parenthesizedExpression: LPAREN expression RPAREN;

propertyModifier:
     MUTABLE
   | STATEFUL
   | SECRET
   | STATIC
   | NATIVE;

fullDeclaration: propertyModifier* ID typeSpec;
declaration: propertyModifier* ID typeSpec?;
assignment: declaration ASSIGN expression;
block: LCPAREN body RCPAREN;

call:  LPAREN args RPAREN;

referencedCall: LSPAREN args RSPAREN;

args: expression? (COMMA expression)*;
typeSpec: DOUBLE_COLON type;

type: functionType | ID | NOTHING | tuple;

tuple: LPAREN (tupleParam COMMA tupleParam)+ RPAREN;
tupleParam : ID typeSpec;

/*
Structures
*/
structureDef:
      structDef #structDefinition
    | traitDef #traitDefinition;

traitDef: TRAIT ID parents? structureBody?;
structDef: STRUCT ID parents? structureBody?;
parents: DOUBLE_COLON ID (COMMA ID)*;
structureBody: LCPAREN structureLine* RCPAREN;
structureLine: fullDeclaration #declarationLine | line #normalLine;

/*
Generics
*/
generic: LESS_THAN typeParam (COMMA typeParam)* MORE_THAN;
typeParam: (ID typeSpec) | ID;
/*
Functions
*/
function: generic? (functionSignature block); //{} OR (a::Int) -> Int {}
functionSignature: functionParams ARROW type; //() -> _ OR (a::Int) -> Int
functionParams: (LPAREN functionParam? (COMMA functionParam)* RPAREN); //(a::Int) OR (a::Int, b::Int, etc)
functionParam: ID typeSpec; //a::Int

lambda: block | (lambdaParams ARROW (expression | block)); //{} OR (a::Int) -> Int OR (a::Int) -> Int {}
/*
(a, b) -> 3
(a::Int, b::Int) -> a + b
() -> {}
*/
lambdaParams: (LPAREN lamdaParam? (COMMA lamdaParam)* RPAREN); //(a::Int) OR (a, b, etc)
lamdaParam: functionParam | ID; //a::Int OR a

/*
Function Types
*/
functionType: (functionParamTypes) ARROW type;
functionParamTypes: LPAREN type? (COMMA type)* RPAREN;

literal:
      MINUS? INT #intLiteral
    | MINUS? DEC #decLiteral
    | BOOL #boolLiteral
    | STRING #stringLiteral
    | CHAR #charLiteral
    | LPAREN (expression COMMA expression)+ RPAREN #tupleLiteral;


inverse: INVERSE expression; //!hello == 3

