package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.BennyColor;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;

public class AddBennyAction implements IBotAction {
    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        if (args.length < 1) {
            return new CommandExecutionResult("Command syntax: ab <bennyColor>", args.length + 1);
        }
        ChannelConfig config = ChannelConfigs.getChannelConfig(message.getChannelId());

        BennyColor color = BennyColor.get(args[0]);
        if (color == null) {
            return new CommandExecutionResult("Benny color must be one of: 'w', 'b','r' or 'g'", args.length + 1);
        }
        config.addBennyToPool(color);
        return new CommandExecutionResult("Added "+color.toString().toLowerCase()+" benny to pool", args.length + 1);
    }
}
