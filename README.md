# result - Functional way to handle errors in java

The inspiration for this project was the functional approach to error handling, such as in
[scala](http://www.scala-lang.org/). The scala
has types [Either](http://www.scala-lang.org/api/2.9.3/scala/Either.html) and
[Try](http://www.scala-lang.org/api/2.9.3/scala/util/Try.html).

First one let you contains any type of error class but you have to check instance of class
(in scala with pattern matting) or call method like isLeft() to handle the error.

Instead Try is a monad and give you fluent API but you can't work with custom error class.

I prepare `Result` type which solves both problems. You can have custom error type and
use fluent API with methods like `map()`, `orElse()` and so on.

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
