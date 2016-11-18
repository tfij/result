package pl.tfij.util.result;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    boolean isSucceed();
    <U> Result<U, E> map(Function<? super T, ? extends U> mapper);
    <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper);
    T mustGet();
    Optional<T> get();
    T orElse(T other);
    T orElseGet(Function<E, T> other);
    <X extends Throwable> T orElseThrow(Function<E, ? extends X> exceptionFunction) throws X;
    Result<T, E> peekError(Consumer<E> errorConsumer);
    <E2> Result<T, E2> wrapError(Function<? super E, ? extends E2> wrapper);
}
