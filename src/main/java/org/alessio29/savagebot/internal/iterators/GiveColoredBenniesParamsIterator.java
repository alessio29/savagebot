package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.characters.Character;

import java.util.regex.Pattern;

public class GiveColoredBenniesParamsIterator extends ParamsIterator {

    private static final Pattern bennyPattern = Pattern.compile("\\d*[w|b|r|g]");

    public GiveColoredBenniesParamsIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return (bennyPattern.matcher(param.trim().toLowerCase()).matches());
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
            return null;
        }
        Character character = (Character) entity;
        character.addColoredBennies(BennyColor.parseBennies(modifier));
        return modifier + " to " + character.getName();
    }
}
