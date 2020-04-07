package org.alessio29.savagebot.apiActions.initiative;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.DropCharacterParamsIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DropCharacterAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name should be provided.", 1);
        }

        if (args[0].equalsIgnoreCase("all")) {
            Set<Character> allChars = Characters.getFightingCharacters(message.getGuildId(), message.getChannelId());
            for (Character ch : allChars ) {
                ch.removeFromFight();
            }
            return new CommandExecutionResult("All characters dropped from fight.", args.length+1);
        }

        List<String> removed = new ArrayList<>();
        List<String> notFound =  new ArrayList<>();
        List<String> alreadyOut =  new ArrayList<>();

        DropCharacterParamsIterator it = new DropCharacterParamsIterator(args);
        while (it.hasNext()) {
            String value = it.next();
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), value);
            if (character != null) {
                DropCharacterParamsIterator.DropProcessResult result = it.process(null, character);
                switch (result) {
                    case REMOVED:
                        removed.add(value);
                        break;
                    case ALREADY_OUT:
                        alreadyOut.add(value);
                        break;
                    case NOT_FOUND:
                        notFound.add(value);
                        break;
                }
                if (result != DropCharacterParamsIterator.DropProcessResult.ERROR) {
                    Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
                }
            }
        }

        String response = "";
        if (!notFound.isEmpty()) {
            response = "Character(s) " + StringUtils.join(notFound, ", ")+ " not found.\n";
        }
        if (!alreadyOut.isEmpty()) {
            response = response + "Character(s) " +StringUtils.join(alreadyOut, ", ")+ " already out of fight.\n";
        }
        if (!removed.isEmpty()) {
            response = response + "Character(s) " +StringUtils.join(removed, ", ")+ " removed from fight.\n";
        }
        return new CommandExecutionResult(response, args.length+1);
    }
}
