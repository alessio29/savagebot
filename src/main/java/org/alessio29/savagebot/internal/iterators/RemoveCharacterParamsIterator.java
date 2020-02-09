package org.alessio29.savagebot.internal.iterators;

public class RemoveCharacterParamsIterator extends ParamsIterator {

    public RemoveCharacterParamsIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return false;
    }

    @Override
    public boolean isEntity(String param) {
        return false;
    }

    @Override
    public Object process(String value, String modifier, Object entity) {
        return null;
    }
}
