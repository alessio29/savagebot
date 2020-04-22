package org.alessio29.savagebot.r2.tree;

public class SavageWorldsExtrasRollExpression extends Expression implements WithTargetNumberAndRaiseStep {
    private final Expression facetsArg;
    private final OperatorExpression.Operator modifierOperator;
    private final Expression modifierArg;
    private final Expression targetNumberArg;
    private final Expression raiseStepArg;
    private final Expression targetNumberAndRaiseStepArg;

    public SavageWorldsExtrasRollExpression(
            String text,
            Expression facetsArg,
            OperatorExpression.Operator modifierOperator,
            Expression modifierArg,
            Expression targetNumberArg,
            Expression raiseStepArg,
            Expression targetNumberAndRaiseStepArg
    ) {
        super(text);
        this.facetsArg = facetsArg;
        this.modifierOperator = modifierOperator;
        this.modifierArg = modifierArg;
        this.targetNumberArg = targetNumberArg;
        this.raiseStepArg = raiseStepArg;
        this.targetNumberAndRaiseStepArg = targetNumberAndRaiseStepArg;
    }

    public Expression getFacetsArg() {
        return facetsArg;
    }

    public OperatorExpression.Operator getModifierOperator() {
        return modifierOperator;
    }

    public Expression getModifierArg() {
        return modifierArg;
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
        return visitor.visitExtrasRollExpression(this);
    }
}
