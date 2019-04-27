package org.alessio29.savagebot.r2.tree;

public class IntExpression extends Expression {
    private final int value;

    public IntExpression(String text, int value) {
        super(text);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitIntExpression(this);
    }
}
