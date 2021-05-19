package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.alessio29.savagebot.internal.utils.Utils;

import java.util.Map;

public class PullColoredBenniesParamsIterator extends ParamsIterator {
    public PullColoredBenniesParamsIterator(String[] args) {
        super(args);
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
    public Object process(String modifier, Object entity) {
        if (entity == null) {
            return null;
        }
        if (!(entity instanceof Character)) {
            return null;
        }
        if (modifier == null) {
            return null;
        }
        Character character = (Character) entity;

        return null;
    }
}
