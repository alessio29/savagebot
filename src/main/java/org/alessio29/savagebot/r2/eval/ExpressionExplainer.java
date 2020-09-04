package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.internal.builders.ReplyBuilder;
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

        if (expressionContext.isMarginOfSuccessRequired() || isSavageWorldsCheck(expression)) {
            return expression.getText() + ": " + explanation + " = " +
                    values.stream()
                            .map(i -> ReplyBuilder.bold(i.toString()) + explainMarginOfSuccess(i))
                            .collect(Collectors.joining(", "));
        }

        if (shouldExplanationAlreadyBeResult(expression)) {
            return expression.getText() + ": " + ReplyBuilder.bold(explanation);
        }

        String results = values.stream().map(i -> ReplyBuilder.bold(i.toString())).collect(Collectors.joining(", "));

        if (shouldExplanationContainHeader(expression)) {
            return explanation + " = " + results;
        }

        return expression.getText() + ": " + explanation + " = " + results;
    }

    private String explainMarginOfSuccess(int intValue) {
        int targetNumber = expressionContext.getTargetNumber();
        int raiseStep = expressionContext.getSavageWorldsRaiseStep();
        if (raiseStep <= 0) {
            throw new EvaluationErrorException("Raise step should be above 0: " + raiseStep);
        }

        int marginOfSuccess = intValue - targetNumber;

        TargetNumberMode mode = expressionContext.getTargetNumberMode();
        switch (mode) {
            case SAVAGE_WORLDS_SUCCESSES_AND_RAISES: {
                if (marginOfSuccess < 0) {
                    return "";
                }
                StringBuilder sb = new StringBuilder().append(" (success");
                int raiseCount = marginOfSuccess / raiseStep;
                if (raiseCount > 0) {
                    sb.append("; ").append(ReplyBuilder.bold(raiseCount));
                    if (raiseCount == 1) {
                        sb.append(" raise");
                    } else {
                        sb.append(" raises");
                    }
                }
                return sb.append(")").toString();
            }
            case SAVAGE_WORLDS_DAMAGE: {
                if (marginOfSuccess < 0) {
                    return "";
                }
                int numberOfWounds = marginOfSuccess / raiseStep;
                StringBuilder sb = new StringBuilder().append(" (shaken");
                if (numberOfWounds > 0) {
                    sb.append(", ").append(ReplyBuilder.bold(numberOfWounds));
                    if (numberOfWounds > 1) {
                        sb.append(" wounds");
                    } else {
                        sb.append(" wound");
                    }
                }
                return sb.append(")").toString();
            }
            case GENERIC_TN_ROLL_UNDER:
                marginOfSuccess = -marginOfSuccess;
                // fall-though
            case GENERIC_TN_ROLL_ABOVE:
                if (marginOfSuccess < 0) {
                    return " (failure, MoF: " + (-marginOfSuccess) + ")";
                }
                return " (success, MoS: " + marginOfSuccess + ")";
            default:
                throw new AssertionError("Unexpected TN mode: " + mode);
        }
    }

    private boolean isSavageWorldsCheck(Expression expression) {
        expression = dropBrackets(expression);
        if (expression instanceof SavageWorldsExtrasRollExpression) {
            return true;
        } else if (isSimpleSavageWorldsRoll(expression)) {
            return true;
        } else if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            OperatorExpression.Operator operator = operatorExpression.getOperator();
            if (operator != OperatorExpression.Operator.PLUS && operator != OperatorExpression.Operator.MINUS) {
                return false;
            }
            Expression arg1 = dropBrackets(operatorExpression.getArgument1());
            Expression arg2 = dropBrackets(operatorExpression.getArgument2());
            return isTrivialExpression(arg2) && isSavageWorldsCheck(arg1) ||
                    isTrivialExpression(arg1) && isSavageWorldsCheck(arg2);
        } else {
            return false;
        }
    }

    private static Expression dropBrackets(Expression expression) {
        if (expression instanceof OperatorExpression) {
            OperatorExpression operatorExpression = (OperatorExpression) expression;
            if (operatorExpression.getOperator() == OperatorExpression.Operator.BRACKETS) {
                return dropBrackets(operatorExpression.getArgument1());
            }
            // fall-through
        }
        return expression;
    }

    private boolean isSimpleSavageWorldsRoll(Expression expression) {
        if (!(expression instanceof SavageWorldsRollExpression)) return false;
        SavageWorldsRollExpression savageWorldsRoll = (SavageWorldsRollExpression) expression;
        Expression diceCount = savageWorldsRoll.getDiceCountArg();
        if (diceCount == null) return true;
        if (!(diceCount instanceof IntExpression)) return false;
        return ((IntExpression) diceCount).getValue() == 1;
    }

    public static boolean isTrivialExpression(Expression expression) {
        expression = dropBrackets(expression);
        return expression instanceof IntExpression || isD1(expression);
    }

    private static boolean isD1(Expression expression) {
        if (!(expression instanceof GenericRollExpression)) return false;
        Expression facetsCount = ((GenericRollExpression) expression).getFacetsCountArg();
        if (!(facetsCount instanceof IntExpression)) return false;
        return ((IntExpression) facetsCount).getValue() == 1;
    }

    public static boolean isNonTrivialExpression(Expression expression) {
        return !isTrivialExpression(expression);
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

    private boolean isRepresentedAsAdditiveExpression(Expression expression) {
        if (expression instanceof GenericRollExpression) {
            Expression diceCountArg = ((GenericRollExpression) expression).getDiceCountArg();
            if (diceCountArg instanceof IntExpression) {
                return ((IntExpression) diceCountArg).getValue() > 1;
            } else {
                return true;
            }
        } else return expression instanceof CarcosaRollExpression ||
                expression instanceof WegD6RollExpression;
    }

    private boolean isMultiplicativeOperator(OperatorExpression.Operator operator) {
        return operator == OperatorExpression.Operator.MUL ||
                operator == OperatorExpression.Operator.DIV ||
                operator == OperatorExpression.Operator.MOD;
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
                if (isMultiplicativeOperator(operator)) {
                    if (isRepresentedAsAdditiveExpression(argument1)) {
                        explanation1 = "(" + explanation1 + ")";
                    }
                    if (isRepresentedAsAdditiveExpression(argument2)) {
                        explanation2 = "(" + explanation2 + ")";
                    }
                }
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
    public String visitExtrasRollExpression(SavageWorldsExtrasRollExpression savageWorldsExtrasRollExpression) {
        return getKnownExplanation(savageWorldsExtrasRollExpression);
    }

    @Override
    public String visitD66RollExpression(D66RollExpression d66RollExpression) {
        return getKnownExplanation(d66RollExpression);
    }

    @Override
    public String visitCarcosaRollExpression(CarcosaRollExpression carcosaRollExpression) {
        return getKnownExplanation(carcosaRollExpression);
    }

    @Override
    public String visitWegD6Expression(WegD6RollExpression wegD6RollExpression) {
        return getKnownExplanation(wegD6RollExpression);
    }

    @Override
    public String visitTargetNumberAndRaiseStepExpression(TargetNumberAndRaiseStepExpression expression) {
        return getKnownExplanation(expression.getExpression());
    }
}
