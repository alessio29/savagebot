package org.alessio29.savagebot.r2.tree;

public class SwordWorldPowerRollExpression extends Expression {

    private final Expression power;
    private final Expression critical;

    public SwordWorldPowerRollExpression(String text, Expression power, Expression critical) {
        super(text);
        this.power = power;
        this.critical = critical;
    }

    public Expression getPower() {
        return power;
    }

    public Expression getCritical() {
        return critical;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitSwordWorldPowerRollExpression(this);
    }
}
