package org.alessio29.savagebot.apiActions.states;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.characters.State;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.ArrayList;
import java.util.List;

public class RemoveStateAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        if (args.length < 2) {
            return new CommandExecutionResult("State(s) name missing!", 2);
        }

        List<State> states2remove = new ArrayList<>();

        for (int i=1; i<args.length; i++) {
            State s = State.valueOfOrNull(args[i].trim().toUpperCase());
            if (s!=null) {
                states2remove.add(s);
            }
        }
        if (states2remove.isEmpty()) {
            return new CommandExecutionResult("No valid states to remove!", args.length);
        }

        Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), args[0]);
        if (character == null) {
            return new CommandExecutionResult("Cannot find character named " + args[0], args.length+1);
        }
        String messageStr = "State(s) removed from character "+character.getName();
        for (State s : states2remove) {
            character.removeState(s);
        }
        return new CommandExecutionResult(messageStr, args.length+1);

    }
}
