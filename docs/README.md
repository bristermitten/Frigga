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


## Other Features

* Frigga has very few keywords. Most functionality is done with existing functions
in the standard library that may have lower level access (eg `yield` has direct access
to the stack unlike most functions). This makes functionality more transparent rather than
hiding lower level things from the developer.

* Frigga features protection against side effects. Any function that modifies a mutable
property must have the `stateful` keyword. Calling a stateful function forces the caller
to also be stateful. This forces acknowledgement of state, making functionality more explicit.

* Frigga is partially interoperable with JVM classes, being able to interact with them,
but not vice versa. This will be improved upon in the future.

* Frigga has no concept of `null`. Functions may return Nothing, which is both a type
and a value. `Nothing` is special, as it is a subtype of every type.
By default, calling a function on `Nothing` is a no-op, and will return Nothing, 
however in future Nothing handling will be improved, with errors similar to `NullPointerException` 
and possibly safe access, similar to Kotlin.

* Frigga features some type coersion. Literals can be coersed to functions returning
that value. For example, `3` can be used in place of `() -> 3`. **This is subject to change**

* All operators can be overloaded for a Type (eg + - * etc)

* Frigga features Tuples, which can be used to yield 2 or more values from a single function
