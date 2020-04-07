package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.initiative.DrawCardResult;

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
    public String process(String modifier, Object entity) {
        return null;
    }
}
