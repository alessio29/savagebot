package org.alessio29.savagebot.internal.iterators;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.State;

public class ManageStatesParamIterator extends ParamsIterator {

    public ManageStatesParamIterator(String[] args) {
        super(args);
    }

    @Override
    public boolean isModifier(String param) {

        if (param == null || param.trim().isEmpty()) {
            return false;
        }

        if ("clear".equalsIgnoreCase(param)) {
            return true;
        }

        if (param.startsWith("+") || param.startsWith("-")) {
            return State.getStateFromString(param.substring(1))!=null;
        }

        return State.getStateFromString(param)!=null;
    }

    @Override
    public boolean isEntity(String param) {
        return !isModifier(param);
    }

    @Override
    public String process(String modifier, Object entity) {

        String result = "";
        if (entity == null) {
            return null;
        }
        if (!(entity instanceof Character)) {
            return null;
        }
        Character character = (Character)entity;

        if (modifier.equalsIgnoreCase("clear")) {
            // clear states for character
            character.clearStates();
            result = "All states cleared for "+character.getName();
        }

        if (modifier.startsWith("-")) {
            State state = State.getStateFromString(modifier.substring(1));
            if (state != null ) {
                character.removeState(state);
                result = state.toString()+" removed from "+character.getName();
            }
        }

        if (modifier.startsWith("+")) {
            State state = State.getStateFromString(modifier.substring(1));
            if (state != null) {
                character.removeState(state);
                result = state.toString() + " removed from " + character.getName();

            }
        }

        State state = State.getStateFromString(modifier);
        if (state!= null) {
            character.addState(state);
            result = state.toString()+" added to "+character.getName();
        }

        return result;
    }
}
