/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import java.io.Serializable;
import java.util.UUID;

import in.neolog.minikanren.reify.Reify;

public class LVar implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;

    public static LVar create() {
        return new LVar();
    }

    private LVar() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LVar other = (LVar) obj;
        if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public synchronized String toString() {
        return Reify.name(this)
                    .orElse("LVar[" + id.toString() + "]");
    }

}
