package org.alessio29.savagebot.r2.tree;

public class TargetNumberAndRaiseStepExpression extends Expression {
    private final TargetNumberMode targetNumberMode;
    private final Expression targetNumberArg;
    private final Expression raiseStepArg;
    private final Expression targetNumberAndRaiseStepArg;
    private final Expression expression;

    public TargetNumberAndRaiseStepExpression(
            String text,
            TargetNumberMode targetNumberMode,
            Expression targetNumberArg,
            Expression raiseStepArg,
            Expression targetNumberAndRaiseStepArg,
            Expression expression
    ) {
        super(text);
        this.targetNumberMode = targetNumberMode;
        this.targetNumberArg = targetNumberArg;
        this.raiseStepArg = raiseStepArg;
        this.targetNumberAndRaiseStepArg = targetNumberAndRaiseStepArg;
        this.expression = expression;
    }

    public TargetNumberMode getTargetNumberMode() {
        return targetNumberMode;
    }

    public Expression getTargetNumberArg() {
        return targetNumberArg;
    }

    public Expression getRaiseStepArg() {
        return raiseStepArg;
    }

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
