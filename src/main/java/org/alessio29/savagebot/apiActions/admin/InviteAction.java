package org.alessio29.savagebot.apiActions.admin;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InviteAction implements IDiscordAction {

    private static final String INVITE_LINK =
            "https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0";

    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        return new CommandExecutionResult("Invite link: " + INVITE_LINK + "\n", 1);
    }
}
