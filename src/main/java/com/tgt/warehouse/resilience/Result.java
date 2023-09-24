package com.tgt.warehouse.resilience;

import java.util.function.Function;

public sealed interface Result<T> {

     <R> Result<R> map(Function<T, R> mapper);

    static <U> Result<U> failure(Throwable throwable) {
        return new Failure<>(throwable);
    }

    static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    record Success<T>(T value) implements Result<T> {
        @Override
        @SuppressWarnings("unchecked")
        public <R> Result<R> map(Function<T, R> mapper) {
            return (Result<R>) mapper.apply(value);
        }
    }

    record Failure<T>(Throwable throwable) implements Result<T> {
            @Override
        public <R> Result<R> map(Function<T, R> mapper) {
            return null;
        }
    }
}

/*
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Try<T> {

    private final boolean success;

    public Try(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isFailure() {
        return this.success == false;
    }

    public abstract Throwable getThrownMessage();

    public abstract T get();

    public abstract <U> Try<U> map(Function<? super T, ? extends U> fn);

    public abstract <U> Try<U> flatMap(Function<? super T, Try<U>> fn);

    static <T> Try<T> failure(Throwable t) {
        Objects.requireNonNull(t);
        return new Failure<>(t);
    }

    static <V> Try.Success<V> success(V value) {
        Objects.requireNonNull(value);
        return new Success<>(value);
    }

    static <T> Try<T> of(Supplier<T> fn) {
        Objects.requireNonNull(fn);
        try {
            return Try.success(fn.get());
        }
        catch (Throwable t) {
            return Try.failure(t);
        }
    }

    static class Failure<T> extends Try<T> {

        private final RuntimeException exception;

        public Failure(Throwable t) {
            super(false);
            this.exception = new RuntimeException(t);
        }

        @Override
        public T get() {
            throw this.exception;
        }

        @Override
        public <U> Try<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            return Try.failure(this.exception);
        }

        @Override
        public <U> Try<U> flatMap(Function<? super T, Try<U>> fn) {
            Objects.requireNonNull(fn);
            return Try.failure(this.exception);
        }

        @Override
        public Throwable getThrownMessage() {
            return this.exception;
        }
    }

    static class Success<T> extends Try<T> {

        private final T value;

        public Success(T value) {
            super(true);
            this.value = value;
        }

        @Override
        public T get() {
            return this.value;
        }

        @Override
        public <U> Try<U> map(Function<? super T, ? extends U> fn) {
            Objects.requireNonNull(fn);
            try {
                return Try.success(fn.apply(this.value));
            }
            catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public <U> Try<U> flatMap(Function<? super T, Try<U>> fn) {
            Objects.requireNonNull(fn);
            try {
                return fn.apply(this.value);
            }
            catch (Throwable t) {
                return Try.failure(t);
            }
        }

        @Override
        public Throwable getThrownMessage() {
            throw new IllegalStateException("Success never has an exception");
        }
    }
}
 */