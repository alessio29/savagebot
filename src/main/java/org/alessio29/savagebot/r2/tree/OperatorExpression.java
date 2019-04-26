package org.alessio29.savagebot.r2.tree;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OperatorExpression extends Expression {
    public enum Operator {
        PLUS(2, "+"), MINUS(2, "-"), MUL(2, "*"), DIV(2, "/"), MOD(2, "%"),
        UNARY_PLUS(1, "+"), UNARY_MINUS(1, "-");
        private final int arity;
        private final String image;

        Operator(int arity, String image) {
            this.arity = arity;
            this.image = image;
        }

        public int getArity() {
            return arity;
        }

        public String getImage() {
            return image;
        }
    }

    private static final Map<String, Operator> UNARY_OPERATORS =
            Arrays.stream(Operator.values())
                    .filter(op -> op.arity == 1)
                    .collect(Collectors.toMap(Operator::getImage, op -> op));

    public static Operator getUnaryOperator(String image) {
        return UNARY_OPERATORS.get(image);
    }

    private static final Map<String, Operator> BINARY_OPERATORS =
            Arrays.stream(Operator.values())
                    .filter(op -> op.arity == 2)
                    .collect(Collectors.toMap(Operator::getImage, op -> op));

    public static Operator getBinaryOperator(String image) {
        return BINARY_OPERATORS.get(image);
    }

    private final Operator operator;
    private final Expression argument1;
    private final Expression argument2;

    public OperatorExpression(String text, Operator operator, Expression argument1, Expression argument2) {
        super(text);
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
    }

    public OperatorExpression(String text, Operator operator, Expression argument1) {
        this(text, operator, argument1, null);
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getArgument1() {
        return argument1;
    }

    public Expression getArgument2() {
        return argument2;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitOperatorExpression(this);
    }
}
