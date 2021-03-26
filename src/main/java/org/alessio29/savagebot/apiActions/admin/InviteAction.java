package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InviteAction {

    private static final String INVITE_LINK =
            "https://discordapp.com/oauth2/authorize?&client_id=448952545784758303&scope=bot&permissions=0";

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        return new CommandExecutionResult("Invite link: " + INVITE_LINK + "\n", 1);
    }
}
