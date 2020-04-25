package org.alessio29.savagebot.r2.tree;

public class SavageWorldsRollExpression extends Expression {
    private final Expression diceCountArg;
    private final Expression abilityDieArg;
    private final Expression wildDieArg;

    public SavageWorldsRollExpression(
            String text,
            Expression diceCountArg,
            Expression abilityDieArg,
            Expression wildDieArg
    ) {
        super(text);
        this.diceCountArg = diceCountArg;
        this.abilityDieArg = abilityDieArg;
        this.wildDieArg = wildDieArg;
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

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitSavageWorldsRollExpression(this);
    }
}
