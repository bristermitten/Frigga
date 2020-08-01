parser grammar FriggaParser;

options { tokenVocab=FriggaLexer; }

literal
    : IntLiteral
    | DecLiteral
    | CharLiteral
    | BoolLiteral
    | StringLiteral
    | tupleLiteral
    ;

/*
* Tuples
* Tuple literals can be made with either named syntax (eg `(age: 30, name: "Steve")`)
* or indexed syntax (eg `(30, "Steve"))
*/

tupleLiteral
    : namedTupleLiteral
    | indexedTupleLiteral
    ;

indexedTupleLiteral
    : LPAREN indexedTupleValues RPAREN
    ;

indexedTupleValues
    : (expression COMMA expression)+
    ;


namedTupleLiteral
    : LPAREN namedTupleValues RPAREN
    ;

namedTupleValues
    :(namedTupleValue COMMA namedTupleValue)+
    ;

namedTupleValue
    : ID COLON expression
    ;


type
    : structType //eg StructName
    | nothingType //_
    | functionType //eg (Int) -> _
    | tupleType //eg (name: String, age: Int)
    ;

nothingType
    : UNDERSCORE
    ;

structType
    : ID typeArguments?
    ;

functionType
    : LPAREN functionTypeParameterTypes RPAREN ARROW functionTypeReturnType
    ;

functionTypeParameterTypes
    :  type? (COMMA type) *
    ;

functionTypeReturnType
    : type
    ;

tupleType
    : LPAREN tupleTypeParam (COMMA tupleTypeParam)? RPAREN
    ;
tupleTypeParam
    : ID COLON type
    ;


typeArguments //eg <Int, String>
    : '<' typeArgumentList '>'
    ;

typeArgumentList //eg Int, String
    : typeArgument (COMMA typeArgument)*
    ;

typeArgument //eg Int - this will be improved on in future to add things like wildcards
    : type
    ;

/*
* Names
*/

namespaceName
    : ID
    | namespaceName '/' ID
    ;

javaPackageName
    : ID (DOT javaPackageName)*
    ;

useNamespaceName
    : namespaceName
    | JVM COLON javaPackageName
    ;

structName
    : ID
    | namespaceName '/' ID
    ;

/*
* Headers
*/

header
    : (namespaceDeclaration ENDLINE)? (useDeclaration ENDLINE)*
    ;

namespaceDeclaration
    : NAMESPACE namespaceName
    ;

useDeclaration
    : USE useNamespaceName
    ;

friggaFile
    : header body EOF
    ;


/*
* Body
*/

body
    : lines
    ;

lines
    : ENDLINE* | (ENDLINE* line (ENDLINE* line)* ENDLINE?)
    ;



line
    : statement #statementLine
    | expression #expressionLine
    ;

untypedPropertyDeclaration
    : propertyModifier* ID
    ;

typedPropertyDeclaration
    : propertyModifier* ID DOUBLE_COLON type
    ;

propertyAssignment
    :
    ( untypedPropertyDeclaration | typedPropertyDeclaration )
    ASSIGN expression
    ;

propertyDeclaration
    : typedPropertyDeclaration //for native properties (and possibly more in the future)
    ;

propertyModifier
    : NATIVE
    | SECRET
    | MUTABLE
    ;

/*
* Functions
*/

functionValue
    : functionSignature functionBody
    ;

functionBody
    : LCPAREN body RCPAREN
    ;

functionSignature
    : typeSignature? functionArguments ARROW type
    ;

functionArguments
    : LPAREN (functionArgument? (COMMA functionArgument)*) RPAREN
    ;

functionArgument
    : ID DOUBLE_COLON type
    ;

typeSignature
    : '<' typeParameters '>'
    ;

typeParameters
    : typeParameter (COMMA typeParameter)*
    ;

typeParameter
    : ID DOUBLE_COLON type
    ;


/*
* Statements
*/
statement
    : propertyDeclaration #propertyDeclarationStatement
    | propertyAssignment  #propertyAssignmentStatement
    | structDeclaration   #structDeclarationStatement
    | traitDeclaration    #traitDeclarationStatement
    ;

/*
* Expressions
*/
propertyAccess //eg SomeStruct.prop or someValue.prop or prop
    : propertyAccess DOT ID #childAccess
    | ID #directAccess
    ;

functionCall
    : propertyAccess //the function name
      functionCallParameters //parameters
      functionBody? //a Lambda body
    ;

referencedCall
    : propertyAccess
      refererencedCallParameters
    ;

refererencedCallParameters
    : LSPAREN functionCallParametersList RSPAREN
    ;

functionCallParameters
    : LPAREN functionCallParametersList RPAREN
    ;



functionCallParametersList
    : (indexedFunctionCallParameter? (COMMA indexedFunctionCallParameter)*) //Indexed calls
    | (namedFunctionCallParameter? (COMMA namedFunctionCallParameter)*) //named calls
    ;


indexedFunctionCallParameter // 3
    : expression
    ;

namedFunctionCallParameter //age: 30
    : ID COLON expression
    ;

infixFunctionCall
    : assignableExpression ID expression
    ;

prefixOperatorCall
    :
       operator=(NOT | BIN_NOT)
       right=expression
    ;

//Expresssions that can be used in assignment (eg x = 3) but cannot be used stanalone (eg 3)
assignableExpression
    : literal #literalExpression
    | functionValue #functionExpression
    | lambdaValue #lambdaExpression
    ;

notBinaryExpression
    : functionCall #callExpression
    | referencedCall #referencedCallExpression
    | infixFunctionCall #infixCallExpression
    | assignableExpression #assignableExpressionExpression
    | propertyAccess #accessExpression
    | prefixOperatorCall #prefixOperatorExpression
    | LPAREN expression RPAREN #parenthesisedExpression
    ;

expression
    : notBinaryExpression #notBinaryExpressionExpression
    | left=expression
      operator=(POWER | TIMES | DIVIDE | PLUS | MINUS | EQUAL | MORE_THAN | MORE_EQUAL_THAN | LESS_THAN | LESS_EQUAL_THAN )
      right=expression
      #binaryOperatorExpression //we can't have this in its own rule because antlr doesn't like mutual left-recursion :(
    ;

/*
* Lambdas
*/

lambdaValue
    : functionBody #blockLambda //eg { println("Hello") }
    | functionArguments ARROW expression #typedSingleExpressionLambda //single expression typed lambda eg (a::Int) -> blah(a)
    | lambdaArguments ARROW expression #untypedSingleExpressionLambda //single expression untyped lambda eg (a) -> blah(a)
    | functionArguments ARROW functionBody #blockTypedParamsLambda //block typed lambda
    | lambdaArguments ARROW functionBody #blockUntypedParamsLambda //block untyped lambda
    ;

lambdaArguments
    : LPAREN ID (COMMA ID)* RPAREN;


/*
* Structs and Traits
*/

structDeclaration
    : STRUCT ID structParentDeclaration?
    structBody?
    ;

traitDeclaration
    : TRAIT ID structParentDeclaration?
    structBody?
    ;

structBody
    : LCPAREN
    (ENDLINE* statement (ENDLINE* statement)* ENDLINE?) //TODO this bit should probably be alongside the body rule, rather than being redefined...
    RCPAREN
    ;

structParentDeclaration
    : COLON
    ID (COMMA ID)*
    ;
