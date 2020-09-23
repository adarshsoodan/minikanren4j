/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import java.io.Serializable;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.Empty;
import in.neolog.minikanren.stream.Delayed;
import in.neolog.minikanren.stream.LazyStream;
import in.neolog.minikanren.stream.Computed;

public class Unify implements Goal {

    private final Serializable u;
    private final Serializable v;

    public Unify(Serializable u, Serializable v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        return new Delayed<>(() -> {
            SubstMap uMap = map.unify(u, v);
            if (uMap.isValid()) {
                return new Computed<>(uMap, new Empty<>());
            } else {
                return new Empty<>();
            }
        });
    }

    @Override
    public String toString() {
        return "Unify [u=" + u + ", v=" + v + "]";
    }
    
}
