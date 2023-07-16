package org.alessio29.savagebot.r2.tree;

public class SwordWorldPowerRollExpression extends Expression {

    private final Expression power;
    private final Expression critical;
    private final Expression autoFailThreshold;
    private final Expression numDice;
    private final Expression rollModifier;
    private final int rollModifierSign;
    private final boolean withHumanSwordGrace;

    public SwordWorldPowerRollExpression(
            String text,
            Expression power,
            Expression critical,
            Expression autoFailThreshold,
            Expression numDice,
            Expression rollModifier,
            int rollModifierSign,
            boolean withHumanSwordGrace
    ) {
        super(text);
        this.power = power;
        this.critical = critical;
        this.autoFailThreshold = autoFailThreshold;
        this.numDice = numDice;
        this.rollModifier = rollModifier;
        this.rollModifierSign = rollModifierSign;
        this.withHumanSwordGrace = withHumanSwordGrace;
    }

    public Expression getPower() {
        return power;
    }

    public Expression getCritical() {
        return critical;
    }

    public Expression getAutoFailThreshold() {
        return autoFailThreshold;
    }

    public Expression getNumDice() {
        return numDice;
    }

    public Expression getRollModifier() {
        return rollModifier;
    }

    public int getRollModifierSign() {
        return rollModifierSign;
    }

    public boolean isWithHumanSwordGrace() {
        return withHumanSwordGrace;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitSwordWorldPowerRollExpression(this);
    }
}
