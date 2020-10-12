/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.stream;

import java.util.function.Function;

import io.vavr.collection.Stream;

public class Empty<T> implements LazyStream<T> {

    @Override
    public LazyStream<T> mergeStreams(LazyStream<T> other) {
        return other;
    }

    @Override
    public LazyStream<T> map(Function<T, LazyStream<T>> f) {
        return this;
    }

    @Override
    public LazyStream<T> realize() {
        return this;
    }

    @Override
    public Stream<T> streamToIter() {
        return Stream.empty();
    }

}
