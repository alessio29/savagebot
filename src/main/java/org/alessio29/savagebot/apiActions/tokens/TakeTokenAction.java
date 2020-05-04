package org.alessio29.savagebot.apiActions.tokens;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.TakeTokensParamsIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeTokenAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        List<String> taken = new ArrayList<>();
        List<String> notFound = new ArrayList<>();
        TakeTokensParamsIterator it = new TakeTokensParamsIterator(args);

        while (it.hasNext()) {
            String value = it.next();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length + 1);
            }
            Character character = Characters.getCharacterByName(message.getGuildId(), message.getChannelId(), value);
            if (character == null) {
                notFound.add(value);
                continue;
            }
            String modifier = null;
            Integer tokens = 1;
            if (it.nextIsModifier()) {
                modifier = it.next().trim();
                tokens = Integer.parseInt(modifier);
            }
            if (tokens > 0) {
                taken.add(it.process(modifier, character));
                Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
            }
        }
        String messageStr = "";
        if (!taken.isEmpty()) {
            messageStr += "Taken tokens from character(s): " + StringUtils.join(taken, ", ");
        }
        if (!notFound.isEmpty()) {
            messageStr += "Character(s) not found: " + StringUtils.join(notFound, ", ");
        }

        return new CommandExecutionResult(messageStr, args.length + 1);
    }
}
