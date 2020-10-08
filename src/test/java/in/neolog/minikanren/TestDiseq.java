/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import static in.neolog.minikanren.MinKan.and;
import static in.neolog.minikanren.MinKan.diseq;
import static in.neolog.minikanren.MinKan.or;
import static in.neolog.minikanren.MinKan.unify;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasValue;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotEquals;

import java.io.Serializable;
import java.util.function.BiFunction;

import org.junit.Test;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.reify.Reify;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class TestDiseq {

    @Test
    public void x_eq_y_bad() {
        try (Reify reify = Reify.reify()) {
            LVar x = LVar.create();
            LVar y = LVar.create();
            var smap = new SubstMap().diseq(x, y)
                                     .unify(x, "bad")
                                     .unify(y, "bad")
                                     .unify(x, y);

            assertNotEquals(smap.walk(x), "bad");
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public void test() {
        try (Reify reify = Reify.reify()) {
            LVar x = LVar.create();
            LVar y = LVar.create();
            LVar z = LVar.create();

            BiFunction<Serializable, Serializable, Goal> withVals = (xx, zz) -> and(diseq(x, z), unify(x, xx),
                    unify(z, zz), unify(x, y));

            Goal goal = or(withVals.apply(11, "strz"), withVals.apply("strx", 5), withVals.apply("strx", "strz"),
                    withVals.apply("bad", "bad"));

            List<Map<LVar, Serializable>> results = new Run().run(List.of(x, y, z), goal, 3);

            var firstM = allOf(hasValue((Serializable) "strz"), hasValue((Serializable) 11));
            var secondM = allOf(hasValue((Serializable) "strx"), hasValue((Serializable) 5));
            var thirdM = allOf(hasValue((Serializable) "strx"), hasValue((Serializable) "strz"));
            var fourthM = not(hasValue((Serializable) "bad"));

            results.forEach(smap -> {
                assertThat(smap.toJavaMap(), allOf(anyOf(firstM, secondM, thirdM), fourthM));
            });
        }

    }

}
