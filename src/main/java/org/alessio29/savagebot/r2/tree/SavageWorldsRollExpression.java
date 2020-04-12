package org.alessio29.savagebot.r2.tree;

public class SavageWorldsRollExpression extends Expression {
    private final Expression diceCountArg;
    private final Expression abilityDieArg;
    private final Expression wildDieArg;
    private final Expression targetNumberArg;

    public SavageWorldsRollExpression(
            String text,
            Expression diceCountArg,
            Expression abilityDieArg,
            Expression wildDieArg,
            Expression targetNumberArg
    ) {
        super(text);
        this.diceCountArg = diceCountArg;
        this.abilityDieArg = abilityDieArg;
        this.wildDieArg = wildDieArg;
        this.targetNumberArg = targetNumberArg;
    }

    public Expression getDiceCountArg() {
        return diceCountArg;
    }

    public Expression getAbilityDieArg() {
        return abilityDieArg;
    }

    public Expression getWildDieArg() {
        return wildDieArg;
    }

    public Expression getTargetNumberArg() {
        return targetNumberArg;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitSavageWorldsRollExpression(this);
    }
}
