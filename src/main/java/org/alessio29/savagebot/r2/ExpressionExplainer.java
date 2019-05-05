package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.tree.*;

class ExpressionExplainer implements Expression.Visitor<String> {
    private final ExpressionContext expressionContext;

    ExpressionExplainer(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
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
        return getExplanation(assignVariableExpression);
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
            explanation.append("{").append(operatorExpression.getText()).append(": ");
            appendArgumentExplanation(explanation, null, explanation1);
            appendArgumentExplanation(explanation, argument2, explanation2);
            appendArgumentExplanation(explanation, argument3, explanation3);
            explanation.append(expressionContext.getExplanation(operatorExpression)).append("}");
            return explanation.toString();
        }

        return getExplanation(operatorExpression);
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

    private String getExplanation(Expression expression) {
        if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            if (operatorExpression.getOperator() == OperatorExpression.Operator.BRACKETS) {
                return getExplanation(operatorExpression.getArgument1());
            }
        }

        String explanation = expressionContext.getExplanation(expression);
        if (explanation != null) {
            return "{" + expression.getText() + ": " + explanation + "}";
        }
        return expression.getText();
    }

    @Override
    public String visitGenericRollExpression(GenericRollExpression genericRollExpression) {
        return getExplanation(genericRollExpression);
    }

    @Override
    public String visitFudgeRollExpression(FudgeRollExpression fudgeRollExpression) {
        return getExplanation(fudgeRollExpression);
    }

    @Override
    public String visitSavageWorldsRollExpression(SavageWorldsRollExpression savageWorldsRollExpression) {
        return getExplanation(savageWorldsRollExpression);
    }

    @Override
    public String visitD66RollExpression(D66RollExpression d66RollExpression) {
        return getExplanation(d66RollExpression);
    }
}
