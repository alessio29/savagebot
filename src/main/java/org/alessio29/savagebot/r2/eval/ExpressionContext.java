package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.Expression;

import java.util.HashMap;
import java.util.Map;

class ExpressionContext {
    private final Expression topExpression;
    private final CommandContext commandContext;
    private final Map<Expression, String> explanations = new HashMap<>();

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
}
