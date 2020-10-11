package in.neolog.minikanren;

import static in.neolog.minikanren.MinKan.and;
import static in.neolog.minikanren.MinKan.diseq;
import static in.neolog.minikanren.MinKan.or;
import static in.neolog.minikanren.MinKan.unify;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.Test;

import in.neolog.minikanren.goal.Goal;
import in.neolog.minikanren.reify.Reify;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;

public class TestTicTacToe {

    final String empty  = "-";
    final String cross  = "X";
    final String circle = "O";

    @SuppressWarnings("boxing")
    @Test
    public void ticTacToe() {
        try (Reify reify = Reify.reify()) {
            var pos = List.range(0, 3)
                          .map(i -> List.range(0, 3)
                                        .map(j -> Tuple.of(i, j)));
            var allPos = pos.flatMap(l -> l);

            final int totalMoves = 6; // cross moves first and circle wins

            var boards = List.range(0, totalMoves + 1)
                             .map(i -> pos.map(l -> l.map(t -> LVar.create())));

            Goal game = firstFourMoves(boards, cross)
                                                     .and(move(boards.get(4), boards.get(5), cross),
                                                             noWin(allPos, boards.get(5), cross))
                                                     .and(move(boards.get(5), boards.get(6), circle),
                                                             win(allPos, boards.get(6), circle));
            var results = new Run().run(boards.get(6)
                                              .flatMap(l -> l),
                    game, 10);
            /*
             * results.forEach(smap -> pos.forEach(l -> System.out.println(l.map(t -> { var lv = boards.get(6)
             * .get(t._1) .get(t._2); return lv.toString() + "@" + t.toString() + " = " + smap.getOrElse(lv, empty)
             * .toString(); }))));
             */

            results.forEach(smap -> {
                var allCircles = allPos.filter(t -> smap.getOrElse(boards.get(6)
                                                                         .get(t._1)
                                                                         .get(t._2),
                        empty)
                                                        .equals(circle));
                var allCrosses = allPos.filter(t -> smap.getOrElse(boards.get(6)
                                                                         .get(t._1)
                                                                         .get(t._2),
                        empty)
                                                        .equals(cross));
                assertEquals(1, allCircles.combinations(2)
                                          .map(c -> slope(c.get(0), c.get(1)))
                                          .distinct()
                                          .size());
                assertEquals(3, allCrosses.combinations(2)
                                          .map(c -> slope(c.get(0), c.get(1)))
                                          .distinct()
                                          .size());
            });

        }
    }

    private Goal firstFourMoves(List<List<List<LVar>>> boards, String startingMark) {
        String otherMark;
        if (cross.equals(startingMark)) {
            otherMark = circle;
        } else {
            otherMark = cross;
        }
        return blank(boards.get(0)).and(move(boards.get(0), boards.get(1), startingMark))
                                   .and(move(boards.get(1), boards.get(2), otherMark))
                                   .and(move(boards.get(2), boards.get(3), startingMark))
                                   .and(move(boards.get(3), boards.get(4), otherMark));
    }

    private Goal move(List<List<LVar>> board1, List<List<LVar>> board2, String mark) {
        var from = board1.flatMap(l -> l);
        var to = board2.flatMap(l -> l);
        var paired = from.zip(to)
                         .toSet();
        return or(paired.<Goal>map(t -> {
            var rest = paired.remove(t);
            var eqRest = and(rest.<Goal>map(r -> unify(r._1, r._2))
                                 .toList());
            return eqRest.unify(t._1, empty)
                         .unify(t._2, mark);
        })
                        .toList());
    }

    private Goal blank(List<List<LVar>> board) {
        return and(board.flatMap(l -> l.map(lv -> unify(lv, empty))));
    }

    private Goal full(List<List<LVar>> board) {
        return and(board.flatMap(l -> l.map(lv -> unify(lv, cross).or(unify(lv, circle)))));
    }

    private Goal notFull(List<List<LVar>> board) {
        return or(board.flatMap(l -> l.map(lv -> unify(lv, empty))));
    }

    @SuppressWarnings("boxing")
    private Goal win(List<Tuple2<Integer, Integer>> allPos, List<List<LVar>> board, String mark) {
        var allLines = allLines(allPos, board);

        return or(allLines.<Goal>map(l -> and(l.map(t -> unify(board.get(t._1)
                                                                    .get(t._2),
                mark)))));
    }

    @SuppressWarnings("boxing")
    private Goal noWin(final List<Tuple2<Integer, Integer>> allPos, final List<List<LVar>> board, String mark) {
        var allLines = allLines(allPos, board);

        return and(allLines.<Goal>map(l -> or(l.map(t -> diseq(mark, board.get(t._1)
                                                                          .get(t._2))))));
    }

    private <C> List<List<Tuple2<Integer, Integer>>> lineWise(List<Tuple2<Integer, Integer>> allPos,
            Predicate<Tuple2<Integer, Integer>> filterFn, Function<Tuple2<Integer, Integer>, C> groupFn) {
        return allPos.filter(filterFn)
                     .groupBy(groupFn)
                     .values()
                     .toList();
    }

    @SuppressWarnings("boxing")
    private List<List<Tuple2<Integer, Integer>>> allLines(List<Tuple2<Integer, Integer>> allPos,
            List<List<LVar>> board) {
        var rows = lineWise(allPos, t -> true, t -> t._1);
        var cols = lineWise(allPos, t -> true, t -> t._2);
        var diag = lineWise(allPos, t -> t._1.intValue() == t._2.intValue(), t -> "");
        var antiDiag = lineWise(allPos, t -> (board.size() - 1 - t._1) == t._2.intValue(), t -> "");

        var allLines = rows.appendAll(cols)
                           .appendAll(diag)
                           .appendAll(antiDiag);
        return allLines;
    }

    @SuppressWarnings("boxing")
    private double slope(Tuple2<Integer, Integer> t1, Tuple2<Integer, Integer> t2) {
        double yy = t2._1 - t1._1;
        double xx = t2._2 - t1._2;
        if (xx == 0) {
            return Double.POSITIVE_INFINITY;
        } else
            return yy / xx;
    }

}
