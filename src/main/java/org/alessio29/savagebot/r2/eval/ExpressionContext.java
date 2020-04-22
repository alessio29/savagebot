package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.Expression;

import java.util.HashMap;
import java.util.Map;

class ExpressionContext {
    private final Expression topExpression;
    private final CommandContext commandContext;
    private final Map<Expression, String> explanations = new HashMap<>();

    private boolean savageWorldsMarginOfSuccessRequired = false;
    private boolean treatMarginOfSuccessAsSuccessesAndRaises = false;
    private int savageWorldsTargetNumber = 4;
    private int savageWorldsRaiseStep = 4;

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

    public void setSavageWorldsMarginOfSuccessRequired(boolean savageWorldsMarginOfSuccessRequired) {
        this.savageWorldsMarginOfSuccessRequired = savageWorldsMarginOfSuccessRequired;
    }

    public void setTreatMarginOfSuccessAsSuccessesAndRaises(boolean treatMarginOfSuccessAsSuccessesAndRaises) {
        this.treatMarginOfSuccessAsSuccessesAndRaises = treatMarginOfSuccessAsSuccessesAndRaises;
    }

    public void setSavageWorldsTargetNumber(int targetNumber) {
        savageWorldsTargetNumber = targetNumber;
    }

    public int getSavageWorldsTargetNumber() {
        return savageWorldsTargetNumber;
    }

    public void setSavageWorldsRaiseStep(int raiseStep) {
        savageWorldsRaiseStep = raiseStep;
    }

    public int getSavageWorldsRaiseStep() {
        return savageWorldsRaiseStep;
    }

    public boolean isSavageWorldsMarginOfSuccessRequired() {
        return savageWorldsMarginOfSuccessRequired;
    }

    public boolean isTreatMarginOfSuccessAsSuccessesAndRaises() {
        return treatMarginOfSuccessAsSuccessesAndRaises;
    }
}
