package in.neolog.minikanren.goal;

import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.Delayed;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.collection.List;

public class Or implements Goal {

    private final List<Goal> goals;

    public Or(Goal... goals) {
        this.goals = List.of(goals);
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        return new Delayed<>(() -> {
            var streams = goals.map(g -> g.with(map));
            var merged = streams.reduceLeft((left, right) -> left.mergeStreams(right));
            return merged;
        });
    }

}
