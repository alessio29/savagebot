package org.alessio29.savagebot.apiActions.characters;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RemoveCharacterAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("No character name(s) provided!", 1);
        }

        if (args[0].equalsIgnoreCase("all")) {
            Characters.removeAllCharacters(message.getGuildId(), message.getChannelId());
            return new CommandExecutionResult("All characters removed.", args.length + 1);
        }
        List<String> charsNotFound = new ArrayList<>();
        List<String> charsToRemove = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            Character ch = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), args[i].trim());
            if (ch == null) {
                charsNotFound.add(args[i].trim());
                continue;
            }
            charsToRemove.add(ch.getName());
        }

        for (String charName : charsToRemove) {
            Characters.removeCharacter(message.getGuildId(), message.getChannelId(), charName);
        }
        String response = "";
        if (!charsNotFound.isEmpty()) {
            response = "Character(s) not found: " + StringUtils.join(charsNotFound, ", ") + "\n";
        }
        if (!charsToRemove.isEmpty()) {
            response = response + "Character(s) removed: " + StringUtils.join(charsToRemove, ", ");
        }
        return new CommandExecutionResult(response, args.length + 1);
    }
}