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
but can be made mutable with the `mutable` keyword.~~~~ 
