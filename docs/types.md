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

