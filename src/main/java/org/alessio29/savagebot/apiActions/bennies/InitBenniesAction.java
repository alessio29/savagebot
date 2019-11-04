package org.alessio29.savagebot.apiActions.bennies;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.bennies.Hat;
import org.alessio29.savagebot.bennies.Hats;
import org.alessio29.savagebot.bennies.Pockets;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InitBenniesAction implements IDiscordAction {

    private static final String RESET = "fill";

    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        boolean reset = (args.length>0) && args[0].equals(RESET);
        Hat hat = Hats.getHat(event.getGuild(), event.getTextChannel(), reset);
        if (reset) {
            Pockets.resetPockets(event.getGuild(), event.getTextChannel());
        }
        return new CommandExecutionResult(hat.getInfo(), ((reset)?2:1) );
    }
}
