/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.stream;

import java.util.Iterator;
import java.util.function.Function;

public interface LazyStream<T> {

    LazyStream<T> mergeStreams(LazyStream<T> other);

    LazyStream<T> map(Function<T, LazyStream<T>> f);

    LazyStream<T> realize();

    Iterator<T> streamToIter();

}
