# Frigga

Frigga is a strongly, dynamically typed functional programming language.

It follows a simple syntax that looks something like this: 
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

To find out more about Frigga's syntax and features, take a look at the [docs](/docs/README.md)


## Planned Features

- [ ] Static Analysis in place of a compiler
- [ ] Annotations
- [ ] Language Level function optimisations (eg tail recursion unrolling, same-input same-output caching, etc)
- [ ] Extension Functions
- [ ] Structs / Classes
- [ ] Named and Default Parameters
- [ ] Fully implement the runtime!
- [ ] Much more!


## Potential Features
* Function definition as a reference call. For example `printAValue = println[value::Any]`
 would create a function equivalent to `printAValue = (value::Any) -> { println(value) }`. 
*This could get very complicated, very quickly and isn't great for readability. It is unlikely to be implemented.*
* Different Backends? eg a JVM compiler for larger projects, an interpreter for scripting, etc. *Potentially will happen, may be more trouble than it's worth.*
