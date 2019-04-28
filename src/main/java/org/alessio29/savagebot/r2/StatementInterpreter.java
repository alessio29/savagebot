package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.r2.tree.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class StatementInterpreter implements Statement.Visitor<String> {
    private Interpreter interpreter;

    public StatementInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public String visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement) {
        return nonParsedStringStatement.getText() + " ";
    }

    @Override
    public String visitErrorStatement(ErrorStatement errorStatement) {
        return "(Error: '" + errorStatement.getText() + "': " + errorStatement.getErrorMessage() + ")\n";
    }

    @Override
    public String visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
        StringBuilder result = new StringBuilder();

        IntListResult evalResult = eval(rollOnceStatement.getExpression());
        result.append(evalResult.getExplained());

        result.append("\n");

        return result.toString();
    }

    private ExpressionContext getTopLevelExpressionContext(Expression topExpression) {
        return new ExpressionContext(topExpression, interpreter.getContext());
    }

    @Override
    public String visitRollTimesStatement(RollTimesStatement rollTimesStatement) {
        StringBuilder result = new StringBuilder();
        result.append(rollTimesStatement.getText()).append(": ");

        Expression timesExpression = rollTimesStatement.getTimes();
        int times = evalAndExplainTimes(result, timesExpression);

        result.append("\n");

        for (int i = 0; i < times; ++i) {
            result.append(i+1).append(": ");
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
            result.append(i+1).append(": ");
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
        if (!(timesExpression instanceof IntExpression)) {
            result.append(timesResult.getExplained());
        }
        return timesResult.getValues().get(0);
    }

    private IntListResult eval(Expression expression) {
        StringBuilder result = new StringBuilder();
        List<Integer> values;

        try {
            ExpressionContext expressionContext = getTopLevelExpressionContext(expression);
            ExpressionEvaluator evaluator = new ExpressionEvaluator(expressionContext);

            values = expression.accept(evaluator);

            ExpressionExplainer explainer = new ExpressionExplainer(expressionContext);

            result.append(expression.accept(explainer)).append(" = ");

            result.append(
                    values.stream()
                            .map(integer -> Messages.bold(integer.toString()))
                            .collect(Collectors.joining(", "))
            );
        } catch (EvaluationErrorException e) {
            values = Collections.singletonList(0);
            result.append(expression.getText()).append(": ").append(e.getMessage());
        }

        return new IntListResult(values, result.toString());
    }
}
