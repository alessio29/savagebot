package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.Expression;
import org.alessio29.savagebot.r2.tree.TargetNumberMode;

import java.util.HashMap;
import java.util.Map;

public class ExpressionContext {
    private final Expression topExpression;
    private final CommandContext commandContext;
    private final Map<Expression, String> explanations = new HashMap<>();

    private TargetNumberMode targetNumberMode = null;
    private int targetNumber = 4;
    private int savageWorldsRaiseStep = 4;
    private boolean swordWorldAutoFail = false;

    public ExpressionContext(Expression topExpression, CommandContext commandContext) {
        this.topExpression = topExpression;
        this.commandContext = commandContext;
    }

    public Expression getTopExpression() {
        return topExpression;
    }

    public void putExplanation(Expression expression, String explanation) {
        explanations.put(expression, explanation);
    }

    public String getExplanation(Expression expression) {
        return explanations.get(expression);
    }

    public CommandContext getCommandContext() {
        return commandContext;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setSavageWorldsRaiseStep(int raiseStep) {
        savageWorldsRaiseStep = raiseStep;
    }

    public int getSavageWorldsRaiseStep() {
        return savageWorldsRaiseStep;
    }

    public void setTargetNumberMode(TargetNumberMode targetNumberMode) {
        this.targetNumberMode = targetNumberMode;
    }

    public TargetNumberMode getTargetNumberMode() {
        return targetNumberMode;
    }

    public void setSwordWorldAutoFail(boolean autoFail) {
        this.swordWorldAutoFail = autoFail;
    }

    public boolean isSwordWorldAutoFail() {
        return swordWorldAutoFail;
    }
}
