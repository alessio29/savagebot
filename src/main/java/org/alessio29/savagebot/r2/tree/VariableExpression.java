package org.alessio29.savagebot.r2.tree;

public class VariableExpression extends Expression {
    private final String variable;

    public VariableExpression(String text, String variable) {
        super(text);
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitVariableExpression(this);
    }
}
