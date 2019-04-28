package org.alessio29.savagebot.r2.tree;

public class D66RollExpression extends Expression {
    private final int digitsCount;

    public D66RollExpression(String text, int digitsCount) {
        super(text);
        this.digitsCount = digitsCount;
    }

    public int getDigitsCount() {
        return digitsCount;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitD66RollExpression(this);
    }
}
