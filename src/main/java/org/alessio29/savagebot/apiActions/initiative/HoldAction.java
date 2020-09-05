package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class HoldAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        String charName;
        String actionName;
        if (args.length == 0) {
            return new CommandExecutionResult("Provide character name", 1);
        }
        boolean putOnHold = true;
        charName = args[0];
        if (charName.startsWith("-")) {
            actionName = " returned to fight";
            charName = charName.substring(1);
            putOnHold = false;
        } else {
            actionName = " put on hold";
        }
        Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), charName);
        if (character == null) {
            return new CommandExecutionResult("Character with name "+charName+" not found", 2);
        }

        if (putOnHold) {
            character.hold();
        } else {
            character.returnToFight();
        }
        return new CommandExecutionResult("Character " + charName + actionName, 2);
    }
}
