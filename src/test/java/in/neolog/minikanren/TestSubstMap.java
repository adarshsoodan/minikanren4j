/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import in.neolog.minikanren.reify.Reify;
import in.neolog.minikanren.util.Hashed;
import io.vavr.collection.HashSet;

public class TestSubstMap {

    @Test
    public void walk() {
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();

            var smap = new SubstMap().unify(x, y)
                                     .unify(y, "banana");

            assertEquals("banana", smap.walk(x));
            assertEquals("banana", smap.walk(y));
            assertEquals("mango", smap.walk("mango"));
        }
    }

    @Test
    public void unify() {
        try (Reify reify = Reify.reify()) {
            SubstMap smap = new SubstMap().unify("mango", "banana");
            assertEquals(SubstMap.invalidMap(), smap);
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var smap = new SubstMap().unify(x, "banana");
            assertEquals("banana", smap.walk(x));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var smap = new SubstMap().unify("banana", x);
            assertEquals("banana", smap.walk(x));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var smap = new SubstMap().unify(x, "banana")
                                     .unify(x, "banana");
            assertEquals("banana", smap.walk(x));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var smap = new SubstMap().unify(x, "mango")
                                     .unify(x, "banana");
            assertNotEquals("banana", smap.walk(x));
            assertEquals(SubstMap.invalidMap(), smap);
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();

            var smap = new SubstMap().unify(x, "squirrels")
                                     .unify(y, "banana");

            assertEquals("banana", smap.walk(y));
            assertNotEquals("banana", smap.walk(x));

            assertEquals("squirrels", smap.walk(x));
            assertNotEquals("squirrels", smap.walk(y));

        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();
            var z = LVar.create();

            var smap = new SubstMap().unify(x, y)
                                     .unify(y, z);

            assertEquals(z, smap.walk(x));
            assertEquals(z, smap.walk(y));
        }

    }

    @Test
    public void empty() {
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();

            var smap = new SubstMap();

            assertEquals(x, smap.walk(x));
            assertEquals(y, smap.walk(y));
            assertEquals("mango", smap.walk("mango"));

            var uMap = smap.unify(x, "str1");
            assertTrue(uMap.isValid());
        }
    }

    @Test
    public void eqc() {
        try (Reify reify = Reify.reify()) {
            var smap = new SubstMap();

            assertEquals(Hashed.of(HashSet.of(Hashed.of("str"))), smap.computeEqC(Hashed.of("str")));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();
            var z = LVar.create();

            var smap = new SubstMap().unify(x, y)
                                     .unify(y, z);

            assertEquals(Hashed.of(HashSet.of(Hashed.of(x), Hashed.of(y), Hashed.of(z))),
                    smap.computeEqC(Hashed.of(x)));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();
            var z = LVar.create();

            var smap = new SubstMap().unify(x, y)
                                     .unify(x, z);

            assertEquals(Hashed.of(HashSet.of(Hashed.of(x), Hashed.of(y), Hashed.of(z))),
                    smap.computeEqC(Hashed.of(x)));
        }
        try (Reify reify = Reify.reify()) {
            var x = LVar.create();
            var y = LVar.create();
            var z = LVar.create();

            var smap = new SubstMap().unify(x, y)
                                     .unify(z, x);
            assertEquals(Hashed.of(HashSet.of(Hashed.of(x), Hashed.of(y), Hashed.of(z))),
                    smap.computeEqC(Hashed.of(x)));
        }
    }

}
