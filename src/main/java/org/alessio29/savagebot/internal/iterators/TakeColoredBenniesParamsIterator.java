package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.internal.utils.Utils;

public class TakeColoredBenniesParamsIterator extends ParamsIterator {
    public TakeColoredBenniesParamsIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return (Utils.bennyPattern.matcher(param.trim().toLowerCase()).matches());
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
        character.takeColoredBennies(BennyColor.parseBennies(modifier));
        return modifier + " from " + character.getName();
    }
}
