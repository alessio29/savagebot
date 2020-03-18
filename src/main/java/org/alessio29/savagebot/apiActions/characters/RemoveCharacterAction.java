package org.alessio29.savagebot.apiActions.characters;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.RemoveCharacterParamsIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RemoveCharacterAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("No character name(s) provided!", 1);
        }

        RemoveCharacterParamsIterator it = new RemoveCharacterParamsIterator(args);
        List<String> charsNotFound = new ArrayList<>();
        List<String> charsToRemove = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            Character ch = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), args[i].trim());
            if (ch == null) {
                charsNotFound.add(ch.getName());
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
        return new CommandExecutionResult(response, args.length+1);
    }
}