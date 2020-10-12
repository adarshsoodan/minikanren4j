/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import java.io.Serializable;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

public interface Goal {

    LazyStream<SubstMap> with(SubstMap map);

    public default Goal and(Goal... goals) {
        return new And(List.of(this)
                           .appendAll(List.of(goals)));
    }

    public default Goal and(Seq<Goal> goals) {
        return new And(List.of(this)
                           .appendAll(goals));
    }

    public default Goal or(Goal... goals) {
        return new Or(List.of(this)
                          .appendAll(List.of(goals)));
    }

    public default Goal or(Seq<Goal> goals) {
        return new Or(List.of(this)
                          .appendAll(goals));
    }

    public default Goal unify(Serializable u, Serializable v) {
        return and(new Unify(u, v));
    }

    public default Goal diseq(Serializable u, Serializable v) {
        return and(new Diseq(u, v));
    }

}
