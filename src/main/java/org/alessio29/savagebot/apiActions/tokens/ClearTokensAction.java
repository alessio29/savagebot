package org.alessio29.savagebot.apiActions.tokens;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

import java.util.Set;

public class ClearTokensAction {

    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {
        if (args.length <1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
        }
        Set<Character> chars = Characters.getCharacters(guildId, channelId);
        String text;
        if (args[0].trim().toLowerCase().equals("all")) {
            chars.clear();
            text = "Removed all characters";
        } else {
            chars.remove(new Character(args[0]));
            text = "Removed character "+args[0];
        }
        Characters.storeAllCharacters(guildId, channelId, chars);
        return new CommandExecutionResult(text, args.length+1);
    }
}
