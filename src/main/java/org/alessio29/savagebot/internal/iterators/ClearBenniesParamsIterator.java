package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;

import java.util.ArrayList;
import java.util.Collection;

public class ClearBenniesParamsIterator extends ParamsIterator {
    public ClearBenniesParamsIterator(String[] args) {
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
    public Collection<String> process(String modifier, Object entity) {
        if (entity == null) {
            return null;
        }
        if (!(entity instanceof Collection)) {
            return null;
        }
        Collection collection = (Collection) entity;
        Collection<String> result = new ArrayList<>();

        for (Object item : collection) {
            if (!(item instanceof Character)) {
                continue;
            }
            Character character = (Character) item;
            character.removeAllBennies();
            result.add(character.getName());
        }
        return result;
    }
}
