package in.neolog.minikanren;

import java.io.Serializable;

public class LVar implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;

    public LVar(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
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
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LVar [id=" + id + "]";
    }

    
}
