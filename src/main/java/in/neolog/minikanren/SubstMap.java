/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.UnaryOperator;

import in.neolog.minikanren.util.Hashed;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class SubstMap implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final SubstMap FAILED_INSTANCE = new SubstMap(false);

    private final boolean valid;

    // Use Hashed for all except LVar.
    private final Map<LVar, Serializable>                              eq;
    private final Map<Hashed<Serializable>, Set<Hashed<Serializable>>> eqBack;

    private final Set<Tuple2<Hashed<Serializable>, Hashed<Serializable>>>                           diseq;
    private final Set<Hashed<Serializable>>                                                         allDiseq;
    private final Map<Hashed<Serializable>, Hashed<Set<Hashed<Serializable>>>>                      cacheEqC;
    private final Set<Tuple2<Hashed<Set<Hashed<Serializable>>>, Hashed<Set<Hashed<Serializable>>>>> diseqAsEqC;

    public SubstMap() {
        this(true);
    }

    private SubstMap(boolean valid, Map<LVar, Serializable> eq,
            Map<Hashed<Serializable>, Set<Hashed<Serializable>>> eqBack,
            Set<Tuple2<Hashed<Serializable>, Hashed<Serializable>>> diseq, Set<Hashed<Serializable>> allDiseq,
            Map<Hashed<Serializable>, Hashed<Set<Hashed<Serializable>>>> cacheEqC,
            Set<Tuple2<Hashed<Set<Hashed<Serializable>>>, Hashed<Set<Hashed<Serializable>>>>> diseqAsEqC) {
        this.valid = valid;
        this.eq = eq;
        this.eqBack = eqBack;
        this.diseq = diseq;
        this.allDiseq = allDiseq;
        this.cacheEqC = cacheEqC;
        this.diseqAsEqC = diseqAsEqC;
    }

    public static SubstMap invalidMap() {
        return FAILED_INSTANCE;
    }

    private SubstMap(boolean valid) {
        this.valid = valid;
        this.eq = HashMap.empty();
        this.eqBack = HashMap.empty();
        this.diseq = HashSet.empty();
        this.allDiseq = HashSet.empty();
        this.cacheEqC = HashMap.empty();
        this.diseqAsEqC = HashSet.empty();
    }

    public boolean isValid() {
        return valid;
    }

    public Serializable walk(Serializable x) {
        if (x instanceof LVar) {
            var y = Option.of(x);
            var yy = y.getOrNull();
            while (y.isDefined() && yy instanceof LVar && eq.containsKey((LVar) yy)) {
                y = eq.get((LVar) yy);
                yy = y.getOrNull();
            }
            return y.getOrElse(x);
        } else {
            return x;
        }
    }

    public SubstMap unify(Serializable u, Serializable v) {
        if (!valid) {
            return this;
        }
        var hu = Hashed.of(u);
        var hv = Hashed.of(v);
        if (allDiseq.contains(hu) || allDiseq.contains(hv)) {
            var uEqC = cacheEqC.get(hu)
                               .getOrElse(() -> computeEqC(hu));
            var vEqC = cacheEqC.get(hv)
                               .getOrElse(() -> computeEqC(hv));

            if (allDiseq.contains(hu) && allDiseq.contains(hv)) {
                var tuvEqC = Tuple.of(uEqC, vEqC);
                if (diseqAsEqC.exists(tuvEqC::equals)) {
                    return FAILED_INSTANCE;
                }
            }
            var uvEqC = Hashed.of(uEqC.obj()
                                      .union(vEqC.obj()));
            var newCacheEqC = uvEqC.obj()
                                   .foldLeft(cacheEqC, (mapEqC, key) -> mapEqC.put(key, uvEqC));

            UnaryOperator<Hashed<Set<Hashed<Serializable>>>> ef =
                                                                e1 -> (e1.equals(uEqC) || e1.equals(vEqC)) ? uvEqC : e1;
            var newDiseqAsEqC = diseqAsEqC.map(t2 -> t2.map(ef, ef));

            var eqOnly = unifyNonDiseq(u, v);
            if (eqOnly.valid) {
                return new SubstMap(true, eqOnly.eq, eqOnly.eqBack, diseq, allDiseq.union(uvEqC.obj()), newCacheEqC,
                        newDiseqAsEqC);
            } else {
                return eqOnly;
            }
        } else {
            return unifyNonDiseq(u, v);
        }
    }

    public SubstMap diseq(Serializable u, Serializable v) {
        if (!valid) {
            return this;
        }
        var hu = Hashed.of(u);
        var hv = Hashed.of(v);
        var uvEqC = cacheEqC.computeIfAbsent(hu, this::computeEqC)._2.computeIfAbsent(hv, this::computeEqC)._2;
        var uEqC = uvEqC.get(hu)
                        .get();
        var vEqC = uvEqC.get(hv)
                        .get();
        if (uEqC.equals(vEqC)) {
            return FAILED_INSTANCE;
        }

        var newDiseq = diseq.add(Tuple.of(hu, hv))
                            .add(Tuple.of(hv, hu));
        var newAllDiseq = allDiseq.addAll(uEqC.obj()
                                              .union(vEqC.obj()));

        var uCacheEqC = uEqC.obj()
                            .foldLeft(uvEqC, (mapEqC, key) -> mapEqC.put(key, uEqC));
        var uvCacheEqC = vEqC.obj()
                             .foldLeft(uCacheEqC, (mapEqC, key) -> mapEqC.put(key, vEqC));

        var newDiseqAsEqC = diseqAsEqC.add(Tuple.of(uEqC, vEqC))
                                      .add(Tuple.of(vEqC, uEqC));

        return new SubstMap(true, eq, eqBack, newDiseq, newAllDiseq, uvCacheEqC, newDiseqAsEqC);
    }

    public Hashed<Set<Hashed<Serializable>>> computeEqC(Hashed<Serializable> x) {
        return Hashed.of(walkGather(x).obj()
                                      .add(x));
    }

    private SubstMap add(LVar lvar, Serializable right) {
        if (valid) {
            var hlvar = Hashed.<Serializable>of(lvar);
            var hright = Hashed.of(right);
            var newEq = eq.put(lvar, right);
            var newEqBack = eqBack.put(hright, HashSet.of(hlvar), (pre, now) -> pre.add(hlvar));
            return new SubstMap(true, newEq, newEqBack, diseq, allDiseq, cacheEqC, diseqAsEqC);
        }
        return this;
    }

    private SubstMap unifyNonDiseq(Serializable u, Serializable v) {
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

    private Hashed<Set<Hashed<Serializable>>> walkGather(Hashed<Serializable> x) {
        return backWalkGather(Hashed.of(walk(x.obj())));
    }

    private Hashed<Set<Hashed<Serializable>>> backWalkGather(Hashed<Serializable> x) {
        Set<Hashed<Serializable>> ret = HashSet.empty();

        Queue<Option<Set<Hashed<Serializable>>>> q = new LinkedList<>();
        q.add(Option.of(HashSet.of(x)));
        while (!q.isEmpty()) {
            var y = q.poll();
            var yy = y.getOrNull();
            if (yy != null) {
                yy.forEach(s -> q.add(eqBack.get(s)));
                ret = ret.union(yy);
            }
        }

        return Hashed.of(ret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allDiseq, diseq, diseqAsEqC, cacheEqC, eq, eqBack, Boolean.valueOf(valid));
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
        return Objects.equals(allDiseq, other.allDiseq) && Objects.equals(diseq, other.diseq)
               && Objects.equals(diseqAsEqC, other.diseqAsEqC) && Objects.equals(cacheEqC, other.cacheEqC)
               && Objects.equals(eq, other.eq) && Objects.equals(eqBack, other.eqBack) && valid == other.valid;
    }

    @Override
    public String toString() {
        return "SubstMap [valid=" + valid + ", eq=" + eq + ", eqBack=" + eqBack + ", diseq=" + diseq + ", allDiseq="
               + allDiseq + ", cacheEqC=" + cacheEqC + ", diseqAsEqC=" + diseqAsEqC + "]";
    }

}
