package org.alessio29.savagebot.r2.tree;

public class FlagStatement extends Statement {
    private final String flag;

    public FlagStatement(String text, String flag) {
        super(text);
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    @Override
    public <V> V accept(Visitor<V> visitor) {
        return visitor.visitFlagStatement(this);
    }
}
