/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren.util;

import java.util.Objects;

public class Hashed<T> {
    private final T obj;

    private final int hash;

    @SuppressWarnings("unchecked")
    public static <T> Hashed<T> of(T obj) {
        Objects.requireNonNull(obj, "Hashed object cannot be null");
        if (obj instanceof Hashed) {
            return (Hashed<T>) obj;
        }
        return new Hashed<>(obj, obj.hashCode());
    }

    public T getObj() {
        return obj;
    }

    public T obj() {
        return obj;
    }

    private Hashed(T obj, int hash) {
        this.obj = obj;
        this.hash = hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        @SuppressWarnings("rawtypes")
        Hashed other = (Hashed) obj;
        return hash == other.hash && Objects.equals(this.obj, other.obj);
    }

    @Override
    public String toString() {
        return "Hashed [obj=" + obj + ", hash=" + hash + "]";
    }

}
