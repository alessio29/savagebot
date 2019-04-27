package org.alessio29.savagebot.r2;

import org.alessio29.savagebot.r2.grammar.R2BaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

abstract class Desugarer<T> extends R2BaseVisitor<T> {
    protected final String inputString;

    public Desugarer(String inputString) {
        this.inputString = inputString;
    }

    protected String getOriginalText(ParserRuleContext ctx) {
        int start = ctx.start.getStartIndex();
        int end = ctx.stop.getStopIndex();
        return inputString.substring(start, end+1);
    }
}
