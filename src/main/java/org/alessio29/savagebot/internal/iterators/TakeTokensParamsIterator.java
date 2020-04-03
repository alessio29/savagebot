package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.internal.utils.Utils;

public class TakeTokensParamsIterator extends ParamsIterator {

    public TakeTokensParamsIterator(String[] args) {
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
    public String process(String modifier, Object entity) {
        if (entity == null) {
            return null;
        }
        if (!(entity instanceof Character)) {
            return null;
        }
        if (modifier == null) {
            modifier = "1";
        }
        Character character = (Character)entity;
        character.removeTokens(Integer.parseInt(modifier));
        return modifier+" from "+character.getName();
    }
}
