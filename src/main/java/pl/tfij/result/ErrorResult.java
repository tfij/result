package pl.tfij.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class ErrorResult<T, E> implements Result<T, E> {
    private final E error;

    ErrorResult(E error) {
        this.error = error;
    }

    @Override
    public boolean isSucceed() {
        return false;
    }

    @Override
    public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        return new ErrorResult<>(error);
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
        return new ErrorResult<>(error);
    }

    @Override
    public T mustGet() {
        throw new IllegalStateException("Can't mustGet value from error result");
    }

    @Override
    public Optional<T> get() {
        return Optional.empty();
    }

    @Override
    public T getOrElse(T other) {
        return other;
    }

    @Override
    public T getOrElse(Function<? super E, ? extends T> other) {
        return other.apply(error);
    }

    @Override
    public Result<T, E> orElse(Result<? extends T, ? extends E> other) {
        return Result.narrow(other);
    }

    @Override
    public Result<T, E> orElse(Function<? super E, Result<? extends T, ? extends E>> other) {
        return Result.narrow(other.apply(error));
    }

    @Override
    public <X extends RuntimeException> T getOrElseThrow(Function<E, ? extends X> exceptionFunction) {
        throw exceptionFunction.apply(error);
    }

    @Override
    public Result<T, E> peekError(Consumer<E> errorConsumer) {
        errorConsumer.accept(error);
        return this;
    }

    @Override
    public <E2> Result<T, E2> mapError(Function<? super E, ? extends E2> wrapper) {
        return new ErrorResult<>(wrapper.apply(error));
    }

    @Override
    public Stream<T> stream() {
        return Stream.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResult<?, ?> that = (ErrorResult<?, ?>) o;
        return Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error);
    }

    @Override
    public String toString() {
        return String.format("ErrorResult[%s]", error);
    }
}
