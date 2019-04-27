package org.alessio29.savagebot.r2.tree;

public class NonParsedStringStatement extends Statement {
    private final String parserErrorMessage;

    public NonParsedStringStatement(String text, String parserErrorMessage) {
        super(text);
        this.parserErrorMessage = parserErrorMessage;
    }

    public String getParserErrorMessage() {
        return parserErrorMessage;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitNonParsedStringStatement(this);
    }
}
