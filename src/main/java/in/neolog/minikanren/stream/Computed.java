/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.stream;

import java.util.function.Function;

import io.vavr.collection.Stream;

public class Computed<T> implements LazyStream<T> {

    private final T             head;
    private final LazyStream<T> next;

    public Computed(T head, LazyStream<T> next) {
        this.head = head;
        this.next = next;
    }

    @Override
    public LazyStream<T> mergeStreams(LazyStream<T> other) {
        return new Computed<>(head, next.mergeStreams(other));
    }

    @Override
    public LazyStream<T> map(Function<T, LazyStream<T>> f) {
        return f.apply(head)
                .mergeStreams(next.map(f));
    }

    @Override
    public LazyStream<T> realize() {
        return this;
    }

    @Override
    public Stream<T> streamToIter() {
        return Stream.cons(head, () -> next.streamToIter());
    }

}
