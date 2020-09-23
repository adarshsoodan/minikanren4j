package in.neolog.minikanren;

import java.io.Serializable;
import java.util.function.Function;

import in.neolog.minikanren.goal.And;
import in.neolog.minikanren.goal.Fresh;
import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.goal.NotEq;
import in.neolog.minikanren.goal.Or;
import in.neolog.minikanren.goal.Unify;

public class MinKan {

    public static Fresh fresh(Function<LVar, Goal> unbound) {
        return new Fresh(unbound);
    };

    public static Or or(Goal... goals) {
        return new Or(goals);
    };

    public static And and(Goal... goals) {
        return new And(goals);
    };

    public static Unify unify(Serializable u, Serializable v) {
        return new Unify(u, v);
    };

    public static NotEq noteq(Serializable u, Serializable v) {
        return new NotEq(u, v);
    };

}
