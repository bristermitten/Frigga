# Frigga's Type System

Frigga is a **Strongly Typed** language. Values can only be used if their type matches
an expected type - a `String` cannot be used in place of an `Int`.

However, Frigga comes with some form of **Type Coercion**. 
Some values can be used when other types are expected.

These situations involve: 
* Using an `Int` in place of a `Dec` (eg `3` will be treated as `3.0`)
* Using a literal instead of a function that returns that value (eg `() -> 5` can be replaced with `5`)
. However, a `() -> Int` cannot be coerced to an `Int`. *This may be improved upon in the future.*
* All values can be coerced into a String when used in String concatenation. *This will probably involve types defining a `toString`  function*.

## Built-in Types
* Any - Every type is a subtype of Any. It is the highest point in the Type Hierarchy
* Num - Num is the supertype of every number type
* Int - Represents a 64-bit signed integer. Internally these are handled with Java longs
* Dec - Represents a 64-bit signed floating-point number. Internally these are Java doubles
* Char - A single character.
* String - A String of Chars
* Nothing - Nothing represents the absence of a value. Nothing is special in that it is a subtype of every other type
i.e - every value can be Nothing
