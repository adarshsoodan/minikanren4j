/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import java.io.Serializable;
import java.util.Objects;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.Computed;
import in.neolog.minikanren.stream.Delayed;
import in.neolog.minikanren.stream.Empty;
import in.neolog.minikanren.stream.LazyStream;

public class NotEq implements Goal {

    private final Serializable u;
    private final Serializable v;

    public NotEq(Serializable u, Serializable v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        return new Delayed<>(() -> {
            var x = map.walk(u);
            var y = map.walk(v);

            if (Objects.equals(x, y)) {
                return new Empty<>();
            } else {
                return new Computed<>(map, new Empty<>());
            }
        });
    }

}
