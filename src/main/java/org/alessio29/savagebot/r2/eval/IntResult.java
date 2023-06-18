package org.alessio29.savagebot.r2.eval;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IntResult {
    private final int value;
    private final String explained;

    public IntResult(int value, String explained) {
        this.value = value;
        this.explained = explained;
    }

    public int getValue() {
        return value;
    }

    public List<Integer> getSingletonValue() {
        return Collections.singletonList(value);
    }

    public String getExplained() {
        return explained;
    }

    public static final Comparator<IntResult> BY_VALUE = Comparator.comparingInt(IntResult::getValue);
}
