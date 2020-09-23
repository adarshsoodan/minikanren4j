/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.reify;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import in.neolog.minikanren.LVar;

public class Reify implements AutoCloseable {
    private AtomicInteger               nameN = new AtomicInteger(0);
    private ConcurrentMap<LVar, String> names = new ConcurrentHashMap<>();

    private static final ThreadLocal<Reify> simpleNames = new ThreadLocal<>();

    private Reify() {
    }

    public static Reify reify() {
        var reify = new Reify();
        simpleNames.set(reify);
        return reify;
    }

    @Override
    public void close() {
        simpleNames.set(null);
    }

    public static void resetSimpleNames() {
        simpleNames.set(new Reify());
    }

    public static Optional<String> name(LVar lv) {
        Reify reify = simpleNames.get();
        if (reify == null) {
            return Optional.empty();
        }
        String name = reify.names.computeIfAbsent(lv, x -> "_." + reify.nameN.getAndIncrement());
        return Optional.of(name);
    }

}
