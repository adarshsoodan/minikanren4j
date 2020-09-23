/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import static in.neolog.minikanren.MinKan.and;
import static in.neolog.minikanren.MinKan.or;
import static in.neolog.minikanren.MinKan.unify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.junit.Test;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.reify.Reify;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TestRun {

    @SuppressWarnings("boxing")
    @Test
    public void run() {
        LVar x = LVar.create();
        LVar y = LVar.create();

        BiFunction<Serializable, Serializable, Goal> withVals = (xx, yy) -> and(unify(x, xx), unify(y, yy));

        Goal goal = or(withVals.apply(11, "stry"), withVals.apply("strx", 5));

        List<Map<LVar, Serializable>> results = new Run().run(List.of(x, y), goal, 3);

        try (Reify reify = Reify.reify()) {
            var firstM = allOf(hasValue((Serializable) "stry"), hasValue((Serializable) 11));
            var secondM = allOf(hasValue((Serializable) "strx"), hasValue((Serializable) 5));

            results.forEach(smap -> {
                assertThat(smap.toJavaMap(), anyOf(firstM, secondM));
            });
            // results.forEach(smap -> System.out.println(smap));
        }

    }

}
