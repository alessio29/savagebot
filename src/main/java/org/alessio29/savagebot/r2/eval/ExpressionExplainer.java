package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.Messages;
import org.alessio29.savagebot.r2.tree.*;

import java.util.List;
import java.util.stream.Collectors;

class ExpressionExplainer implements Expression.Visitor<String> {
    private final ExpressionContext expressionContext;

    ExpressionExplainer(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }

    String explainExpressionResult(Expression expression, List<Integer> values) {
        String explanation = expression.accept(this);

        if (isTrivialExpression(expression)) {
            return explanation;
        }

        if (isSavageWorldsCheck(expression)) {
            return expression.getText() + ": " + explanation + " = " +
                    values.stream()
                            .map(i -> Messages.bold(i.toString()) + getSuccessesIfAny(i))
                            .collect(Collectors.joining(", "));
        }

        if (shouldExplanationAlreadyBeResult(expression)) {
            return expression.getText() + ": " + Messages.bold(explanation);
        }

        String results = values.stream().map(i -> Messages.bold(i.toString())).collect(Collectors.joining(", "));

        if (shouldExplanationContainHeader(expression)) {
            return explanation + " = " + results;
        }

        return expression.getText() + ": " + explanation + " = " + results;
    }

    private String getSuccessesIfAny(int intValue) {
        if (intValue < 4) {
            return "";
        } else {
            return " (" + (intValue / 4) + ")";
        }
    }

