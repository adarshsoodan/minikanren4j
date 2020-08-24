package in.neolog.minikanren;

import java.io.Serializable;
import java.util.UUID;

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
    public String toString() {
        return "LVar [id=" + id + "]";
    }

}
