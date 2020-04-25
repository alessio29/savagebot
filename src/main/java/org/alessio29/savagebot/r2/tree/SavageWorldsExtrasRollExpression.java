package org.alessio29.savagebot.r2.tree;

public class SavageWorldsExtrasRollExpression extends Expression {
    private final Expression facetsArg;
    private final OperatorExpression.Operator modifierOperator;
    private final Expression modifierArg;

    public SavageWorldsExtrasRollExpression(
            String text,
            Expression facetsArg,
            OperatorExpression.Operator modifierOperator,
            Expression modifierArg
    ) {
        super(text);
        this.facetsArg = facetsArg;
        this.modifierOperator = modifierOperator;
        this.modifierArg = modifierArg;
    }

    public Expression getFacetsArg() {
        return facetsArg;
    }

    public OperatorExpression.Operator getModifierOperator() {
        return modifierOperator;
    }

    public Expression getModifierArg() {
        return modifierArg;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitExtrasRollExpression(this);
    }
}