    private boolean isSavageWorldsCheck(Expression expression) {
        if (isSimpleSavageWorldsRoll(expression)) {
            return true;
        } else if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            OperatorExpression.Operator operator = operatorExpression.getOperator();
            if (operator != OperatorExpression.Operator.PLUS && operator != OperatorExpression.Operator.MINUS) {
                return false;
            }
            Expression arg1 = operatorExpression.getArgument1();
            Expression arg2 = operatorExpression.getArgument2();
            return isTrivialExpression(arg2) && isSavageWorldsCheck(arg1) ||
                    isTrivialExpression(arg1) && isSavageWorldsCheck(arg2);
        } else {
            return false;
        }
    }

    private boolean isSimpleSavageWorldsRoll(Expression expression) {
        if (!(expression instanceof SavageWorldsRollExpression)) return false;
        SavageWorldsRollExpression savageWorldsRoll = (SavageWorldsRollExpression) expression;
        Expression diceCount = savageWorldsRoll.getDiceCountArg();
        if (diceCount == null) return true;
        if (!(diceCount instanceof IntExpression)) return false;
        return ((IntExpression) diceCount).getValue() == 1;
    }

    private boolean isTrivialExpression(Expression expression) {
        return expression instanceof IntExpression;
    }

    private static boolean shouldExplanationAlreadyBeResult(Expression expression) {
        if (expression instanceof IntExpression || expression instanceof D66RollExpression) {
            return true;
        }

        if (expression instanceof GenericRollExpression) {
            GenericRollExpression rollExpression = (GenericRollExpression) expression;
            if (rollExpression.getSuffixOperator() != null) return false;
            if (rollExpression.isOpenEnded()) return false;

            Expression diceCount = rollExpression.getDiceCountArg();
            if (diceCount == null) return true;
            return diceCount instanceof IntExpression && ((IntExpression) diceCount).getValue() == 1;
        }

        return false;
    }

    private static boolean shouldExplanationContainHeader(Expression expression) {
        return expression instanceof CommentedExpression;
    }

    @Override
    public String visitIntExpression(IntExpression intExpression) {
        return intExpression.getText();
    }

    @Override
    public String visitAssignVariableExpression(AssignVariableExpression assignVariableExpression) {
        String argumentExplanation = expressionContext.getExplanation(assignVariableExpression.getArgument());
        if (argumentExplanation != null) {
            return "{" + assignVariableExpression.getVariable() + "=" + argumentExplanation + "}";
        }
        return getKnownExplanation(assignVariableExpression);
    }

    @Override
    public String visitVariableExpression(VariableExpression variableExpression) {
        return expressionContext.getExplanation(variableExpression);
    }

    @Override
    public String visitOperatorExpression(OperatorExpression operatorExpression) {
        OperatorExpression.Operator operator = operatorExpression.getOperator();
        OperatorExpression.OperatorKind kind = operator.getKind();
        Expression argument1 = operatorExpression.getArgument1();
        Expression argument2 = operatorExpression.getArgument2();
        Expression argument3 = operatorExpression.getArgument3();

        String explanation1 = explainOrNull(argument1);
        String explanation2 = explainOrNull(argument2);
        explainOrNull(argument3);

        if (operator == OperatorExpression.Operator.BOUND_TO) {
            return explainBoundTo(operatorExpression);
        }

        String operatorExplanation;
        switch (kind) {
            case BINARY:
                operatorExplanation = explanation1 + " " + operator.getImage() + " " + explanation2;
                break;
            case PREFIX:
                operatorExplanation = operator.getImage() + explanation1;
                break;
            case BRACKETS:
                operatorExplanation = operator.getImage1() + explanation1 + operator.getImage2();
                break;
            default:
                throw new EvaluationErrorException("Unexpected operator kind: " + kind);
        }

        if (expressionContext.getExplanation(argument1) != null ||
                expressionContext.getExplanation(argument2) != null ||
                expressionContext.getExplanation(argument3) != null
        ) {
            expressionContext.putExplanation(operatorExpression, operatorExplanation);
        }

        return operatorExplanation;
    }

    private String explainOrNull(Expression argument1) {
        return argument1 == null ? null : argument1.accept(this);
    }

    private String explainBoundTo(OperatorExpression operatorExpression) {
        Expression argument1 = operatorExpression.getArgument1();
        Expression argument2 = operatorExpression.getArgument2();
        Expression argument3 = operatorExpression.getArgument3();
        String explanation1 = expressionContext.getExplanation(argument1);
        String explanation2 = expressionContext.getExplanation(argument2);
        String explanation3 = expressionContext.getExplanation(argument3);

        if (explanation1 != null || explanation2 != null || explanation3 != null) {
            StringBuilder explanation = new StringBuilder();
            explanation.append("{");
            appendArgumentExplanation(explanation, null, explanation1);
            appendArgumentExplanation(explanation, argument2, explanation2);
            appendArgumentExplanation(explanation, argument3, explanation3);
            explanation.append("= ").append(expressionContext.getExplanation(operatorExpression)).append("}");
            return explanation.toString();
        }

        return getKnownExplanation(operatorExpression);
    }

    private void appendArgumentExplanation(StringBuilder result, Expression argument, String explanation) {
        if (explanation != null) {
            if (argument != null) {
                result.append(argument.getText()).append(": ");
            }
            result.append(explanation).append(" ");
        }
    }

    @Override
    public String visitCommentedExpression(CommentedExpression commentedExpression) {
        return commentedExpression.getComment() + ": " + commentedExpression.getExpression().accept(this);
    }

    private String getKnownExplanation(Expression expression) {
        if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            if (operatorExpression.getOperator() == OperatorExpression.Operator.BRACKETS) {
                return getKnownExplanation(operatorExpression.getArgument1());
            }
        }

        String explanation = expressionContext.getExplanation(expression);
        if (explanation != null) {
            return explanation;
        }
        return expression.getText();
    }

    @Override
    public String visitGenericRollExpression(GenericRollExpression genericRollExpression) {
        return getKnownExplanation(genericRollExpression);
    }

    @Override
    public String visitFudgeRollExpression(FudgeRollExpression fudgeRollExpression) {
        return getKnownExplanation(fudgeRollExpression);
    }

    @Override
    public String visitSavageWorldsRollExpression(SavageWorldsRollExpression savageWorldsRollExpression) {
        return getKnownExplanation(savageWorldsRollExpression);
    }

    @Override
    public String visitD66RollExpression(D66RollExpression d66RollExpression) {
        return getKnownExplanation(d66RollExpression);
    }
}
