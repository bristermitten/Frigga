# Frigga

Frigga is a strongly, dynamically typed functional language

Frigga follows a simple syntax that looks something like this 
```
x = 3 #Type inferred to Int
pi::Dec = 3.141 #Explicit Type Specification

mutable y = x + pi
y += 1

printANumber = (value::Num) -> {
    println(value)
}

printANumber(y) #prints 7.141
```

## Properties

Property declaration is done with the syntax:

`name = value`
or `name::Type = value`

All properties are reference immutable by default,
but can be made mutable with the `mutable` keyword. 
This keyword is deliberately verbose to discourage mutability.


Functions are declared in the same way as properties.

## Functions
Functions are first class types, and as such 
they are declared in exactly the same way as 
properties.

For example:

```
printHello = () -> _ {
    println("Hello")
}
```

The `() -> _` is known as the **Signature**.
This describes the input and output of a function, along with optional
type parameters for generics. 
In this example, `printHello` takes no parameters, and returns
**Nothing**. Nothing can be substituted with `_` for less verbosity.

In this case, a **Lambda** can also be used in place of 
a direct function. Lambdas are a shorthand for declaring functions,
where the output (and sometimes input) types are inferred.

For example 
```
printHello = () -> {
    println("Hello")
}
```
or even
```
printHello = {
    println("Hello")
}
``` 
(where the output type is inferred to **Nothing**)

This can be used in exactly the same way as before.

Functions can also **yield** values:

```
getMyFavouriteNumber = {
    yield(10)
}
```
This allows the result of a function to be used elsewhere.
Yielding also breaks execution, so any expressions after a `yield()`
call will not be evaluated.

We also have the option of using an **Expression Lambda**,
which is a lambda that performs 1 expression only. The value
is implicitly yielded.

```
getAnotherNumber = () -> 10
```

Finally, function calls can be transformed into functions themselves
with **reference calls**. These look like normal function calls
except with square brackets instead of normal ones.

For example:
```
printHello = println["Hello"]
printHello()
```


These language features give developers freedom to design code 
as concisely or verbosely as they like, while maintaining clarity either way.
