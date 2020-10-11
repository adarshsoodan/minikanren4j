/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.Delayed;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.collection.List;

public class Or implements Goal {

    private final List<Goal> goals;

    public Or(List<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        return new Delayed<>(() -> {
            var streams = goals.map(g -> g.with(map));
            return streams.reduceLeft((left, right) -> left.mergeStreams(right));
        });
    }

}
