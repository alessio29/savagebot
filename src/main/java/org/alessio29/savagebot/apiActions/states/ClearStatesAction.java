package org.alessio29.savagebot.apiActions.states;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Map;

public class ClearStatesAction implements IBotAction {

    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length <1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
        }
        Map<String, Character> chars = Characters.getCharacters(message.getGuildId(), message.getChannelId());
        String text;
        if (args[0].trim().toLowerCase().equals("all")) {

            for (Character c : chars.values()) {
                c.clearStates();
                Characters.storeCharacter(message.getGuildId(), message.getChannelId(), c);
            }
            text = "Cleared states for all characters";
        } else {
            chars.get(args[0]).clearStates();
            text = "Cleared states for character "+args[0];
        }
        return new CommandExecutionResult(text, args.length+1);
    }
}
