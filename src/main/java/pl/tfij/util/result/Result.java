package pl.tfij.util.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Result<T, E> {
    static<T, E> Result<T, E> succedResult(T value) {
        return new SucceedResult<>(value);
    }
    static<T, E> Result<T, E> errorResult(E error) {
        return new ErrorResult<T, E>(error);
    }

    boolean isSucceed();
    <U> Result<U, E> map(Function<? super T, ? extends U> mapper);
    <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper);
    T get();
    T orElse(T other);
    T orElseGet(Supplier<T> other);
    Result<T, E> peekError(Consumer<E> errorConsumer);
    <E2> Result<T, E2> wrapError(Function<? super E, ? extends E2> wrapper);
}
