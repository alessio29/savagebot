package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.bennies.BennyType;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.iterators.ParamsIterator;
import org.alessio29.savagebot.internal.iterators.PullColoredBenniesParamsIterator;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PullBenniesAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Character name missing!", 1);
        }
        ChannelConfig channelConfig = ChannelConfigs.getChannelConfig(message.getChannelId());

        if (channelConfig.getBennyType().equals(BennyType.NORMAL)) {
            return new CommandExecutionResult("Bennies can be pulled only for Deadlands. " +
                    "Switch bennies type by using command `!sbm d` or use `gb` command to give normal bennies.", args.length + 1);
        }

        ParamsIterator it = new PullColoredBenniesParamsIterator(args);
        List<String> pulled = new ArrayList<>();

        while (it.hasNext()) {
            String value = it.next().trim();
            if (!it.isEntity(value)) {
                return new CommandExecutionResult("Provide character name!", args.length + 1);
            }
            Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), value);
            String val = "1";
            if (it.nextIsModifier()) {
                val = it.next().trim().toLowerCase();
            }

            Map<BennyColor, Integer> newBennies = channelConfig.pullBennies(Integer.parseInt(val));
            newBennies.entrySet().stream().forEach(entry -> character.addColoredBennies(entry));
            String info = StringUtils.join(
                    newBennies.entrySet().stream().map(p -> p.getValue().toString().toLowerCase() + " " + p.getKey()).
                            collect(Collectors.toList()), ",");
            pulled.add(character.getName() + " pulled " + info + " benny(ies)");
            Characters.storeCharacter(message.getGuildId(), message.getChannelId(), character);
        }
        return new CommandExecutionResult("Puled bennies for character(s): " + StringUtils.join(pulled, ", "), args.length + 1);
    }
}