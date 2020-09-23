package in.neolog.minikanren.stream;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;


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
            ls = ls.realize();
        }
        return ls;
    }

    @Override
    public Iterator<T> streamToIter() {
        return realize().streamToIter();
    }

}
