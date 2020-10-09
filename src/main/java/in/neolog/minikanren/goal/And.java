/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.goal;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.Computed;
import in.neolog.minikanren.stream.Delayed;
import in.neolog.minikanren.stream.Empty;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.collection.List;

public class And implements Goal {

    private final List<Goal> goals;

    public And(Goal... goals) {
        this.goals = List.of(goals);
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        return new Delayed<>(() -> {
            LazyStream<SubstMap> seed = new Computed<>(map, new Empty<>());
            return goals.foldLeft(seed, (acc, g) -> acc.map(g::with));
        });
    }

}
