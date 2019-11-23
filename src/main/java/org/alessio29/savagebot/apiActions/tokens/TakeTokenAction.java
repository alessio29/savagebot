package org.alessio29.savagebot.apiActions.tokens;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class TakeTokenAction {

    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        int tokens = 1;
        if (args.length > 1) {
            try {
                tokens = Integer.parseInt(args[1]);
            } catch (Exception ignored) {
            }
        }

        Character character = Characters.getCharacterByName(guildId, channelId, args[0]);
        if (character == null) {
            return new CommandExecutionResult("Cannot find character named " + args[0], 1);
        }
        if (tokens > 0) {
            character.removeTokens(tokens);
            Characters.storeCharacter(guildId, channelId, character);
            return new CommandExecutionResult(tokens + " token(s) taken from character " + character.getName(), args.length+1);
        }
        return new CommandExecutionResult("Only positive values can be added!", 2);
    }
}
