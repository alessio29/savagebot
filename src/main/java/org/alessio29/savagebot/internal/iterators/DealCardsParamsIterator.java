package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.internal.utils.Utils;

public class DealCardsParamsIterator extends  ParamsIterator {

    public DealCardsParamsIterator(String[] params) {
        super(params);
    }

    @Override
    public boolean isModifier(String param) {
        return Utils.isIntegerNumber(param);
    }

    @Override
    public boolean isEntity(String param) {
        return !isModifier(param);
    }

    @Override
    public Object process(String value, String modifier, Object entity) {
        return null;
    }
}
