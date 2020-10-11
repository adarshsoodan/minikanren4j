/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.stream;

import java.util.function.Function;
import java.util.function.Supplier;

import io.vavr.collection.Stream;

public class Delayed<T> implements LazyStream<T> {

    private final Supplier<LazyStream<T>> thunk;

    public Delayed(Supplier<LazyStream<T>> thunk) {
        this.thunk = thunk;
    }

    @Override
    public LazyStream<T> mergeStreams(LazyStream<T> other) {
        return new Delayed<>(() -> other.mergeStreams(thunk.get()));
    }

    @Override
    public LazyStream<T> map(Function<T, LazyStream<T>> f) {
        return new Delayed<>(() -> thunk.get()
                                        .map(f));
    }

    @Override
    public LazyStream<T> realize() {
        var ls = thunk.get();
        while (ls instanceof Delayed) {
            Delayed<T> dls = (Delayed<T>) ls;
            var nestedThunk = dls.thunk;
            ls = nestedThunk.get();
        }
        return ls;
    }

    @Override
    public Stream<T> streamToIter() {
        return realize().streamToIter();
    }

}
