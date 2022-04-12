package org.alessio29.savagebot.r2.tree;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class OperatorExpression extends Expression {
    public enum OperatorKind {
        BINARY(2),
        PREFIX(1),
        BRACKETS(1),
        TERNARY(3);

        private final int arity;

        OperatorKind(int arity) {
            this.arity = arity;
        }

        public int getArity() {
            return arity;
        }
    }

    public enum Operator {
        PLUS(OperatorKind.BINARY, "+"),
        MINUS(OperatorKind.BINARY, "-"),
        MUL(OperatorKind.BINARY, "*", true, "×"),
        DIV(OperatorKind.BINARY, "/"),
        MOD(OperatorKind.BINARY, "%"),
        UNARY_PLUS(OperatorKind.PREFIX, "+"),
        UNARY_MINUS(OperatorKind.PREFIX, "-"),
        BRACKETS(OperatorKind.BRACKETS, "(", ")"),
        BOUND_TO(OperatorKind.TERNARY, "[", ":", "]");

        private final OperatorKind kind;
        private final String image1;
        private final String image2;
        private final String image3;
        private final String outputImage;

        Operator(OperatorKind kind, String image, boolean marker, String outputImage) {
            this.kind = kind;
            this.image1 = image;
            this.image2 = "";
            this.image3 = "";
            this.outputImage = outputImage;
        }

        Operator(OperatorKind kind, String image) {
            this(kind, image, "");
        }

        Operator(OperatorKind kind, String image1, String image2) {
            this(kind, image1, image2, "");
        }

        Operator(OperatorKind kind, String image1, String image2, String image3) {
            this.kind = kind;
            this.image1 = image1;
            this.image2 = image2;
            this.image3 = image3;
            this.outputImage = image1;
        }

        public OperatorKind getKind() {
            return kind;
        }

        public int getArity() {
            return kind.getArity();
        }

        public String getImage() {
            return image1;
        }

        public String getImage1() {
            return image1;
        }

        public String getImage2() {
            return image2;
        }

        public String getImage3() {
            return image3;
        }

        public String getOutputImage() {
            return outputImage;
        }
    }

    private static final Map<String, Operator> UNARY_OPERATORS =
            Arrays.stream(Operator.values())
                    .filter(op -> op.getArity() == 1)
                    .collect(Collectors.toMap(Operator::getImage, op -> op));

    public static Operator getUnaryOperator(String image) {
        return UNARY_OPERATORS.get(image);
    }

    private static final Map<String, Operator> BINARY_OPERATORS =
            Arrays.stream(Operator.values())
                    .filter(op -> op.getArity() == 2)
                    .collect(Collectors.toMap(Operator::getImage, op -> op));

    public static Operator getBinaryOperator(String image) {
        return BINARY_OPERATORS.get(image);
    }

    private final Operator operator;
    private final Expression argument1;
    private final Expression argument2;
    private final Expression argument3;

    public OperatorExpression(String text, Operator operator, Expression argument1, Expression argument2, Expression argument3) {
        super(text);
        this.operator = operator;
        this.argument1 = argument1;
        this.argument2 = argument2;
        this.argument3 = argument3;
    }

    public OperatorExpression(String text, Operator operator, Expression argument1, Expression argument2) {
        this(text, operator, argument1, argument2, null);
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

    public Expression getArgument3() {
        return argument3;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitOperatorExpression(this);
    }
}
