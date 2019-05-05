package org.alessio29.savagebot.r2.tree;

public class AssignVariableExpression extends Expression {
    private final String variable;
    private final Expression argument;

    public AssignVariableExpression(String text, String variable, Expression argument) {
        super(text);
        this.variable = variable;
        this.argument = argument;
    }

    public String getVariable() {
        return variable;
    }

    public Expression getArgument() {
        return argument;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitAssignVariableExpression(this);
    }
}
