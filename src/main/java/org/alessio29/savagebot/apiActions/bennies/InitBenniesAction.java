package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InitBenniesAction implements IBotAction {

    private static final String RESET = "fill";


    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        boolean reset = (args.length > 0) && args[0].equals(RESET);
        Hat hat = Hats.getHat(message.getGuildId(), message.getChannelId(), reset);
        if (reset) {
            Pockets.resetPockets(message.getGuildId(), message.getChannelId());
        }
        return new CommandExecutionResult(hat.getInfo(), ((reset) ? 2 : 1));
    }
}
