package org.alessio29.savagebot.r2.eval;

import org.alessio29.savagebot.r2.tree.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExpressionEvaluator implements Expression.Visitor<List<Integer>> {
    private final ExpressionContext context;
    private final Roller roller;

    @NotNull
    public static IntListResult evalUnsafe(Expression expression, CommandContext context) {
        ExpressionContext expressionContext = new ExpressionContext(expression, context);
        List<Integer> values = new ExpressionEvaluator(expressionContext).eval(expression);
        String explanation = new ExpressionExplainer(expressionContext).explainExpressionResult(expression, values);
        return new IntListResult(values, explanation);
    }

    private ExpressionEvaluator(ExpressionContext context) {
        this.context = context;
        this.roller = new Roller(context.getCommandContext().getRandom());
    }

    @Override
    public List<Integer> visitIntExpression(IntExpression intExpression) {
        return Collections.singletonList(intExpression.getValue());
    }

    @Override
    public List<Integer> visitAssignVariableExpression(AssignVariableExpression assignVariableExpression) {
        List<Integer> value = assignVariableExpression.getArgument().accept(this);
        String variable = assignVariableExpression.getVariable();
        context.getCommandContext().putVariable(variable, value);
        return value;
    }

    @Override
    public List<Integer> visitVariableExpression(VariableExpression variableExpression) {
        String variable = variableExpression.getVariable();
        List<Integer> value = context.getCommandContext().getVariable(variable);
        if (value == null) {
            throw new EvaluationErrorException("Undefined variable: `" + variable + "`");
        }
        if (value.size() == 1) {
            context.putExplanation(variableExpression, "{" + variable + "=" + value.get(0) + "}");
        } else {
            context.putExplanation(variableExpression, "{" + variable + "=" + value + "}");
        }
        return value;
    }

    @Override
    public List<Integer> visitOperatorExpression(OperatorExpression operatorExpression) {
        OperatorExpression.Operator operator = operatorExpression.getOperator();

        if (operator == OperatorExpression.Operator.BOUND_TO) {
            return evalBoundToOperator(operatorExpression);
        }

        List<Integer> arg1 = eval(operatorExpression.getArgument1());
        List<Integer> arg2 = eval(operatorExpression.getArgument2());

        if (operator.getArity() == 1) {
            if (operator == OperatorExpression.Operator.BRACKETS) {
                String argumentExplanation = context.getExplanation(operatorExpression.getArgument1());
                if (argumentExplanation != null) {
                    context.putExplanation(operatorExpression, argumentExplanation);
                }
            }

            return arg1.stream()
                    .map(it -> applyUnaryOperator(operator, it))
                    .collect(Collectors.toList());
        }

        int arg10 = arg1.get(0);
        int arg20 = arg2.get(0);
        if (arg1.size() == 1 && arg2.size() == 1) {
            return Collections.singletonList(applyBinaryOperator(operator, arg10, arg20));
        } else if (arg1.size() == 1) {
            return arg2.stream()
                    .map(it -> applyBinaryOperator(operator, arg10, it))
                    .collect(Collectors.toList());
        } else if (arg2.size() == 1) {
            return arg1.stream()
                    .map(it -> applyBinaryOperator(operator, it, arg20))
                    .collect(Collectors.toList());
        } else {
            throw new EvaluationErrorException("Unexpected argument sizes: " + arg1 + ", " + arg2 +
                    " in `" + operatorExpression.getText() + "`");
        }
    }

    private List<Integer> evalBoundToOperator(OperatorExpression operatorExpression) {
        List<Integer> arg = eval(operatorExpression.getArgument1());
        int lowBound = evalInt(operatorExpression.getArgument2(), Integer.MIN_VALUE);
        int highBound = evalInt(operatorExpression.getArgument3(), Integer.MAX_VALUE);

        if (lowBound > highBound) {
            throw new EvaluationErrorException(
                    "Empty range in `" + operatorExpression.getText() + "`: " +
                            "[" + lowBound + ":" + highBound + "]"
            );
        }

        List<Integer> result = new ArrayList<>(arg.size());
        StringBuilder explain = new StringBuilder();
        if (arg.size() > 1) {
            explain.append("[");
        }

        StringJoiner listContent = new StringJoiner(",");
        for (Integer value : arg) {
            int bounded;
            if (value > highBound) {
                listContent.add(value + "=>" + highBound);
                bounded = highBound;
            } else if (value < lowBound) {
                listContent.add(value + "=>" + lowBound);
                bounded = lowBound;
            } else {
                listContent.add(Integer.toString(value));
                bounded = value;
            }
            result.add(bounded);
        }

        explain.append(listContent.toString());
        if (arg.size() > 1) {
            explain.append("]");
        }

        context.putExplanation(operatorExpression, explain.toString());

        return result;
    }

    private int applyUnaryOperator(OperatorExpression.Operator operator, int it) {
        if (operator.getArity() != 1) {
            throw new EvaluationErrorException("Unary operator expected: " + operator);
        }

        switch (operator) {
            case UNARY_PLUS:
            case BRACKETS:
                return it;
            case UNARY_MINUS:
                return -it;
            default:
                throw new EvaluationErrorException("Unexpected unary operator: " + operator);
        }
    }

    private int applyBinaryOperator(OperatorExpression.Operator operator, int arg1, int arg2) {
        if (operator.getArity() != 2) {
            throw new EvaluationErrorException("Binary operator expected: " + operator);
        }

        switch (operator) {
            case PLUS:
                return arg1 + arg2;
            case MINUS:
                return arg1 - arg2;
            case MUL:
                return arg1 * arg2;
            case DIV:
                if (arg2 == 0) {
                    throw new EvaluationErrorException("Division by 0");
                }
                return arg1 / arg2;
            case MOD:
                if (arg2 == 0) {
                    throw new EvaluationErrorException("Division by 0");
                }
                return arg1 % arg2;
            default:
                throw new EvaluationErrorException("Unexpected binary operator: " + operator);
        }
    }

    @Override
    public List<Integer> visitCommentedExpression(CommentedExpression commentedExpression) {
        return commentedExpression.getExpression().accept(this);
    }

    @Override
    public List<Integer> visitGenericRollExpression(GenericRollExpression genericRollExpression) {
        int diceCount = evalInt(genericRollExpression.getDiceCountArg(), 1);

        int facetsCount = evalInt(
                genericRollExpression.getFacetsCountArg(),
                () -> "No facets count: `" + genericRollExpression.getText() + "`"
        );

        IntResult result;

        if (genericRollExpression.isWithTargetNumberAndRaiseStep()) {
            context.setMarginOfSuccessRequired(true);
            handleTargetNumberAndRaiseStep(genericRollExpression);
        }

        if (genericRollExpression.getSuffixOperator() == GenericRollExpression.SuffixOperator.SUCCESS_OR_FAIL) {
            int successThreshold = evalInt(
                    genericRollExpression.getSuffixArg1(),
                    () -> "No success threshold: `" + genericRollExpression.getText() + "`"
            );

            int failThreshold = evalInt(genericRollExpression.getSuffixArg2(), 0);

            result = roller.rollSuccessOrFail(
                    diceCount,
                    facetsCount,
                    genericRollExpression.isOpenEnded(),
                    successThreshold,
                    failThreshold
            );
        } else {
            int suffixArg = evalRollAndKeepSuffixArgument(genericRollExpression);

            result = roller.rollAndKeep(
                    diceCount,
                    facetsCount,
                    genericRollExpression.isOpenEnded(),
                    genericRollExpression.getSuffixOperator(),
                    suffixArg
            );
        }

        context.putExplanation(genericRollExpression, result.getExplained());

        return Collections.singletonList(result.getValue());
    }

    private int evalRollAndKeepSuffixArgument(GenericRollExpression expression) {
        GenericRollExpression.SuffixOperator suffixOperator = expression.getSuffixOperator();
        Expression suffixArgument = expression.getSuffixArg1();

        if (suffixOperator == null) {
            return 0;
        }

        switch (suffixOperator) {
            case KEEP:
                return evalInt(suffixArgument, () -> "No argument for 'k': `" + expression.getText() + "`");
            case KEEP_LEAST:
                return evalInt(suffixArgument, () -> "No argument for 'kl': `" + expression.getText() + "`");
            case ADVANTAGE:
            case DISADVANTAGE:
                return evalInt(suffixArgument, 1);
            default:
                throw new EvaluationErrorException("Unexpected suffix operator: " + suffixOperator);
        }
    }

    @Override
    public List<Integer> visitFudgeRollExpression(FudgeRollExpression fudgeRollExpression) {
        int diceCount = evalInt(fudgeRollExpression.getDiceCountArg(), 4);

        IntResult dieResult = roller.rollFudge(diceCount);

        context.putExplanation(fudgeRollExpression, dieResult.getExplained());

        return Collections.singletonList(dieResult.getValue());
    }

    @Override
    public List<Integer> visitCarcosaRollExpression(CarcosaRollExpression carcosaRollExpression) {
        int diceCount = evalInt(carcosaRollExpression.getDiceCountArg(), 1);
        IntResult diceResult = roller.rollCarcosa(diceCount);
        context.putExplanation(carcosaRollExpression, diceResult.getExplained());
        return Collections.singletonList(diceResult.getValue());
    }

    @Override
    public List<Integer> visitSavageWorldsRollExpression(SavageWorldsRollExpression savageWorldsRollExpression) {
        context.setMarginOfSuccessRequired(true);
        context.setTargetNumberMode(TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES);

        int diceCount = evalInt(savageWorldsRollExpression.getDiceCountArg(), 1);

        int abilityDieFacets = evalInt(
                savageWorldsRollExpression.getAbilityDieArg(),
                () -> "No ability die facets: `" + savageWorldsRollExpression.getText() + "`"
        );

        int wildDieFacets = evalInt(savageWorldsRollExpression.getWildDieArg(), 6);

        IntListResult result = roller.rollSavageWorlds(diceCount, abilityDieFacets, wildDieFacets);

        context.putExplanation(savageWorldsRollExpression, result.getExplained());

        handleTargetNumberAndRaiseStep(savageWorldsRollExpression);
        return result.getValues();
    }

    @Override
    public List<Integer> visitExtrasRollExpression(SavageWorldsExtrasRollExpression savageWorldsExtrasRollExpression) {
        context.setMarginOfSuccessRequired(true);
        context.setTargetNumberMode(TargetNumberMode.SAVAGE_WORLDS_SUCCESSES_AND_RAISES);

        int facetsCount = evalInt(savageWorldsExtrasRollExpression.getFacetsArg(), 6);

        int dieValue = roller.roll(facetsCount, true).getValue();

        int modifier = evalInt(savageWorldsExtrasRollExpression.getModifierArg(), 0);

        int result;
        StringBuilder explanation = new StringBuilder();
        explanation.append(dieValue);
        OperatorExpression.Operator operator = savageWorldsExtrasRollExpression.getModifierOperator();
        if (operator == OperatorExpression.Operator.PLUS) {
            result = dieValue + modifier;
            explanation.append(" + ").append(modifier);
        } else if (operator == OperatorExpression.Operator.MINUS) {
            result = dieValue - modifier;
            explanation.append(" - ").append(modifier);
        } else {
            result = dieValue;
        }

        context.putExplanation(savageWorldsExtrasRollExpression, explanation.toString());
        handleTargetNumberAndRaiseStep(savageWorldsExtrasRollExpression);
        return Collections.singletonList(result);
    }

    @Override
    public List<Integer> visitTargetNumberAndRaiseStepExpression(TargetNumberAndRaiseStepExpression expression) {
        context.setMarginOfSuccessRequired(true);
        handleTargetNumberAndRaiseStep(expression);
        return eval(expression.getExpression());
    }

    private void handleTargetNumberAndRaiseStep(WithTargetNumberAndRaiseStep expression) {
        Expression targetNumberAndRaiseStepArg = expression.getTargetNumberAndRaiseStepArg();
        context.setTargetNumberMode(expression.getTargetNumberMode());
        if (expression.getTargetNumberMode() == TargetNumberMode.GENERIC_TN_ROLL_ABOVE) {
            context.setTargetNumber(evalInt(expression.getTargetNumberArg(), () -> "No target number"));
        } else if (targetNumberAndRaiseStepArg != null) {
            int targetNumberAndRaiseStep = evalInt(targetNumberAndRaiseStepArg, 4);
            context.setTargetNumber(targetNumberAndRaiseStep);
            context.setSavageWorldsRaiseStep(targetNumberAndRaiseStep);
        } else {
            if (expression.getTargetNumberArg() != null) {
                int targetNumber = evalInt(expression.getTargetNumberArg(), 4);
                context.setTargetNumber(targetNumber);
            }
            if (expression.getRaiseStepArg() != null) {
                int raiseStep = evalInt(expression.getRaiseStepArg(), 4);
                context.setSavageWorldsRaiseStep(raiseStep);
            }
        }
    }

    @Override
    public List<Integer> visitD66RollExpression(D66RollExpression d66RollExpression) {
        IntResult result = roller.rollD66(d66RollExpression.getDigitsCount());

        context.putExplanation(d66RollExpression, result.getExplained());

        return Collections.singletonList(result.getValue());
    }

    @Override
    public List<Integer> visitWegD6Expression(WegD6RollExpression wegD6RollExpression) {
        int diceCount = evalInt(wegD6RollExpression.getDiceCountArg(), 1);
        IntResult result = roller.rollWegD6(diceCount);
        context.putExplanation(wegD6RollExpression, result.getExplained());
        return Collections.singletonList(result.getValue());
    }

    public List<Integer> eval(Expression expression) {
        return expression == null ? null : expression.accept(this);
    }

    private int evalInt(Expression expression, Supplier<String> ifAbsentErrorMessage) {
        if (expression == null) {
            throw new EvaluationErrorException(ifAbsentErrorMessage.get());
        }
        List<Integer> result = expression.accept(this);
        if (result.size() != 1) {
            throw new EvaluationErrorException("Single value expected in `" + expression.getText() + "`: " + result);
        }
        return result.get(0);
    }

    private int evalInt(Expression expression, int defaultValue) {
        if (expression == null) return defaultValue;
        List<Integer> result = expression.accept(this);
        if (result.size() != 1) {
            throw new EvaluationErrorException("Single value expected in `" + expression.getText() + "`: " + result);
        }
        return result.get(0);
    }
}
