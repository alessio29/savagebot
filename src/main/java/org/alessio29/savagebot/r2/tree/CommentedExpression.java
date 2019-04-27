package org.alessio29.savagebot.r2.tree;

public class CommentedExpression extends Expression {
    private final String comment;
    private final Expression expression;

    public CommentedExpression(String text, String comment, Expression expression) {
        super(text);
        this.comment = comment;
        this.expression = expression;
    }

    public String getComment() {
        return comment;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitCommentedExpression(this);
    }
}
