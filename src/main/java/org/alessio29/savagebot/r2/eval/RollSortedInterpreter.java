package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RollSortedInterpreter {
    private final CommandContext context;

    private String currentHeading;
    private ArrayList<ErrorMessage> errors;
    private ArrayList<String> internalExplanations;
    private ArrayList<IntResult> results;

    public RollSortedInterpreter(CommandContext context) {
        this.context = context;
    }

    public String run(List<Statement> statements) {
        initializeInternalState();
        processStatements(statements);
        results.sort(VALUE_RESULT_COMPARATOR);
        return generateOutput();
    }

    private void initializeInternalState() {
        currentHeading = null;
        errors = new ArrayList<>();
        internalExplanations = new ArrayList<>();
        results = new ArrayList<>();
    }

    private void processStatements(List<Statement> statements) {
        StatementInterpreterInternal interpreter = new StatementInterpreterInternal();

        for (Statement statement : statements) {
            statement.accept(interpreter);

            if (!(statement instanceof NonParsedStringStatement)) {
                currentHeading = null;
            }
        }
    }

    @NotNull
    private String generateOutput() {
        StringBuilder output = new StringBuilder();

        for (ErrorMessage error : errors) {
            output.append("Error: `")
                    .append(error.getSourceText())
                    .append("`: ")
                    .append(error.getErrorMessage())
                    .append('\n');
        }

        for (String internalExplanation : internalExplanations) {
            output.append(internalExplanation).append('\n');
        }

        for (IntResult valueResult : results) {
            output.append(valueResult.getExplained()).append("\n");
        }

        if (currentHeading != null) {
            output.append(currentHeading);
        }

        return output.toString();
    }

    private static final Comparator<IntResult> VALUE_RESULT_COMPARATOR =
            Comparator.comparingInt(IntResult::getValue).reversed()
                    .thenComparing(IntResult::getExplained);

    private static class ErrorMessage {
        private final String sourceText;
        private final String errorMessage;

        ErrorMessage(Statement statement, String errorMessage) {
            this.sourceText = statement.getText();
            this.errorMessage = errorMessage;
        }

        ErrorMessage(Expression expression, String errorMessage) {
            this.sourceText = expression.getText();
            this.errorMessage = errorMessage;
        }

        String getSourceText() {
            return sourceText;
        }

        String getErrorMessage() {
            return errorMessage;
        }
    }

    private class StatementInterpreterInternal implements Statement.Visitor<Void> {

        @Override
        public Void visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement) {
            String lastHeading = currentHeading == null ? "" : currentHeading + " ";
            currentHeading = lastHeading + nonParsedStringStatement.getText();
            return null;
        }

        @Override
        public Void visitErrorStatement(ErrorStatement errorStatement) {
            errors.add(new ErrorMessage(errorStatement, errorStatement.getErrorMessage()));
            return null;
        }

        @Override
        public Void visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
            try {
                IntListResult result = ExpressionEvaluator.evalUnsafe(rollOnceStatement.getExpression(), context);
                List<Integer> values = result.getValues();
                if (values.size() != 1) {
                    errors.add(new ErrorMessage(rollOnceStatement, "Expected single result: " + values));
                } else {
                    results.add(new IntResult(values.get(0), currentHeading + ": " + result.getExplained()));
                }
            } catch (EvaluationErrorException e) {
                errors.add(new ErrorMessage(rollOnceStatement, e.getMessage()));
            }

            return null;
        }

        @Override
        public Void visitRollTimesStatement(RollTimesStatement rollTimesStatement) {
            rollTimesInternal(
                    rollTimesStatement.getTimes(),
                    Collections.singletonList(rollTimesStatement.getExpression())
            );
            return null;
        }

        private void rollTimesInternal(Expression timesExpression, List<Expression> rollExpressions) {
            int times;
            try {
                IntListResult timesResult = ExpressionEvaluator.evalUnsafe(timesExpression, context);
                List<Integer> values = timesResult.getValues();
                if (values.size() != 1) {
                    errors.add(new ErrorMessage(timesExpression, "Expected single result: " + values));
                    return;
                }

                times = values.get(0);

                if (ExpressionExplainer.isNonTrivialExpression(timesExpression)) {
                    internalExplanations.add(timesResult.getExplained());
                }
            } catch (EvaluationErrorException e) {
                errors.add(new ErrorMessage(timesExpression, e.getMessage()));
                return;
            }

            boolean hasSizeErrors = false;

            String headingPrefix = currentHeading == null ? "" : currentHeading + " ";
            for (int i = 0; i < times; ++i) {
                for (Expression rollExpression0 : rollExpressions) {
                    // Unwrap commented expressions
                    Expression rollExpression;
                    String expressionHeadingSuffix;
                    if (rollExpression0 instanceof CommentedExpression) {
                        CommentedExpression commentedExpression = (CommentedExpression) rollExpression0;
                        rollExpression = commentedExpression.getExpression();
                        expressionHeadingSuffix = " " + commentedExpression.getComment();
                    } else {
                        rollExpression = rollExpression0;
                        expressionHeadingSuffix = "";
                    }

                    try {
                        IntListResult rollResult = ExpressionEvaluator.evalUnsafe(rollExpression, context);
                        List<Integer> values = rollResult.getValues();
                        if (values.size() != 1) {
                            if (!hasSizeErrors) {
                                errors.add(new ErrorMessage(rollExpression, "Expected single result: " + values));
                                hasSizeErrors = true;
                            }
                            continue;
                        }

                        results.add(new IntResult(
                                values.get(0),
                                headingPrefix +
                                        (i + 1) + " of " + times +
                                        expressionHeadingSuffix + ": " +
                                        rollResult.getExplained()
                        ));
                    } catch (EvaluationErrorException e) {
                        errors.add(new ErrorMessage(rollExpression, i + ": " + e.getMessage()));
                        return;
                    }
                }
            }
        }

        @Override
        public Void visitRollBatchTimesStatement(RollBatchTimesStatement rollBatchTimesStatement) {
            rollTimesInternal(rollBatchTimesStatement.getTimes(), rollBatchTimesStatement.getExpressions());
            return null;
        }

        @Override
        public Void visitFlagStatement(FlagStatement flagStatement) {
            errors.add(new ErrorMessage(flagStatement, "Flags are not supported here"));
            return null;
        }
    }
}
