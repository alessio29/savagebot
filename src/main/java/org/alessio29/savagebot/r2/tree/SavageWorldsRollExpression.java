package org.alessio29.savagebot.r2.tree;

public class SavageWorldsRollExpression extends Expression implements WithTargetNumberAndRaiseStep {
    private final Expression diceCountArg;
    private final Expression abilityDieArg;
    private final Expression wildDieArg;
    private final Expression targetNumberArg;
    private final Expression raiseStepArg;
    private final Expression targetNumberAndRaiseStepArg;

    public SavageWorldsRollExpression(
            String text,
            Expression diceCountArg,
            Expression abilityDieArg,
            Expression wildDieArg,
            Expression targetNumberArg,
            Expression raiseStepArg,
            Expression targetNumberAndRaiseStepArg
    ) {
        super(text);
        this.diceCountArg = diceCountArg;
        this.abilityDieArg = abilityDieArg;
        this.wildDieArg = wildDieArg;
        this.targetNumberArg = targetNumberArg;
        this.raiseStepArg = raiseStepArg;
        this.targetNumberAndRaiseStepArg = targetNumberAndRaiseStepArg;
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
    public TargetNumberMode getTargetNumberMode() {
        return TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES;
    }

    @Override
    public Expression getTargetNumberArg() {
        return targetNumberArg;
    }

    @Override
    public Expression getRaiseStepArg() {
        return raiseStepArg;
    }

    @Override
    public Expression getTargetNumberAndRaiseStepArg() {
        return targetNumberAndRaiseStepArg;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitSavageWorldsRollExpression(this);
    }
}
