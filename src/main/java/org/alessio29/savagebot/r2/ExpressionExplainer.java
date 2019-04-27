package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.tree.*;

class ExpressionExplainer implements Expression.Visitor<String> {
    private final ExpressionContext expressionContext;

    public ExpressionExplainer(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }

    @Override
    public String visitIntExpression(IntExpression intExpression) {
        return intExpression.getText();
    }

    @Override
    public String visitOperatorExpression(OperatorExpression operatorExpression) {
        OperatorExpression.Operator operator = operatorExpression.getOperator();
        OperatorExpression.OperatorKind kind = operator.getKind();

        Expression argument1 = operatorExpression.getArgument1();
        Expression argument2 = operatorExpression.getArgument2();

        switch (kind) {
            case BINARY:
                return argument1.accept(this) + " " + operator.getImage() + " " + argument2.accept(this);
            case PREFIX:
                return operator.getImage() + argument1.accept(this);
            case BRACKETS:
                return operator.getImage1() + argument1.accept(this) + operator.getImage2();
            default:
                throw new EvaluationErrorException("Unexpected operator kind: " + kind);
        }
    }

    private String getExplanation(Expression expression) {
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
}
