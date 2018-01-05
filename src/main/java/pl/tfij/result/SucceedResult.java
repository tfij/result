package pl.tfij.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class SucceedResult<T, E> implements Result<T, E> {
    private final T value;

    SucceedResult(T value) {
        this.value = value;
    }

    @Override
    public boolean isSucceed() {
        return true;
    }

    @Override
    public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new SucceedResult<>(mapper.apply(value));
    }

    @Override
    public <U> Result<U, E> flatMap(Function<? super T, Result<U, E>> mapper) {
        Objects.requireNonNull(mapper);
        return mapper.apply(value);
    }

    @Override
    public T mustGet() {
        return value;
    }

    @Override
    public Optional<T> get() {
        return Optional.of(value);
    }

    @Override
    public T getOrElse(T other) {
        return value;
    }

    @Override
    public T getOrElse(Function<? super E, ? extends T> other) {
        return value;
    }

    @Override
    public Result<T, E> orElse(Result<? extends T, ? extends E> other) {
        return this;
    }

    @Override
    public Result<T, E> orElse(Function<? super E, Result<? extends T, ? extends E>> other) {
        return this;
    }

    @Override
    public <X extends RuntimeException> T getOrElseThrow(Function<E, ? extends X> exceptionFunction) {
        return value;
    }

    @Override
    public Result<T, E> peekError(Consumer<E> errorConsumer) {
        return this;
    }

    @Override
    public <E2> Result<T, E2> mapError(Function<? super E, ? extends E2> wrapper) {
        return new SucceedResult<>(value);
    }

    @Override
    public Stream<T> stream() {
        return Stream.of(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SucceedResult<?, ?> that = (SucceedResult<?, ?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("SucceedResult[%s]", value);
    }
}
