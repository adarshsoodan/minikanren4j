package in.neolog.minikanren.goal;

import java.util.function.Function;

import in.neolog.minikanren.LVar;
import in.neolog.minikanren.SubstMap;
import in.neolog.minikanren.stream.LazyStream;

public class Fresh implements Goal {
    private final Function<LVar, Goal> unbound;

    public Fresh(Function<LVar, Goal> unbound) {
        this.unbound = unbound;
    }

    @Override
    public LazyStream<SubstMap> with(SubstMap map) {
        Goal bound = unbound.apply(LVar.create());
        return bound.with(map);
    }

}
