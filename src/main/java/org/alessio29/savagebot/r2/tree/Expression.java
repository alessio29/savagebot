package org.alessio29.savagebot.r2.tree;

public abstract class Expression extends Node {
    public Expression(String text) {
        super(text);
    }

    public abstract <V> V accept(Visitor<V> visitor);

    public interface Visitor<V> {
        V visitIntExpression(IntExpression intExpression);

        V visitOperatorExpression(OperatorExpression operatorExpression);

        V visitGenericRollExpression(GenericRollExpression genericRollExpression);

        V visitFudgeRollExpression(FudgeRollExpression fudgeRollExpression);

        V visitSavageWorldsRollExpression(SavageWorldsRollExpression savageWorldsRollExpression);

        V visitCommentedExpression(CommentedExpression commentedExpression);

        V visitD66RollExpression(D66RollExpression d66RollExpression);

        V visitAssignVariableExpression(AssignVariableExpression assignVariableExpression);

        V visitVariableExpression(VariableExpression variableExpression);

        V visitCarcosaRollExpression(CarcosaRollExpression carcosaRollExpression);
    }
}
