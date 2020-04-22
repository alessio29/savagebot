package org.alessio29.savagebot.r2.parse;

import org.alessio29.savagebot.r2.tree.Expression;

public class TargetNumberAndRaiseStep {
    private final Expression targetNumber;
    private final Expression raiseStep;
    private final Expression targetNumberAndRaiseStep;

    public TargetNumberAndRaiseStep(
            Expression targetNumber,
            Expression raiseStep,
            Expression targetNumberAndRaiseStep
    ) {
        this.targetNumber = targetNumber;
        this.raiseStep = raiseStep;
        this.targetNumberAndRaiseStep = targetNumberAndRaiseStep;
    }

    public Expression getTargetNumber() {
        return targetNumber;
    }

    public Expression getRaiseStep() {
        return raiseStep;
    }

    public Expression getTargetNumberAndRaiseStep() {
        return targetNumberAndRaiseStep;
    }
}
