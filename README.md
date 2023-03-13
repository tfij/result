# result - Functional way to handle errors in java 

The inspiration for this project was the functional approach to error handling, such as in
[scala](http://www.scala-lang.org/). The scala
has types [Either](http://www.scala-lang.org/api/2.9.3/scala/Either.html) and
[Try](http://www.scala-lang.org/api/2.9.3/scala/util/Try.html).

The first one is more generic, it lets to use any type of error class.
On the other hand, to handle an error, checking an instance of subclass (in scala with a pattern matting) or calling a method like isLeft() is required.

Try is a monad with fluent API, unfortunately there's no possibility to use a custom error class.

I prepare `Result` type which solves both problems. 
This type allow to have a custom error type and use fluent API with methods like `map()`, `flatMap()`, `getOrElse()` and so on.

## Code example

```Java
double discount = 0.2;
double DEFAULT_PRICE = 100;
String priceInput = ""; // not validated input
double calcylatedPrice = Result.tryToDo(() -> Double.parseDouble(priceInput))
        .map(price -> price * discount)
        .peekError(error -> log.warn(error.getMessage()))
        .getOrElse(DEFAULT_PRICE);
```

## Maven

```
<dependency>
  <groupId>pl.tfij</groupId>
  <artifactId>result</artifactId>
  <version>1.0</version>
</dependency>
```
