/*
 * Copyright Adarsh Soodan, 2020
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */
package in.neolog.minikanren;

import java.io.Serializable;
import java.util.function.Function;

import in.neolog.minikanren.goal.And;
import in.neolog.minikanren.goal.Diseq;
import in.neolog.minikanren.goal.Fresh;
import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.goal.Or;
import in.neolog.minikanren.goal.Unify;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class MinKan {

    private MinKan() {
    }

    public static Fresh fresh(Function<LVar, Goal> unbound) {
        return new Fresh(unbound);
    }

    public static Or or(Goal... goals) {
        return new Or(List.of(goals));
    }

    public static Or or(Seq<Goal> goals) {
        return new Or(goals.toList());
    }

    public static And and(Goal... goals) {
        return new And(List.of(goals));
    }

    public static And and(Seq<Goal> goals) {
        return new And(goals.toList());
    }

    public static Unify unify(Serializable u, Serializable v) {
        return new Unify(u, v);
    }

    public static Diseq diseq(Serializable u, Serializable v) {
        return new Diseq(u, v);
    }

}
