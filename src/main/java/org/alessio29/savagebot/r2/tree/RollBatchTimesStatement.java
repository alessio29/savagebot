package org.alessio29.savagebot.r2.tree;

import java.util.List;

public class RollBatchTimesStatement extends Statement {
    private final Expression times;
    private final List<Expression> expressions;

    public RollBatchTimesStatement(String text, Expression times, List<Expression> expressions) {
        super(text);
        this.times = times;
        this.expressions = expressions;
    }

    public Expression getTimes() {
        return times;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitRollBatchTimesStatement(this);
    }
}
