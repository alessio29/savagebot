package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;
import org.alessio29.savagebot.internal.utils.ChannelConfig;
import org.alessio29.savagebot.internal.utils.ChannelConfigs;

public class InitBenniesAction implements IBotAction {
    @Override
    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {

        ChannelConfig config = ChannelConfigs.getChannelConfig(message.getChannelId());
        config.initBenniesPoool();
        return new CommandExecutionResult("Bennies pool initialized", args.length + 1);
    }
}
