package in.neolog.minikanren.stream;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Function;

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
    public Iterator<T> streamToIter() {
        return Collections.emptyIterator();
    }

}
