package org.alessio29.savagebot.r2.tree;

public abstract class Statement extends Node {
    Statement(String text) {
        super(text);
    }

    public abstract <V> V accept(Visitor<V> visitor);

    public interface Visitor<V> {
        V visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement);

        V visitErrorStatement(ErrorStatement errorStatement);

        V visitRollOnceStatement(RollOnceStatement rollOnceStatement);

        V visitRollTimesStatement(RollTimesStatement rollTimesStatement);

        V visitRollBatchTimesStatement(RollBatchTimesStatement rollBatchTimesStatement);
    }
}
