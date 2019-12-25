package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class DropCharacterAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name should be provided.", 1);
        }

        String name = args[0].trim();
        Character ch = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), name);
        if (ch == null) {
            return new CommandExecutionResult("No character with name " + name + " found.", 2);
        }

        if (ch.isOutOfFight()) {
            return new CommandExecutionResult("Character " + name + " is already out of fight.", 2);
        }

        ch.removeFromFight();
        Characters.storeCharacter(message.getGuildId(), message.getChannelId(), ch);
        return new CommandExecutionResult("Character " + name + " is out of fight.", 2);
    }
}
