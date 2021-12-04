package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.r2.tree.*;

import java.util.Collections;
import java.util.List;

class StatementInterpreter implements Statement.Visitor<String> {
    private final RollInterpreter interpreter;

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
        checkTimes(times);

        result.append("\n");

        for (int i = 0; i < times; ++i) {
            result.append(i + 1).append(": ");
            IntListResult nthResult = eval(rollTimesStatement.getExpression());
            result.append(nthResult.getExplained());
            result.append("\n");
        }

        return result.toString();
    }

    private void checkTimes(int times) {
        if (times > Limits.MAX_TIMES) {
            throw new EvaluationErrorException("Too many repetitions: " + times + ", should be <= " + Limits.MAX_TIMES);
        }
    }

    @Override
    public String visitRollBatchTimesStatement(RollBatchTimesStatement rollBatchTimesStatement) {
        StringBuilder result = new StringBuilder();
        result.append(rollBatchTimesStatement.getText()).append(": ");
        Expression timesExpression = rollBatchTimesStatement.getTimes();
        List<Expression> expressions = rollBatchTimesStatement.getExpressions();

        int times = evalAndExplainTimes(result, timesExpression);
        checkTimes(times);

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
        } catch (Throwable e) {
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

    @Override
    public String visitIronSwornRollStatement(IronSwornRollStatement ironSwornRollStatement) {
        Expression modifierExpression = ironSwornRollStatement.getModivierExpression();
        OperatorExpression.Operator modifierOperator = ironSwornRollStatement.getModifierOperator();

        Roller roller = new Roller(interpreter.getContext().getRandom());
        int d6 = roller.roll(6);
        IntResult modifier = getModifierValue(modifierExpression, modifierOperator);
        int modifiedD6 = d6 + modifier.getValue();
        int d10a = roller.roll(10);
        int d10b = roller.roll(10);

        StringBuilder result = new StringBuilder();
        String moveResult;
        if (modifiedD6 > d10a && modifiedD6 > d10b) {
            moveResult = "Strong hit";
        } else if (modifiedD6 > d10a || modifiedD6 > d10b) {
            moveResult = "Weak hit";
        } else {
            moveResult = "Miss";
        }
        if (d10a == d10b) {
            moveResult += " with match";
        }
        result.append(ReplyBuilder.bold(moveResult)).append(": ").append(d6);
        if (modifierOperator != null) {
            result.append(' ').append(modifierOperator.getImage()).append(' ')
                    .append(modifier.getExplained())
                    .append(" = ").append(modifiedD6);
        }
        result.append(" VS ").append(d10a).append(", ").append(d10b);
        return result.toString();
    }

    private IntResult getModifierValue(Expression modifierExpression, OperatorExpression.Operator modifierOperator) {
        if (modifierExpression == null) {
            return new IntResult(0, null);
        }

        IntListResult modifierResult;
        try {
            modifierResult = ExpressionEvaluator.evalUnsafe(modifierExpression, interpreter.getContext());
        } catch (EvaluationErrorException e) {
            throw e;
        } catch (Throwable e) {
            throw new EvaluationErrorException("Error in `" + modifierExpression.getText() + "`", e);
        }
        List<Integer> modifierValues = modifierResult.getValues();
        if (modifierValues.size() != 1) {
            throw new EvaluationErrorException(
                    "Scalar result expected in `" + modifierExpression.getText() + "`: " +
                            modifierValues + " = " + modifierResult.getExplained()
            );
        }

        int modifierValue = modifierValues.get(0);
        String modifierExplained = modifierResult.getExplained();
        if (modifierOperator == OperatorExpression.Operator.PLUS) {
            return new IntResult(modifierValue, modifierExplained);
        } else if (modifierOperator == OperatorExpression.Operator.MINUS) {
            return new IntResult(-modifierValue, modifierExplained);
        } else {
            throw new EvaluationErrorException("Unexpected modifier operator: " + modifierOperator);
        }
    }
}
