package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.GiveBenniesParamsIterator;
import org.alessio29.savagebot.internal.iterators.GiveColoredBenniesParamsIterator;
import org.alessio29.savagebot.internal.iterators.ParamsIterator;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GiveBenniesAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        List<String> given = new ArrayList<>();
        ParamsIterator it;
        ChannelConfig channelConfig = ChannelConfigs.getChannelConfig(message.getChannelId());
        if (channelConfig.normalBennies()) {
            it = new GiveBenniesParamsIterator(args);
        } else {
            // Deadlands Reloaded bennies
            it = new GiveColoredBenniesParamsIterator(args);
        }

        while (it.hasNext()) {
            String value = it.next().trim();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length+1);
            }

            Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
            if (channelConfig.normalBennies()) {
                if (it.nextIsModifier()) {
                    given.add((String)it.process(it.next().trim().toLowerCase(), character));
                    Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
                } else {
                    return new CommandExecutionResult("Provide benny type to give!", args.length + 1);
                }
            } else {
                while (it.nextIsModifier()) {
                    String val = it.next().trim().toLowerCase();
                    given.add((String)it.process(val, character));
                }
                Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
            }

        }
        return new CommandExecutionResult("Given bennies to character(s): "+ StringUtils.join(given, ", "), args.length + 1);
    }
}
