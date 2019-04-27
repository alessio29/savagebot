package org.alessio29.savagebot.r2.tree;

public class RollOnceStatement extends Statement {
    private final Expression expression;

    public RollOnceStatement(String text, Expression expression) {
        super(text);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitRollOnceStatement(this);
    }
}
