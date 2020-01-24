package org.alessio29.savagebot.r2.tree;

public class WegD6RollExpression extends Expression {

    private final Expression diceCountArg;

    public WegD6RollExpression(String text, Expression diceCountArg) {
        super(text);
        this.diceCountArg = diceCountArg;
    }

    public Expression getDiceCountArg() {
        return diceCountArg;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitWegD6Expression(this);
    }
}
