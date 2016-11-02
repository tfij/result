package pl.tfij.util.result;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public T get() {
        throw new RuntimeException("Can't get value from error result");
    }

    @Override
    public T orElse(T other) {
        return other;
    }

    @Override
    public T orElseGet(Supplier<T> other) {
        return other.get();
    }

    @Override
    public Result<T, E> peekError(Consumer<E> errorConsumer) {
        errorConsumer.accept(error);
        return this;
    }

    @Override
    public <E2> Result<T, E2> wrapError(Function<? super E, ? extends E2> wrapper) {
        return new ErrorResult<>(wrapper.apply(error));
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
