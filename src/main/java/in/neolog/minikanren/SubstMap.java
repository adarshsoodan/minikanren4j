package in.neolog.minikanren;

import java.io.Serializable;
import java.util.Objects;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public class SubstMap {

    private static final SubstMap FAILED_INSTANCE = new SubstMap(false);

    private final boolean valid;

    private final Map<LVar, Serializable> map;

    public SubstMap() {
        this.valid = true;
        this.map = HashMap.empty();
    }

    public SubstMap(Map<LVar, Serializable> init) {
        this.valid = true;
        this.map = init;
    }

    public SubstMap(boolean valid) {
        this.valid = valid;
        this.map = HashMap.empty();
    }

    public boolean isValid() {
        return valid;
    }

    public SubstMap add(LVar lvar, Serializable right) {
        if (valid) {
            return new SubstMap(map.put(lvar, right));
        }
        return this;
    }

    public Serializable walk(Serializable x) {
        if (x instanceof LVar) {
            var y = Option.of(x);
            while (y.isDefined() && y.getOrNull() instanceof LVar && map.containsKey((LVar) y.getOrNull())) {
                y = map.get((LVar) y.getOrNull());
            }
            return y.getOrElse(x);
        } else {
            return x;
        }
    }

    public SubstMap unify(Serializable u, Serializable v) {
        var x = walk(u);
        var y = walk(v);

        if (Objects.equals(x, y)) {
            return this;
        } else if (x instanceof LVar) {
            return add((LVar) x, y);
        } else if (y instanceof LVar) {
            return add((LVar) y, x);
        } else {
            return FAILED_INSTANCE;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result + (valid ? 1231 : 1237);
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
        SubstMap other = (SubstMap) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        if (valid != other.valid)
            return false;
        return true;
    }

}
