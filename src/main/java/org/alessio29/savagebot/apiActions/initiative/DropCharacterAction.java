package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DropCharacterAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name should be provided.", 1);
        }
        List<String> removed = new ArrayList<>();
        List<String> notFound =  new ArrayList<>();
        List<String> alreadyOut =  new ArrayList<>();

        for (int i = 0; i<args.length; i++) {
            String name = args[i].trim();
            Character ch = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), name);
            if (ch == null) {
                notFound.add(name);
                continue;
            }

            if (ch.isOutOfFight()) {
                alreadyOut.add(name);
                continue;
            }
            ch.removeFromFight();
            removed.add(name);
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), ch);
        }

        String response = "";
        if (!notFound.isEmpty()) {
            response = "Character(s) " +StringUtils.join(notFound, ", ")+ " not found!\n";
        }
        if (!alreadyOut.isEmpty()) {
            response = response + "Character(s) " +StringUtils.join(alreadyOut, ", ")+ " already out of fight!\n";
        }
        if (!removed.isEmpty()) {
            response = response + "Character(s) " +StringUtils.join(removed, ", ")+ " removed from fight!\n";
        }

        return new CommandExecutionResult(response, args.length+1);
    }
}
