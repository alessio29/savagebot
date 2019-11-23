package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.SavageBotRunner;
import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class InfoAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        CommandExecutionResult result = null;
        int count = -1;
        if (args.length>0) {
            String password = args[0].trim();
            if (SavageBotRunner.passwdOk(password)) {
                result = new CommandExecutionResult("Bot registered at "+count + " servers.", 2);
            }
        } else {
            result = new CommandExecutionResult("Password must be provided!", 1);
        }
        return result;
    }
}
