package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.*;

import java.util.*;

public class RollAccumulatingInterpreter {
    private final CommandContext context;

    public RollAccumulatingInterpreter(CommandContext context) {
        this.context = context;
    }

    public String rollSorted(List<Statement> statements) {
        RollSortedState state = new RollSortedState();
        processStatements(statements, state);
        return state.generateOutput();
    }

    private void processStatements(List<Statement> statements, InterpreterState state) {
        StatementInterpreterInternal interpreter = new StatementInterpreterInternal(state);

        for (Statement statement : statements) {
            statement.accept(interpreter);

            if (!(statement instanceof NonParsedStringStatement)) {
                state.seeString(null);
            }
        }
    }

    private static final Comparator<IntResult> VALUE_RESULT_COMPARATOR =
            Comparator.comparingInt(IntResult::getValue).reversed()
                    .thenComparing(IntResult::getExplained);

    public String rollHistogram(List<Statement> statements) {
        RollHistogramState state = new RollHistogramState();
        processStatements(statements, state);
        return state.generateOutput();
    }

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

    private static abstract class InterpreterState {
        String heading;
        final ArrayList<ErrorMessage> errors = new ArrayList<>();
        final ArrayList<String> internalExplanations = new ArrayList<>();

        void addError(ErrorMessage errorMessage) {
            errors.add(errorMessage);
        }

        void seeString(String heading) {
            this.heading = heading;
        }

        String getHeading() {
            return heading;
        }

        void addInternalExplanation(String explanation) {
            internalExplanations.add(explanation);
        }

        public abstract void addResult(IntResult result);

        void prefixInfo(StringBuilder output) {
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
        }

        void postfixInfo(StringBuilder output) {
            if (heading != null) {
                output.append(heading);
            }
        }
    }

    private static class RollSortedState extends InterpreterState {
        final ArrayList<IntResult> results = new ArrayList<>();

        @Override
        public void addResult(IntResult result) {
            results.add(result);
        }

        String generateOutput() {
            results.sort(VALUE_RESULT_COMPARATOR);

            StringBuilder output = new StringBuilder();
            if (results.size() > 0) {
                output.append(":\n");
            }

            prefixInfo(output);

            for (IntResult valueResult : results) {
                output.append(valueResult.getExplained()).append('\n');
            }

            postfixInfo(output);

            return output.toString();
        }

    }

    private static class RollHistogramState extends InterpreterState {
        private static class Histogram {
            final String name;
            final TreeMap<Integer, Integer> results = new TreeMap<>();

            public Histogram(String name) {
                this.name = name;
            }

            public void addResult(IntResult result) {
                Integer oldValue = results.computeIfAbsent(result.getValue(), k -> 0);
                results.put(result.getValue(), oldValue + 1);
            }
        }

        private final List<Histogram> histograms = new ArrayList<>();
        private Histogram histogram = null;

        @Override
        public void addResult(IntResult result) {
            if (histogram == null) {
                histogram = new Histogram(getHeading());
                histograms.add(histogram);
            }
            histogram.addResult(result);
        }

        @Override
        void seeString(String heading) {
            super.seeString(heading);
            if (heading != null) {
                histogram = null;
            }
        }

        String generateOutput() {
            Set<Integer> values = new TreeSet<>();
            for (Histogram h : histograms) {
                values.addAll(h.results.keySet());
            }

            StringBuilder output = new StringBuilder();

            if (histograms.size() > 0) {
                output.append(":\n");
            }

            prefixInfo(output);

            for (Integer value : values) {
                StringBuilder line = new StringBuilder();
                line.append("**").append(value).append("**: ");
                for (Histogram h : histograms) {
                    if (h.name != null) {
                        line.append(h.name).append(": ");
                    } else if (histograms.size() > 1) {
                        line.append("<default>: ");
                    }
                    Integer count = h.results.get(value);
                    if (count == null) {
                        count = 0;
                    }
                    line.append(count).append("; ");
                }
                output.append(line.toString().trim()).append('\n');
            }
            
            postfixInfo(output);

            return output.toString();
        }
    }

    private class StatementInterpreterInternal implements Statement.Visitor<Void> {
        private final InterpreterState state;

        StatementInterpreterInternal(InterpreterState state) {
            this.state = state;
        }

        @Override
        public Void visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement) {
            String currentHeading = state.getHeading();
            String lastHeading = currentHeading == null ? "" : currentHeading + " ";
            state.seeString(lastHeading + nonParsedStringStatement.getText());
            return null;
        }

        @Override
        public Void visitErrorStatement(ErrorStatement errorStatement) {
            state.addError(new ErrorMessage(errorStatement, errorStatement.getErrorMessage()));
            return null;
        }

        @Override
        public Void visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
            try {
                IntListResult result = ExpressionEvaluator.evalUnsafe(rollOnceStatement.getExpression(), context);
                List<Integer> values = result.getValues();
                if (values.size() != 1) {
                    state.addError(new ErrorMessage(rollOnceStatement, "Expected single result: " + values));
                } else {
                    state.addResult(new IntResult(values.get(0), state.getHeading() + ": " + result.getExplained()));
                }
            } catch (EvaluationErrorException e) {
                state.addError(new ErrorMessage(rollOnceStatement, e.getMessage()));
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
                    state.addError(new ErrorMessage(timesExpression, "Expected single result: " + values));
                    return;
                }

                times = values.get(0);

                if (ExpressionExplainer.isNonTrivialExpression(timesExpression)) {
                    state.addInternalExplanation(timesResult.getExplained());
                }
            } catch (EvaluationErrorException e) {
                state.addError(new ErrorMessage(timesExpression, e.getMessage()));
                return;
            }

            boolean hasSizeErrors = false;

            String currentHeading = state.getHeading();
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
                                state.addError(new ErrorMessage(rollExpression, "Expected single result: " + values));
                                hasSizeErrors = true;
                            }
                            continue;
                        }

                        state.addResult(new IntResult(
                                values.get(0),
                                headingPrefix +
                                        (i + 1) + " of " + times +
                                        expressionHeadingSuffix + ": " +
                                        rollResult.getExplained()
                        ));
                    } catch (EvaluationErrorException e) {
                        state.addError(new ErrorMessage(rollExpression, i + ": " + e.getMessage()));
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
            state.addError(new ErrorMessage(flagStatement, "Flags are not supported here"));
            return null;
        }
    }
}
