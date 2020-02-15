package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.State;

public class RemoveStatesParamIterator extends ParamsIterator {

    public RemoveStatesParamIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {
        return State.getStateFromString(param)!=null;
    }

    @Override
    public boolean isEntity(String param) {
        return !isModifier(param);
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
        character.removeState(state);
        return state.toString()+" from "+value;
    }
}
