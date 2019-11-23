package org.alessio29.savagebot.apiActions.admin;

import org.alessio29.savagebot.apiActions.IBotAction;
import org.alessio29.savagebot.internal.IMessageReceived;
import org.alessio29.savagebot.internal.builders.ReplyBuilder;
import org.alessio29.savagebot.internal.commands.CommandExecutionResult;

public class PingAction implements IBotAction {

    public CommandExecutionResult doAction(IMessageReceived message, String[] args) {
        return new CommandExecutionResult(
                new ReplyBuilder().attach("Hey, ").
                        attach(message.getAuthorMention()).
                        attach(" SavageBot is ready!").newLine().toString(),
                1);
    }
}
