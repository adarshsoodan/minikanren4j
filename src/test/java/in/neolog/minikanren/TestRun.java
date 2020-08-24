package in.neolog.minikanren;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.junit.Test;

import in.neolog.minikanren.goal.And;
import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.goal.Or;
import in.neolog.minikanren.goal.Unify;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TestRun {

    @Test
    public void run() {
        LVar x = LVar.create();
        LVar y = LVar.create();

        BiFunction<Serializable, Serializable, Goal> withVals = (xx, yy) -> new And(new Unify(x, xx), new Unify(y, yy));

        Goal goal = new Or(withVals.apply(11, "stry"), withVals.apply("strx", 5));

        List<Map<LVar, Serializable>> results = new Run().run(List.of(x, y), goal, 3);
        System.out.println(results);
    }

}
