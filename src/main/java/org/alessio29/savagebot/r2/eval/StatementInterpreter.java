package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.r2.tree.*;

import java.util.Collections;
import java.util.List;

class StatementInterpreter implements Statement.Visitor<String> {
    private RollInterpreter interpreter;

    StatementInterpreter(RollInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public String visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement) {
        return nonParsedStringStatement.getText() + " ";
    }

    @Override
    public String visitErrorStatement(ErrorStatement errorStatement) {
        return "Error: `" + errorStatement.getText() + "`: " + errorStatement.getErrorMessage() + "\n";
    }

    @Override
    public String visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
        StringBuilder result = new StringBuilder();

        IntListResult evalResult = eval(rollOnceStatement.getExpression());
        result.append(evalResult.getExplained());

        result.append("\n");

        return result.toString();
    }

    @Override
    public String visitRollTimesStatement(RollTimesStatement rollTimesStatement) {
        StringBuilder result = new StringBuilder();
        result.append(rollTimesStatement.getText()).append(": ");

        Expression timesExpression = rollTimesStatement.getTimes();
        int times = evalAndExplainTimes(result, timesExpression);

        result.append("\n");

        for (int i = 0; i < times; ++i) {
            result.append(i + 1).append(": ");
            IntListResult nthResult = eval(rollTimesStatement.getExpression());
            result.append(nthResult.getExplained());
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public String visitRollBatchTimesStatement(RollBatchTimesStatement rollBatchTimesStatement) {
        StringBuilder result = new StringBuilder();
        result.append(rollBatchTimesStatement.getText()).append(": ");
        Expression timesExpression = rollBatchTimesStatement.getTimes();
        List<Expression> expressions = rollBatchTimesStatement.getExpressions();
        int times = evalAndExplainTimes(result, timesExpression);
        result.append("\n");
        for (int i = 0; i < times; ++i) {
            result.append(i + 1).append(": ");
            for (Expression expression : expressions) {
                result.append(eval(expression).getExplained());
                result.append("; ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    private int evalAndExplainTimes(StringBuilder result, Expression timesExpression) {
        IntListResult timesResult = eval(timesExpression);
        if (ExpressionExplainer.isNonTrivialExpression(timesExpression)) {
            result.append(timesResult.getExplained());
        }
        return timesResult.getValues().get(0);
    }

    private IntListResult eval(Expression expression) {
        try {
            return ExpressionEvaluator.evalUnsafe(expression, interpreter.getContext());
        } catch (EvaluationErrorException e) {
            return new IntListResult(
                    Collections.emptyList(),
                    expression.getText() + ": " + e.getMessage()
            );
        }
    }

    @Override
    public String visitFlagStatement(FlagStatement flagStatement) {
        String flagToLower = flagStatement.getFlag().toLowerCase();
        if ("debug".equals(flagToLower)) {
            interpreter.setDebugEnabled();
            return ReplyBuilder.italic("Debug mode enabled.") + "\n";
        }
        throw new EvaluationErrorException("Unknown flag: '" + flagStatement.getFlag() + "'");
    }
}
