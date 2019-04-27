package org.alessio29.savagebot.r2.tree;

public class ErrorStatement extends Statement {
    private final String errorMessage;

    public ErrorStatement(String text, String errorMessage) {
        super(text);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitErrorStatement(this);
    }
}
