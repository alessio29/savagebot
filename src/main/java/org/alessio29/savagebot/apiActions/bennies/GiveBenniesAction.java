package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.GiveBenniesParamsIterator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GiveBenniesAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        List<String> given = new ArrayList<>();
        GiveBenniesParamsIterator it = new GiveBenniesParamsIterator(args);

        while (it.hasNext()) {
            String value = it.next();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length+1);
            }
            if (value.equalsIgnoreCase("all")) {
                continue;
            }
            Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
            String modifier = null;
            Integer tokens = 1;
            if (it.nextIsModifier()) {
                modifier = it.next().trim();
                tokens = Integer.parseInt(modifier);
            }
            if (tokens>0) {
                given.add(it.process(modifier, character));
                Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
            }
        }
        return new CommandExecutionResult("Given bennies to character(s): "+ StringUtils.join(given, ", "), args.length + 1);
    }
}
