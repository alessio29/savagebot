package org.alessio29.savagebot.r2.tree;

import org.alessio29.savagebot.r2.tree.Expression;
import org.alessio29.savagebot.r2.tree.Statement;

public class RollTimesStatement extends Statement {
    private final Expression times;
    private final Expression expression;

    public RollTimesStatement(String text, Expression times, Expression expression) {
        super(text);
        this.times = times;
        this.expression = expression;
    }

    public Expression getTimes() {
        return times;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitRollTimesStatement(this);
    }
}
