package in.neolog.minikanren;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class TestSubstMap {

    @Test
    public void add() {
        var x = LVar.create();
        var y = LVar.create();

        var smap = new SubstMap().add(x, y)
                                 .add(y, "banana");

        assertEquals("banana", smap.walk(x));
        assertEquals("banana", smap.walk(y));
        assertEquals("mango", smap.walk("mango"));

    }

    @Test
    public void walk() {
        var x = LVar.create();
        var y = LVar.create();

        var smap = new SubstMap().add(x, y)
                                 .add(y, "banana");

        assertEquals("banana", smap.walk(x));
        assertEquals("banana", smap.walk(y));
        assertEquals("mango", smap.walk("mango"));

    }

    @Test
    public void unify() {
        {
            SubstMap smap = new SubstMap().unify("mango", "banana");
            assertEquals(SubstMap.invalidMap(), smap);
        }
        {
            var x = LVar.create();
            var smap = new SubstMap().unify(x, "banana");
            assertEquals("banana", smap.walk(x));
        }
        {
            var x = LVar.create();
            var smap = new SubstMap().unify("banana", x);
            assertEquals("banana", smap.walk(x));
        }
        {
            var x = LVar.create();
            var smap = new SubstMap().add(x, "banana")
                                     .unify(x, "banana");
            assertEquals("banana", smap.walk(x));
        }
        {
            var x = LVar.create();
            var smap = new SubstMap().add(x, "mango")
                                     .unify(x, "banana");
            assertNotEquals("banana", smap.walk(x));
            assertEquals(SubstMap.invalidMap(), smap);
        }
        {
            var x = LVar.create();
            var y = LVar.create();

            var smap = new SubstMap().add(x, "squirrels")
                                     .unify(y, "banana");

            assertEquals("banana", smap.walk(y));
            assertNotEquals("banana", smap.walk(x));

            assertEquals("squirrels", smap.walk(x));
            assertNotEquals("squirrels", smap.walk(y));

        }
        {
            var x = LVar.create();
            var y = LVar.create();
            var z = LVar.create();

            var smap = new SubstMap().add(x, y)
                                     .unify(y, z);

            assertEquals(z, smap.walk(x));
            assertEquals(z, smap.walk(y));
        }

    }

}
