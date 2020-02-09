package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.State;

public class ClearStatesParamIterator extends ParamsIterator {

    public ClearStatesParamIterator(String[] args) {
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
        if (entity == null) {
            return null;
        }
        if (!(entity instanceof Character)) {
            return null;
        }
        Character character = (Character)entity;
        State state = State.getStateFromString(modifier);
        character.addState(state);
        return state.toString()+" to "+value;
    }
}
