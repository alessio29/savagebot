package org.alessio29.savagebot.apiActions.admin;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.SavageBotRunner;
import org.alessio29.savagebot.apiActions.IDiscordAction;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InfoAction implements IDiscordAction {
    @Override
    public CommandExecutionResult doAction(MessageReceivedEvent event, String[] args) {
        CommandExecutionResult result = null;
        if (args.length>0) {
            String password = args[0].trim();
            if (SavageBotRunner.passwdOk(password)) {
                int serversCount = event.getJDA().getGuilds().size();
                result = new CommandExecutionResult("Bot registered at "+serversCount + " servers.", 2);
            }
        } else {
            result = new CommandExecutionResult("Password must be provided!", 1);
        }
        return result;
    }
}
