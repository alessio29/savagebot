package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.ParamsIterator;
import org.alessio29.savagebot.internal.iterators.TakeBenniesParamsIterator;
import org.alessio29.savagebot.internal.iterators.TakeColoredBenniesParamsIterator;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TakeBenniesAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }

        List<String> taken = new ArrayList<>();
        ParamsIterator it;
        ChannelConfig channelConfig = ChannelConfigs.getChannelConfig(message.getChannelId());
        if (channelConfig.normalBennies()) {
            it = new TakeBenniesParamsIterator(args);
        } else {
            // Deadlands Reloaded bennies
            it = new TakeColoredBenniesParamsIterator(args);
        }

        while (it.hasNext()) {
            String value = it.next().trim();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length+1);
            }

            Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
            if (channelConfig.normalBennies()) {
                String benniesCount = null;
                if (it.nextIsModifier()) {
                    benniesCount = it.next().trim().toLowerCase();
                }
                taken.add((String)it.process(benniesCount, character));
            } else {
                while (it.nextIsModifier()) {
                    String val = it.next().trim().toLowerCase();
                    taken.add((String)it.process(val, character));
                }
            }
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
        }
        return new CommandExecutionResult("Taken bennies from character(s): "+ StringUtils.join(taken, ", "), args.length + 1);

    }
}
