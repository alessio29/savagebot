package org.alessio29.savagebot.r2.tree;

public class GygaxRangeRollExpression extends Expression {
    private final int min;
    private final int max;

    public GygaxRangeRollExpression(String text, int min, int max) {
        super(text);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitGygaxRangeRollExpression(this);
    }
}
