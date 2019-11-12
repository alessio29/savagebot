package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class DropCharacterAction {

    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name should be provided.", 1);
        }

        String name = args[0].trim();
        Character ch = Characters.getCharacterByName(guildId, channelId, name);
        if (ch == null) {
            return new CommandExecutionResult("No character with name " + name + " found.", 2);
        }

        if (ch.isOutOfFight()) {
            return new CommandExecutionResult("Character " + name + " is already out of fight.", 2);
        }

        ch.setOutOfFight(true);
        Characters.storeCharacter(guildId, channelId, ch);
        return new CommandExecutionResult("Character " + name + " is out of fight.", 2);
    }
}
