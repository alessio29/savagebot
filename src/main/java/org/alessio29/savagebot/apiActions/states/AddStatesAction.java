package org.alessio29.savagebot.apiActions.states;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.characters.State;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.ArrayList;
import java.util.List;

public class AddStatesAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        if (args.length < 2) {
            return new CommandExecutionResult("State(s) name missing!", 2);
        }
        List<State> states2add = new ArrayList<>();
        for (int i=1; i<args.length; i++) {
            State s = State.getStateFromString(args[i]);
            if (s!=null) {
                states2add.add(s);
            }
        }
        if (states2add.isEmpty()) {
            return new CommandExecutionResult("No valid states to add!", args.length+1);
        }

        Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), args[0]);
        if (character == null) {
            character = new Character(args[0]);
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
        }
        String messageStr = "State(s) added for character "+character.getName();
        for (State s : states2add) {
            character.addState(s);
        }
        return new CommandExecutionResult(messageStr, args.length+1);    }
}
