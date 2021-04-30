package org.alessio29.savagebot.r2.tree;

public class IronSwornRollStatement extends Statement {
    private final OperatorExpression.Operator modifierOperator;
    private final Expression modivierExpression;

    public IronSwornRollStatement(String text) {
        this(text, null, null);
    }

    public IronSwornRollStatement(
            String text,
            OperatorExpression.Operator modifierOperator,
            Expression modivierExpression
    ) {
        super(text);
        this.modifierOperator = modifierOperator;
        this.modivierExpression = modivierExpression;
    }

    public OperatorExpression.Operator getModifierOperator() {
        return modifierOperator;
    }

    public Expression getModivierExpression() {
        return modivierExpression;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitIronSwornRollStatement(this);
    }
}
