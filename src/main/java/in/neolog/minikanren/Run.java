package in.neolog.minikanren;

import java.io.Serializable;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class Run {

    public List<Map<LVar, Serializable>> run(List<LVar> lvars, Goal goal, int max) {
        LazyStream<SubstMap> stream = goal.with(new SubstMap());
        return Iterator.ofAll(stream.streamToIter())
                       .map(smap -> lvars.map(lv -> Tuple.of(lv, smap.walk(lv)))
                                         .toMap(t -> t))
                       .take(max)
                       .toList();
    }

}
