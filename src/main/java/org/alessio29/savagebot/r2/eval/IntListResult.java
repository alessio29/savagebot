package org.alessio29.savagebot.r2.eval;

import java.util.List;

public class IntListResult {
    private final List<Integer> values;
    private final String explained;

    public IntListResult(List<Integer> values, String explained) {
        this.values = values;
        this.explained = explained;
    }

    public List<Integer> getValues() {
        return values;
    }

    public String getExplained() {
        return explained;
    }
}
