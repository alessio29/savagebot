package org.alessio29.savagebot.r2.parse;

import org.alessio29.savagebot.r2.tree.Expression;
import org.alessio29.savagebot.r2.tree.TargetNumberMode;

public class TargetNumberAndRaiseStep {
    private final Expression targetNumber;
    private final Expression raiseStep;
    private final Expression targetNumberAndRaiseStep;
    private final TargetNumberMode mode;

    public TargetNumberAndRaiseStep(
            TargetNumberMode mode,
            Expression targetNumber,
            Expression raiseStep,
            Expression targetNumberAndRaiseStep
    ) {
        this.mode = mode;
        this.targetNumber = targetNumber;
        this.raiseStep = raiseStep;
        this.targetNumberAndRaiseStep = targetNumberAndRaiseStep;
    }

    public TargetNumberMode getMode() {
        return mode;
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
