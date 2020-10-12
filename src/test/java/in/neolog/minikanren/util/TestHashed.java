/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestHashed {

    @Test
    public void immutableCorrect() {
        String str = "random-string";
        var hashed = Hashed.of(str);
        assertEquals(str.hashCode(), hashed.hashCode());
    }

    @Test
    public void mutableWrong() {
        List<String> buf = new ArrayList<>();
        buf.add("random-");
        var hashed = Hashed.of(buf);

        assertEquals(buf.hashCode(), hashed.hashCode());

        buf.add("string");
        assertNotEquals(buf.hashCode(), hashed.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void nullObj() {
        Hashed.of(null);
    }

}
