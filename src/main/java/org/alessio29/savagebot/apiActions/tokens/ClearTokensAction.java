package org.alessio29.savagebot.apiActions.tokens;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.ClearTokensParamsIterator;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class ClearTokensAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Please provide character name or 'all' to clear all characters", 1);
        }
        List<String> removed = new ArrayList<>();
        ClearTokensParamsIterator it = new ClearTokensParamsIterator(args);
        List<Character> chars = new ArrayList<>();
        if (args[0].trim().toLowerCase().equals("all")) {
            chars.addAll(Characters.getCharacters(message.getGuildId(), message.getChannelId()).values());
        } else {
            while (it.hasNext()) {
                String value = it.next();
                chars.add(Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), value));
            }
        }
        Collection<String> names = it.process(null, chars);
        if (names != null) {
            removed.addAll(names);
        }
        return new CommandExecutionResult("Removed tokens for character(s) " + StringUtils.join(removed, ", "), args.length + 1);
    }
}
