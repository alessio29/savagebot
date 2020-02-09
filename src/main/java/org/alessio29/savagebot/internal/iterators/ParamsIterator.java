package org.alessio29.savagebot.internal.iterators;

import java.util.Arrays;
import java.util.List;

public abstract class ParamsIterator {

    private List<String> params;
    private Integer index;

    public ParamsIterator(String[] params) {
        assert params != null;
        assert params.length > 0;
        this.params = Arrays.asList(params);
        this.index = -1;
    }

    public boolean hasNext() {
        return index < params.size() - 1;
    }

    public boolean nextIsModifier() {
        if (hasNext()) {
            return isModifier(params.get(index + 1));
        }
        return false;
    }

    public abstract boolean isModifier(String param);

    public abstract boolean isEntity(String param);

    public String next() {
        assert hasNext();
        this.index++;
        return this.params.get(index).trim();
    }

    public abstract Object process(String value, String modifier, Object entity);
}
