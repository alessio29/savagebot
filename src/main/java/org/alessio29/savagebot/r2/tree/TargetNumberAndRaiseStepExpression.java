package org.alessio29.savagebot.r2.tree;

public class TargetNumberAndRaiseStepExpression extends Expression implements WithTargetNumberAndRaiseStep {
    private final Expression targetNumberArg;
    private final Expression raiseStepArg;
    private final Expression targetNumberAndRaiseStepArg;
    private final Expression expression;

    public TargetNumberAndRaiseStepExpression(
            String text,
            Expression targetNumberArg,
            Expression raiseStepArg,
            Expression targetNumberAndRaiseStepArg,
            Expression expression
    ) {
        super(text);
        this.targetNumberArg = targetNumberArg;
        this.raiseStepArg = raiseStepArg;
        this.targetNumberAndRaiseStepArg = targetNumberAndRaiseStepArg;
        this.expression = expression;
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

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitTargetNumberAndRaiseStepExpression(this);
    }
}
