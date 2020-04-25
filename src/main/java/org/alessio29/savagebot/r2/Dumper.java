package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.tree.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class Dumper implements Statement.Visitor<Void>, Expression.Visitor<Void> {
    private final PrintWriter out;
    private int indent = 0;
    private boolean atLineStart = true;

    public static String dump(Expression expression) {
        StringWriter sw = new StringWriter();
        expression.accept(new Dumper(sw));
        return sw.toString();
    }

    public static String dump(Statement statement) {
        StringWriter sw = new StringWriter();
        statement.accept(new Dumper(sw));
        return sw.toString();
    }

    public Dumper(Writer writer) {
        this.out = new PrintWriter(writer);
    }

    private void indented(Runnable r) {
        indent += 2;
        r.run();
        indent -= 2;
    }

    private void print(String string) {
        indentIfRequired();
        out.print(string);
    }

    private static String trimRight(String string) {
        int i = string.length() - 1;
        for (; i >= 0; --i) {
            if (!Character.isWhitespace(string.charAt(i))) break;
        }
        return string.substring(0, i + 1);
    }

    private void println(String string) {
        indentIfRequired();
        out.println(trimRight(string));
        atLineStart = true;
    }

    private void println() {
        out.println();
        atLineStart = true;
    }

    private void indentIfRequired() {
        if (atLineStart) {
            atLineStart = false;
            for (int i = 0; i < indent; ++i) {
                out.print(' ');
            }
        }
    }

    private void dump(String label, Expression expression) {
        print(label + ": ");
        if (expression != null) {
            expression.accept(this);
        } else {
            println("null");
        }
    }

    @Override
    public Void visitIntExpression(IntExpression intExpression) {
        println("Int " + intExpression.getValue());
        return null;
    }

    @Override
    public Void visitAssignVariableExpression(AssignVariableExpression assignVariableExpression) {
        println("AssignVar " + assignVariableExpression.getVariable());
        indented(() -> dump("arg", assignVariableExpression.getArgument()));
        return null;
    }

    @Override
    public Void visitVariableExpression(VariableExpression variableExpression) {
        println("Var " + variableExpression.getVariable());
        return null;
    }

    @Override
    public Void visitOperatorExpression(OperatorExpression operatorExpression) {
        OperatorExpression.Operator operator = operatorExpression.getOperator();
        println("Operator " + operator);
        indented(() -> {
            if (operator.getArity() >= 1) {
                dump("arg1", operatorExpression.getArgument1());
            }
            if (operator.getArity() >= 2) {
                dump("arg2", operatorExpression.getArgument2());
            }
            if (operator.getArity() >= 3) {
                dump("arg3", operatorExpression.getArgument3());
            }
        });
        return null;
    }

    @Override
    public Void visitCommentedExpression(CommentedExpression commentedExpression) {
        println("Commented \"" + commentedExpression.getComment() + "\"");
        indented(() -> dump("expr", commentedExpression.getExpression()));
        return null;
    }

    @Override
    public Void visitGenericRollExpression(GenericRollExpression genericRollExpression) {
        GenericRollExpression.SuffixOperator suffixOperator = genericRollExpression.getSuffixOperator();
        print("GenericRoll isOpenEnded=" + genericRollExpression.isOpenEnded());
        if (suffixOperator != null) {
            print(" suffixOperator=" + suffixOperator);
        }
        println();
        indented(() -> {
            dump("diceCount", genericRollExpression.getDiceCountArg());
            dump("facetsCount", genericRollExpression.getFacetsCountArg());
            dump("suffixArg1", genericRollExpression.getSuffixArg1());
            dump("suffixArg2", genericRollExpression.getSuffixArg2());
        });
        return null;
    }

    @Override
    public Void visitFudgeRollExpression(FudgeRollExpression fudgeRollExpression) {
        println("FudgeRoll");
        indented(() -> dump("diceCount", fudgeRollExpression.getDiceCountArg()));
        return null;
    }

    @Override
    public Void visitCarcosaRollExpression(CarcosaRollExpression carcosaRollExpression) {
        println("CarcosaRoll");
        indented(() -> dump("diceCount", carcosaRollExpression.getDiceCountArg()));
        return null;
    }

    @Override
    public Void visitWegD6Expression(WegD6RollExpression wegD6RollExpression) {
        println("WegD6Roll");
        indented(() -> dump("diceCount", wegD6RollExpression.getDiceCountArg()));
        return null;
    }

    @Override
    public Void visitExtrasRollExpression(SavageWorldsExtrasRollExpression savageWorldsExtrasRollExpression) {
        println("ExtrasRoll");
        indented(() -> {
            dump("facetsCount", savageWorldsExtrasRollExpression.getFacetsArg());

            OperatorExpression.Operator modifierOperator = savageWorldsExtrasRollExpression.getModifierOperator();
            if (modifierOperator != null) {
                println("modifierOperator: " + modifierOperator.getImage());
            } else {
                println("modifierOperator: null");
            }

            dump("modifierArg", savageWorldsExtrasRollExpression.getModifierArg());
        });
        return null;
    }

    @Override
    public Void visitSavageWorldsRollExpression(SavageWorldsRollExpression savageWorldsRollExpression) {
        println("SavageWorldsRoll");
        indented(() -> {
            dump("diceCount", savageWorldsRollExpression.getDiceCountArg());
            dump("abilityDie", savageWorldsRollExpression.getAbilityDieArg());
            dump("wildDie", savageWorldsRollExpression.getWildDieArg());
        });
        return null;
    }

    @Override
    public Void visitD66RollExpression(D66RollExpression d66RollExpression) {
        println("RollD66 digits=" + d66RollExpression.getDigitsCount());
        return null;
    }

    @Override
    public Void visitTargetNumberAndRaiseStepExpression(TargetNumberAndRaiseStepExpression expression) {
        println("TargetNumberAndStep");
        indented(() -> {
            dump("targetNumber", expression.getTargetNumberArg());
            dump("raiseStep", expression.getRaiseStepArg());
            dump("targetNumberAndRaiseStep", expression.getTargetNumberAndRaiseStepArg());
            dump("argument", expression.getExpression());
        });
        return null;
    }

    @Override
    public Void visitNonParsedStringStatement(NonParsedStringStatement nonParsedStringStatement) {
        println("NonParsedString " +
                "text='" + nonParsedStringStatement.getText() + "' " +
                "parserErrorMessage='" + nonParsedStringStatement.getParserErrorMessage() + "'"
        );
        return null;
    }

    @Override
    public Void visitErrorStatement(ErrorStatement errorStatement) {
        println("Error " +
                "text='" + errorStatement.getText() + "' " +
                "errorMessage='" + errorStatement.getErrorMessage() + "'"
        );
        return null;
    }

    @Override
    public Void visitRollOnceStatement(RollOnceStatement rollOnceStatement) {
        println("RollOnce");
        indented(() -> dump("expr", rollOnceStatement.getExpression()));
        return null;
    }

    @Override
    public Void visitRollTimesStatement(RollTimesStatement rollTimesStatement) {
        println("RollTimes");
        indented(() -> {
            dump("times", rollTimesStatement.getTimes());
            dump("expr", rollTimesStatement.getExpression());
        });
        return null;
    }

    @Override
    public Void visitRollBatchTimesStatement(RollBatchTimesStatement rollBatchTimesStatement) {
        println("RollBatch");
        indented(() -> {
            dump("times", rollBatchTimesStatement.getTimes());
            for (Expression expression : rollBatchTimesStatement.getExpressions()) {
                dump("expr", expression);
            }
        });
        return null;
    }

    @Override
    public Void visitFlagStatement(FlagStatement flagStatement) {
        println("Flag '" + flagStatement.getFlag() + "'");
        return null;
    }
}
