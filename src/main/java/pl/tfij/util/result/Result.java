package pl.tfij.util.result;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface Result<T, E> {
    static<T, E> Result<T, E> succeedResult(T value) {
        return new SucceedResult<>(value);
    }

    static<T, E> Result<T, E> errorResult(E error) {
        return new ErrorResult<>(error);
    }

    static<T> Result<T, Exception> tryToDo(Callable<T> x) {
        try {
            return succeedResult(x.call());
        } catch (Exception e) {
            return errorResult(e);
        }
    }

    @SuppressWarnings("unchecked")
    static<T, E> Result<T, E> narrow(Result<? extends T, ? extends E> result) {
        return (Result<T, E>) result;
    }

    boolean isSucceed();
    <U> Result<U, E> map(Function<? super T, ? extends U> mapper);
    <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper);
    T mustGet();
    Optional<T> get();
    T getOrElse(T other);
    T getOrElse(Function<? super E, ? extends T> other);
    Result<T, E> orElse(Result<? extends T, ? extends E> other);
    Result<T, E> orElse(Function<? super E, Result<? extends T, ? extends E>> other);
    <X extends RuntimeException> T getOrElseThrow(Function<E, ? extends X> exceptionFunction);
    Result<T, E> peekError(Consumer<E> errorConsumer);
    <E2> Result<T, E2> mapError(Function<? super E, ? extends E2> wrapper);
    Stream<T> stream();
}
