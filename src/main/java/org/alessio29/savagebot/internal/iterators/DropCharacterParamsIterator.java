package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;

public class DropCharacterParamsIterator extends ParamsIterator {
    public DropCharacterParamsIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return false; // no modifiers
    }

    @Override
    public boolean isEntity(String param) {
        return true; // all elements of list are entities
    }

    @Override
    public DropProcessResult process(String value, String modifier, Object entity) {

        if (entity == null || !(entity instanceof Character)) {
            return DropProcessResult.ERROR;
        }
        Character ch = (Character) entity;
        if (ch == null) {
            return DropProcessResult.NOT_FOUND;
        }
        if (ch.isOutOfFight()) {
            return DropProcessResult.ALREADY_OUT;
        }
        ch.removeFromFight();
        return DropProcessResult.REMOVED;
    }

    public enum DropProcessResult {
        NOT_FOUND,
        ALREADY_OUT,
        REMOVED,
        ERROR;
    }
}
