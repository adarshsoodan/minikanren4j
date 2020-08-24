package in.neolog.minikanren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import io.vavr.collection.HashMap;

public class TestSubstMap {

    @Test
    public void add() {
        var smap = new SubstMap().add(new LVar(0), new LVar(1))
                                 .add(new LVar(1), "banana");

        assertEquals("banana", smap.walk(new LVar(0)));
        assertEquals("banana", smap.walk(new LVar(1)));
        assertEquals("mango", smap.walk("mango"));

    }

    @Test
    public void walk() {
        var smap = new SubstMap(HashMap.of(new LVar(0), new LVar(1), new LVar(1), "banana"));

        assertEquals("banana", smap.walk(new LVar(0)));
        assertEquals("banana", smap.walk(new LVar(1)));
        assertEquals("mango", smap.walk("mango"));

    }

    @Test
    public void unify() {
        {
            SubstMap smap = new SubstMap().unify("mango", "banana");
            assertEquals(new SubstMap(false), smap);
        }
        {
            SubstMap smap = new SubstMap().unify(new LVar(0), "banana");
            assertEquals("banana", smap.walk(new LVar(0)));
        }
        {
            SubstMap smap = new SubstMap().unify("banana", new LVar(0));
            assertEquals("banana", smap.walk(new LVar(0)));
        }
        {
            SubstMap smap = new SubstMap().add(new LVar(0), "banana")
                                          .unify(new LVar(0), "banana");
            assertEquals("banana", smap.walk(new LVar(0)));
        }
        {
            SubstMap smap = new SubstMap().add(new LVar(0), "mango")
                                          .unify(new LVar(0), "banana");
            assertNotEquals("banana", smap.walk(new LVar(0)));
            assertEquals(new SubstMap(false), smap);
        }
        {
            SubstMap smap = new SubstMap().add(new LVar(9), "squirrels")
                                          .unify(new LVar(0), "banana");

            assertEquals("banana", smap.walk(new LVar(0)));
            assertNotEquals("banana", smap.walk(new LVar(9)));

            assertEquals("squirrels", smap.walk(new LVar(9)));
            assertNotEquals("squirrels", smap.walk(new LVar(0)));

        }
        {
            SubstMap smap = new SubstMap().add(new LVar(0), new LVar(1))
                                          .unify(new LVar(1), new LVar(2));

            assertEquals(new LVar(2), smap.walk(new LVar(0)));
            assertEquals(new LVar(2), smap.walk(new LVar(1)));

        }

    }

}
