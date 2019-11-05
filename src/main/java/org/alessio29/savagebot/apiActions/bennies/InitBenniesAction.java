package org.alessio29.savagebot.apiActions.bennies;

import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InitBenniesAction  {

    private static final String RESET = "fill";


    public CommandExecutionResult doAction(String guildId, String channelId, String[] args) {
        boolean reset = (args.length>0) && args[0].equals(RESET);
        Hat hat = Hats.getHat(guildId, channelId, reset);
        if (reset) {
            Pockets.resetPockets(guildId, channelId);
        }
        return new CommandExecutionResult(hat.getInfo(), ((reset)?2:1) );
    }
}
