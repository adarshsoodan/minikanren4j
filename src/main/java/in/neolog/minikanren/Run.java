package in.neolog.minikanren;

import java.io.Serializable;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.stream.LazyStream;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class Run {

    public List<Map<LVar, Serializable>> run(List<LVar> lvs, Goal g, int max) {
        LazyStream<SubstMap> stream = g.with(new SubstMap());
        return Iterator.ofAll(stream.streamToIter())
                       .map(smap -> lvs.map(lv -> Tuple.of(lv, smap.walk(lv)))
                                       .toMap(t -> t))
                       .take(max)
                       .toList();
    }

}
