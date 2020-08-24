package in.neolog.minikanren.stream;

import java.util.Iterator;
import java.util.function.Function;

import com.google.common.collect.Iterators;

public class Computed<T> implements LazyStream<T> {

    private final T             head;
    private final LazyStream<T> next;

    public Computed(T head, LazyStream<T> next) {
        this.head = head;
        this.next = next;
    }

    @Override
    public LazyStream<T> mergeStreams(LazyStream<T> other) {
        return new Computed<>(head, other.mergeStreams(next));
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
    public Iterator<T> streamToIter() {
        return Iterators.concat(Iterators.singletonIterator(head), next.streamToIter());
    }

}