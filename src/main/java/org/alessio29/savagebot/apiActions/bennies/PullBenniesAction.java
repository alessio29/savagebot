package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.characters.Character;
import org.alessio29.savagebot.characters.Characters;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

public class PullBenniesAction implements IBotAction {
    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        if (args.length < 1) {
            return new CommandExecutionResult("Command syntax: pb <CharName> [bennyCount]", args.length + 1);
        }
        String charName = args[0];
        Character character = Characters.getByNameOrCreate(message.getGuildId(), message.getChannelId(), charName);
        int count = 1;
        if (args.length > 1) {
            count = Integer.parseInt(args[1]);
        }

        ChannelConfig config = ChannelConfigs.getChannelConfig(message.getChannelId());
        Map<BennyColor, Integer> map = config.pullBennies(count);
        for (Map.Entry<BennyColor, Integer> pair : map.entrySet()) {
            character.addColoredBennies(pair);
        }
        String info = StringUtils.join(
                map.entrySet().stream().map(p ->  p.getValue().toString().toLowerCase() + " " + p.getKey()).
                        collect(Collectors.toList()), ",");

        return new CommandExecutionResult(charName + " pulled " + info + " benny(ies)", args.length + 1);
    }
}
