package in.neolog.minikanren;

import static in.neolog.minikanren.MinKan.and;
import static in.neolog.minikanren.MinKan.noteq;
import static in.neolog.minikanren.MinKan.or;
import static in.neolog.minikanren.MinKan.unify;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.junit.Test;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.reify.Reify;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TestNotEq {

    @Test
    public void test() {
        LVar x = LVar.create();
        LVar y = LVar.create();
        LVar z = LVar.create();

        BiFunction<Serializable, Serializable, Goal> withVals = (xx, zz) -> and(unify(x, xx), unify(z, zz), unify(x, y),
                noteq(x, z));

        Goal goal = or(withVals.apply(11, "strz"), withVals.apply("strx", 5), withVals.apply("strx", "strz"),
                withVals.apply("str", "str"));

        List<Map<LVar, Serializable>> results = new Run().run(List.of(x, y, z), goal, 3);

        try (Reify reify = Reify.reify()) {
            results.forEach(smap -> System.out.println(smap));
        }

    }

}
