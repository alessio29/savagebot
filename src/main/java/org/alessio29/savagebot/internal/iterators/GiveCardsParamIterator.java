package org.alessio29.savagebot.internal.iterators;

public class GiveCardsParamIterator extends ParamsIterator {

    public GiveCardsParamIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return false;
    }

    @Override
    public boolean isEntity(String param) {
        return true;
    }

    @Override
    public String process(String value, String modifier, Object entity) {
        return null;
    }
}
